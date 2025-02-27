package de.symeda.sormas.backend.therapy;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.therapy.PrescriptionCriteria;
import de.symeda.sormas.api.therapy.PrescriptionDto;
import de.symeda.sormas.api.therapy.PrescriptionExportDto;
import de.symeda.sormas.api.therapy.PrescriptionFacade;
import de.symeda.sormas.api.therapy.PrescriptionIndexDto;
import de.symeda.sormas.api.therapy.PrescriptionReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.caze.CaseQueryContext;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.JurisdictionHelper;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.Pseudonymizer;
import de.symeda.sormas.backend.util.QueryHelper;

@Stateless(name = "PrescriptionFacade")
@RolesAllowed(UserRight._CASE_VIEW)
public class PrescriptionFacadeEjb implements PrescriptionFacade {

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private PrescriptionService service;
	@EJB
	private UserService userService;
	@EJB
	private TherapyService therapyService;
	@EJB
	private CaseService caseService;

	@Override
	public List<PrescriptionIndexDto> getIndexList(PrescriptionCriteria criteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PrescriptionIndexDto> cq = cb.createQuery(PrescriptionIndexDto.class);
		Root<Prescription> prescription = cq.from(Prescription.class);
		PrescriptionJoins joins = new PrescriptionJoins(prescription);

		cq.multiselect(
			prescription.get(Prescription.UUID),
			prescription.get(Prescription.PRESCRIPTION_TYPE),
			prescription.get(Prescription.PRESCRIPTION_DETAILS),
			prescription.get(Prescription.TYPE_OF_DRUG),
			prescription.get(Prescription.PRESCRIPTION_DATE),
			prescription.get(Prescription.PRESCRIPTION_START),
			prescription.get(Prescription.PRESCRIPTION_END),
			prescription.get(Prescription.FREQUENCY),
			prescription.get(Prescription.DOSE),
			prescription.get(Prescription.ROUTE),
			prescription.get(Prescription.ROUTE_DETAILS),
			prescription.get(Prescription.PRESCRIBING_CLINICIAN),
			JurisdictionHelper.booleanSelector(cb, caseService.inJurisdictionOrOwned(new CaseQueryContext(cb, cq, joins.getCaseJoins()))));

		if (criteria != null) {
			cq.where(service.buildCriteriaFilter(criteria, cb, prescription));
		}

		cq.orderBy(cb.desc(prescription.get(Prescription.PRESCRIPTION_DATE)));

		List<PrescriptionIndexDto> indexList = em.createQuery(cq).getResultList();

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		pseudonymizer.pseudonymizeDtoCollection(PrescriptionIndexDto.class, indexList, p -> p.getInJurisdiction(), (p, inJurisdiction) -> {
			pseudonymizer.pseudonymizeDto(PrescriptionIndexDto.PrescriptionIndexType.class, p.getPrescriptionIndexType(), inJurisdiction, null);
			pseudonymizer.pseudonymizeDto(PrescriptionIndexDto.PrescriptionIndexRoute.class, p.getPrescriptionIndexRoute(), inJurisdiction, null);
		});

		return indexList;
	}

	@Override
	public PrescriptionDto getPrescriptionByUuid(String uuid) {
		return convertToDto(service.getByUuid(uuid), Pseudonymizer.getDefault(userService::hasRight));
	}

	@Override
	@RolesAllowed({UserRight._PRESCRIPTION_CREATE, UserRight._PRESCRIPTION_EDIT})
	public PrescriptionDto savePrescription(@Valid PrescriptionDto prescription) {
		Prescription existingPrescription = service.getByUuid(prescription.getUuid());
		PrescriptionDto existingPrescriptionDto = toDto(existingPrescription);

		restorePseudonymizedDto(prescription, existingPrescription, existingPrescriptionDto);

		Prescription entity = fromDto(prescription, existingPrescription, true);

		service.ensurePersisted(entity);

		return convertToDto(entity, Pseudonymizer.getDefault(userService::hasRight));
	}

	@Override
	@RolesAllowed(UserRight._PRESCRIPTION_DELETE)
	public void deletePrescription(String prescriptionUuid) {
		Prescription prescription = service.getByUuid(prescriptionUuid);
		service.deletePermanent(prescription);
	}

	@Override
	public List<PrescriptionDto> getAllActivePrescriptionsAfter(Date date) {
		return getAllActivePrescriptionsAfter(date, null, null);
	}

	@Override
	public List<PrescriptionDto> getAllActivePrescriptionsAfter(Date date, Integer batchSize, String lastSynchronizedUuid) {

		User user = userService.getCurrentUser();
		if (user == null) {
			return Collections.emptyList();
		}

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		return service.getAllActivePrescriptionsAfter(date, user, batchSize, lastSynchronizedUuid)
			.stream()
			.map(p -> convertToDto(p, pseudonymizer))
			.collect(Collectors.toList());
	}

	@Override
	public List<PrescriptionDto> getByUuids(List<String> uuids) {
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);

