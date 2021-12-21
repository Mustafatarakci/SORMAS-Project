package de.symeda.sormas.backend.hospitalization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.hospitalization.HospitalizationFacade;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionFacadeEjb;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.util.DtoHelper;

@Stateless(name = "HospitalizationFacade")
public class HospitalizationFacadeEjb implements HospitalizationFacade {

	@EJB
	private HospitalizationService service;
	@EJB
	private PreviousHospitalizationService prevHospService;
	@EJB
	private CaseService caseService;
	@EJB
	private RegionService regionService;
	@EJB
	private DistrictService districtService;
	@EJB
	private CommunityService communityService;
	@EJB
	private FacilityService facilityService;

	public Hospitalization fromDto(HospitalizationDto source, boolean checkChangeDate) {

		if (source == null) {
			return null;
		}

		Hospitalization target = DtoHelper.fillOrBuildEntity(source, service.getByUuid(source.getUuid()), Hospitalization::new, checkChangeDate);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setHospitalizedPreviously(source.getHospitalizedPreviously());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setLeftAgainstAdvice(source.getLeftAgainstAdvice());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());

		List<PreviousHospitalization> previousHospitalizations = new ArrayList<>();
		for (PreviousHospitalizationDto prevDto : source.getPreviousHospitalizations()) {
			PreviousHospitalization prevHosp = fromDto(prevDto, checkChangeDate);
			prevHosp.setHospitalization(target);
			previousHospitalizations.add(prevHosp);
		}
		if (!DataHelper.equal(target.getPreviousHospitalizations(), previousHospitalizations)) {
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getPreviousHospitalizations().clear();
		target.getPreviousHospitalizations().addAll(previousHospitalizations);
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setDescription(source.getDescription());

		return target;
	}

	public PreviousHospitalization fromDto(PreviousHospitalizationDto source, boolean checkChangeDate) {

		if (source == null) {
			return null;
		}

		PreviousHospitalization target =
			DtoHelper.fillOrBuildEntity(source, prevHospService.getByUuid(source.getUuid()), PreviousHospitalization::new, checkChangeDate);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setRegion(regionService.getByReferenceDto(source.getRegion()));
		target.setDistrict(districtService.getByReferenceDto(source.getDistrict()));
		target.setCommunity(communityService.getByReferenceDto(source.getCommunity()));
		target.setHealthFacility(facilityService.getByReferenceDto(source.getHealthFacility()));
		target.setHealthFacilityDetails(source.getHealthFacilityDetails());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setDescription(source.getDescription());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());

		return target;
	}

	public static HospitalizationDto toDto(Hospitalization hospitalization) {

		if (hospitalization == null) {
			return null;
		}

		HospitalizationDto target = new HospitalizationDto();
		Hospitalization source = hospitalization;

		DtoHelper.fillDto(target, source);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setHospitalizedPreviously(source.getHospitalizedPreviously());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setLeftAgainstAdvice(source.getLeftAgainstAdvice());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());

		List<PreviousHospitalizationDto> previousHospitalizations = new ArrayList<>();
		for (PreviousHospitalization prevDto : source.getPreviousHospitalizations()) {
			PreviousHospitalizationDto prevHosp = toDto(prevDto);
			previousHospitalizations.add(prevHosp);
		}
		target.setPreviousHospitalizations(previousHospitalizations);
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setDescription(source.getDescription());

		return target;
	}

	public static PreviousHospitalizationDto toDto(PreviousHospitalization source) {

		if (source == null) {
			return null;
		}

		PreviousHospitalizationDto target = new PreviousHospitalizationDto();

		DtoHelper.fillDto(target, source);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setRegion(RegionFacadeEjb.toReferenceDto(source.getRegion()));
		target.setDistrict(DistrictFacadeEjb.toReferenceDto(source.getDistrict()));
		target.setCommunity(CommunityFacadeEjb.toReferenceDto(source.getCommunity()));
		target.setHealthFacility(FacilityFacadeEjb.toReferenceDto(source.getHealthFacility()));
		target.setHealthFacilityDetails(source.getHealthFacilityDetails());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setDescription(source.getDescription());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());

		return target;
	}

	@LocalBean
	@Stateless
	public static class HospitalizationFacadeEjbLocal extends HospitalizationFacadeEjb {

	}
}