		return service.getByUuids(uuids).stream().map(p -> {
			return convertToDto(p, pseudonymizer);
		}).collect(Collectors.toList());
	}

	@Override
	public List<String> getAllActiveUuids() {

		User user = userService.getCurrentUser();
		if (user == null) {
			return Collections.emptyList();
		}

		return service.getAllActiveUuids(user);
	}

	@Override
	public List<PrescriptionExportDto> getExportList(CaseCriteria criteria, Collection<String> selectedRows, int first, int max) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PrescriptionExportDto> cq = cb.createQuery(PrescriptionExportDto.class);
		Root<Prescription> prescription = cq.from(Prescription.class);

		PrescriptionJoins joins = new PrescriptionJoins(prescription);

		CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, joins.getCaseJoins());
		cq.multiselect(
			joins.getCaze().get(Case.UUID),
			joins.getCasePerson().get(Person.FIRST_NAME),
			joins.getCasePerson().get(Person.LAST_NAME),
			prescription.get(Prescription.PRESCRIPTION_DATE),
			prescription.get(Prescription.PRESCRIPTION_START),
			prescription.get(Prescription.PRESCRIPTION_END),
			prescription.get(Prescription.PRESCRIBING_CLINICIAN),
			prescription.get(Prescription.PRESCRIPTION_TYPE),
			prescription.get(Prescription.PRESCRIPTION_DETAILS),
			prescription.get(Prescription.TYPE_OF_DRUG),
			prescription.get(Prescription.FREQUENCY),
			prescription.get(Prescription.DOSE),
			prescription.get(Prescription.ROUTE),
			prescription.get(Prescription.ROUTE_DETAILS),
			prescription.get(Prescription.ADDITIONAL_NOTES),
			JurisdictionHelper.booleanSelector(cb, caseService.inJurisdictionOrOwned(caseQueryContext)));

		Predicate filter = service.createUserFilter(cb, cq, prescription);
		Predicate criteriaFilter = caseService.createCriteriaFilter(criteria, caseQueryContext);
		filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		filter = CriteriaBuilderHelper.andInValues(selectedRows, filter, cb, joins.getCaze().get(Case.UUID));
		cq.where(filter);
		cq.orderBy(cb.desc(joins.getCaze().get(Case.UUID)), cb.desc(prescription.get(Prescription.PRESCRIPTION_DATE)));

		List<PrescriptionExportDto> exportList = QueryHelper.getResultList(em, cq, first, max);

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		pseudonymizer.pseudonymizeDtoCollection(PrescriptionExportDto.class, exportList, p -> p.getInJurisdiction(), null);

		return exportList;
	}

	private PrescriptionDto convertToDto(Prescription source, Pseudonymizer pseudonymizer) {
		PrescriptionDto dto = toDto(source);

		pseudonymizeDto(source, dto, pseudonymizer);

		return dto;
	}

	private void pseudonymizeDto(Prescription source, PrescriptionDto dto, Pseudonymizer pseudonymizer) {
		if (source != null && dto != null) {
			pseudonymizer.pseudonymizeDto(PrescriptionDto.class, dto, caseService.inJurisdictionOrOwned(source.getTherapy().getCaze()), null);
		}
	}

	private void restorePseudonymizedDto(PrescriptionDto prescription, Prescription existingPrescription, PrescriptionDto existingPrescriptionDto) {
		if (existingPrescription != null) {
			Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
			pseudonymizer.restorePseudonymizedValues(
				PrescriptionDto.class,
				prescription,
				existingPrescriptionDto,
				caseService.inJurisdictionOrOwned(existingPrescription.getTherapy().getCaze()));
		}
	}

	public static PrescriptionDto toDto(Prescription source) {

		if (source == null) {
			return null;
		}

		PrescriptionDto target = new PrescriptionDto();
		DtoHelper.fillDto(target, source);

		target.setTherapy(TherapyFacadeEjb.toReferenceDto(source.getTherapy()));
		target.setPrescriptionType(source.getPrescriptionType());
		target.setPrescriptionDetails(source.getPrescriptionDetails());
		target.setTypeOfDrug(source.getTypeOfDrug());
		target.setPrescriptionDate(source.getPrescriptionDate());
		target.setPrescribingClinician(source.getPrescribingClinician());
		target.setPrescriptionStart(source.getPrescriptionStart());
		target.setPrescriptionEnd(source.getPrescriptionEnd());
		target.setFrequency(source.getFrequency());
		target.setDose(source.getDose());
		target.setRoute(source.getRoute());
		target.setRouteDetails(source.getRouteDetails());
		target.setAdditionalNotes(source.getAdditionalNotes());

		return target;
	}

	public static PrescriptionReferenceDto toReferenceDto(Prescription source) {
		if (source == null) {
			return null;
		}

		PrescriptionReferenceDto reference = new PrescriptionReferenceDto(source.getUuid(), source.toString());
		return reference;
	}

	public Prescription fromDto(@NotNull PrescriptionDto source, Prescription target, boolean checkChangeDate) {

		target = DtoHelper.fillOrBuildEntity(source, target, Prescription::new, checkChangeDate);

		target.setTherapy(therapyService.getByReferenceDto(source.getTherapy()));
		target.setPrescriptionType(source.getPrescriptionType());
		target.setPrescriptionDetails(source.getPrescriptionDetails());
		target.setTypeOfDrug(source.getTypeOfDrug());
		target.setPrescriptionDate(source.getPrescriptionDate());
		target.setPrescribingClinician(source.getPrescribingClinician());
		target.setPrescriptionStart(source.getPrescriptionStart());
		target.setPrescriptionEnd(source.getPrescriptionEnd());
		target.setFrequency(source.getFrequency());
		target.setDose(source.getDose());
		target.setRoute(source.getRoute());
		target.setRouteDetails(source.getRouteDetails());
		target.setAdditionalNotes(source.getAdditionalNotes());

		return target;
	}

	@LocalBean
	@Stateless
	public static class PrescriptionFacadeEjbLocal extends PrescriptionFacadeEjb {

	}
}
