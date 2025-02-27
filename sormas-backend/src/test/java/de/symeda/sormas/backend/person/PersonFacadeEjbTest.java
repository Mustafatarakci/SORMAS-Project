package de.symeda.sormas.backend.person;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.InvestigationStatus;
import de.symeda.sormas.api.clinicalcourse.HealthConditionsDto;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.FollowUpStatus;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolRuntimeException;
import de.symeda.sormas.api.immunization.ImmunizationDto;
import de.symeda.sormas.api.immunization.ImmunizationManagementStatus;
import de.symeda.sormas.api.immunization.ImmunizationStatus;
import de.symeda.sormas.api.immunization.MeansOfImmunization;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.JournalPersonDto;
import de.symeda.sormas.api.person.PersonAssociation;
import de.symeda.sormas.api.person.PersonContactDetailDto;
import de.symeda.sormas.api.person.PersonContactDetailType;
import de.symeda.sormas.api.person.PersonContext;
import de.symeda.sormas.api.person.PersonCriteria;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonExportDto;
import de.symeda.sormas.api.person.PersonFacade;
import de.symeda.sormas.api.person.PersonFollowUpEndDto;
import de.symeda.sormas.api.person.PersonIndexDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.person.PersonSimilarityCriteria;
import de.symeda.sormas.api.person.PhoneNumberType;
import de.symeda.sormas.api.person.PresentCondition;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.person.SymptomJournalStatus;
import de.symeda.sormas.api.user.DefaultUserRole;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.vaccination.VaccinationDto;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.MockProducer;
import de.symeda.sormas.backend.TestDataCreator;

public class PersonFacadeEjbTest extends AbstractBeanTest {

	protected TestDataCreator.RDCF rdcf;
	protected TestDataCreator.RDCFEntities rdcfEntities;
	protected UserDto nationalUser;

	/**
	 * Resets mocks to their initial state so that mock configurations are not
	 * shared between tests.
	 */

	@Override
	public void init() {
		super.init();

		rdcf = creator.createRDCF("Region 1", "District 1", "Community 1", "Facility 1", "Point of entry 1");
		rdcfEntities = creator.createRDCFEntities();
		nationalUser = creator.createUser(
			rdcf.region.getUuid(),
			rdcf.district.getUuid(),
			rdcf.community.getUuid(),
			rdcf.facility.getUuid(),
			"Nat",
			"User",
			creator.getUserRoleReference(DefaultUserRole.NATIONAL_USER));
	}

	/**
	 * Test all {@link PersonAssociation} variants if they work. Also serves to review the generated SQL.
	 */
	@Test
	public void testCountAndGetIndexListWithAssociations() {

		PersonFacade cut = getPersonFacade();
		Integer offset = null;
		Integer limit = null;
		List<SortProperty> sortProperties = null;

		UserDto user = creator.createUser(
			rdcf.region.getUuid(),
			null,
			null,
			null,
			"Surv",
			"Sup",
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		loginWith(user);

		// 1a. Test for all available PersonAssociations
		for (PersonAssociation pa : PersonAssociation.values()) {
			PersonCriteria criteria = new PersonCriteria().personAssociation(pa);
			assertThat("Failed for testing association on count: " + pa.name(), cut.count(criteria), equalTo(0L));
			assertThat(criteria.getPersonAssociation(), equalTo(pa));
			assertThat(
				"Failed for testing association on getIndexList: " + pa.name(),
				cut.getIndexList(criteria, offset, limit, sortProperties),
				is(empty()));
			assertThat(criteria.getPersonAssociation(), equalTo(pa));
		}

		// 1b. Test that calling with "null" as criteria also works
		assertThat(cut.count(null), equalTo(0L));
		assertThat(cut.getIndexList(null, offset, limit, sortProperties), is(empty()));

		// 2. Test paging windows
		final PersonDto person1 = creator.createPerson("James", "Smith", Sex.MALE, 1920, 1, 1);
		final CaseDataDto case1 = creator.createCase(
			user.toReference(),
			person1.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcf);
		final PersonDto person2 = creator.createPerson("Maria", "Garcia", Sex.FEMALE, 1920, 1, 1);
		final ContactDto contact2 =
			creator.createContact(user.toReference(), user.toReference(), person2.toReference(), null, new Date(), new Date(), Disease.EVD, rdcf);

		// 2a. count
		assertThat(cut.count(new PersonCriteria().personAssociation(PersonAssociation.ALL)), equalTo(2L));
		assertThat(cut.count(new PersonCriteria().personAssociation(PersonAssociation.CASE)), equalTo(1L));
		assertThat(cut.count(new PersonCriteria().personAssociation(PersonAssociation.CONTACT)), equalTo(1L));

		// 2b. getIndexList with all persons in the paging window
		assertPersonsFound(case1, contact2, cut, offset, limit, sortProperties);
		offset = 0;
		limit = 2;
		assertPersonsFound(case1, contact2, cut, offset, limit, sortProperties);
		offset = 0;
		limit = 1;
		assertPersonsFound(case1, contact2, cut, offset, limit, sortProperties);

		// 2c. getIndexList [PersonAssociation.ALL] with only the contact person in the paging window (default sorting by changeDate)
		offset = 1;
		limit = 2;
		assertPersonsFound(null, null, Arrays.asList(contact2.getPerson()), cut, offset, limit, sortProperties);
	}

	private static void assertPersonsFound(
		CaseDataDto caze,
		ContactDto contact,
		PersonFacade cut,
		Integer offset,
		Integer limit,
		List<SortProperty> sortProperties) {

		assertPersonsFound(caze, contact, Arrays.asList(caze.getPerson(), contact.getPerson()), cut, offset, limit, sortProperties);
	}

	private static void assertPersonsFound(
		CaseDataDto caze,
		ContactDto contact,
		List<PersonReferenceDto> allPersons,
		PersonFacade cut,
		Integer offset,
		Integer limit,
		List<SortProperty> sortProperties) {

		List<String> casePersonUuids = caze == null ? Collections.emptyList() : Collections.singletonList(caze.getPerson().getUuid());
		List<String> contactPersonUuids = contact == null ? Collections.emptyList() : Collections.singletonList(contact.getPerson().getUuid());
		int allCount = Math.min(allPersons.size(), limit != null ? limit : Integer.MAX_VALUE);

		// single association
		List<PersonIndexDto> caseList =
			cut.getIndexList(new PersonCriteria().personAssociation(PersonAssociation.CASE), offset, limit, sortProperties);
		assertThat(caseList, hasSize(casePersonUuids.size()));
		List<PersonIndexDto> contactList =
			cut.getIndexList(new PersonCriteria().personAssociation(PersonAssociation.CONTACT), offset, limit, sortProperties);
		assertThat(contactList, hasSize(contactPersonUuids.size()));
		assertThat(
			cut.getIndexList(new PersonCriteria().personAssociation(PersonAssociation.EVENT_PARTICIPANT), offset, limit, sortProperties),
			is(empty()));
		assertThat(
			cut.getIndexList(new PersonCriteria().personAssociation(PersonAssociation.IMMUNIZATION), offset, limit, sortProperties),
			is(empty()));
		assertThat(
			cut.getIndexList(new PersonCriteria().personAssociation(PersonAssociation.TRAVEL_ENTRY), offset, limit, sortProperties),
			is(empty()));

		// ALL
		List<PersonIndexDto> allList = cut.getIndexList(new PersonCriteria().personAssociation(PersonAssociation.ALL), offset, limit, sortProperties);
		assertThat(allList, hasSize(allCount));
	}

	@Test
	public void testGetIndexListByPresentCondition() {
		final UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		final PersonDto person1 = creator.createPerson("James", "Smith", Sex.MALE, 1920, 1, 1);
		creator.createCase(
			user.toReference(),
			person1.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcfEntities);
		person1.setPresentCondition(PresentCondition.DEAD);
		final PersonDto person2 = creator.createPerson("Maria", "Garcia", Sex.FEMALE, 1920, 1, 1);
		creator.createCase(
			user.toReference(),
			person2.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcfEntities);
		getPersonFacade().savePerson(person1);

		assertEquals(1, getPersonFacade().getIndexList(new PersonCriteria().presentCondition(PresentCondition.DEAD), null, null, null).size());
		assertEquals(2, getPersonFacade().getIndexList(new PersonCriteria(), null, null, null).size());
	}

	@Test
	public void testGetIndexListByName() {
		final UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		user.setRegion(new RegionReferenceDto(rdcfEntities.region.getUuid()));
		user.setLimitedDisease(Disease.EVD);
		getUserFacade().saveUser(user);
		loginWith(user);

		final PersonDto person1 = creator.createPerson("James", "Smith", Sex.MALE, 1920, 1, 1);
		creator.createCase(
			user.toReference(),
			person1.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcfEntities);
		person1.setPresentCondition(PresentCondition.DEAD);
		final PersonDto person2 = creator.createPerson("Maria", "Garcia", Sex.FEMALE, 1920, 1, 1);
		creator.createCase(
			user.toReference(),
			person2.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcfEntities);
		getPersonFacade().savePerson(person1);

		PersonCriteria criteria = new PersonCriteria();
		criteria.setNameAddressPhoneEmailLike("James");
		assertEquals(1, getPersonFacade().getIndexList(criteria, null, null, null).size());
		assertEquals(2, getPersonFacade().getIndexList(new PersonCriteria(), null, null, null).size());
	}

	@Test
	public void testGetIndexListPersonNotConsideredIfAssociatedEntitiesDeleted() throws ExternalSurveillanceToolRuntimeException {
		final UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));

		final PersonDto person1 = creator.createPerson("James", "Smith", Sex.MALE, 1920, 1, 1);
		person1.setPresentCondition(PresentCondition.DEAD);
		creator.createPerson("Maria", "Garcia", Sex.FEMALE, 1920, 1, 1);

		CaseDataDto caze = creator.createCase(
			user.toReference(),
			person1.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcfEntities);
		CaseDataDto caze2 = creator.createCase(
			user.toReference(),
			person1.toReference(),
			Disease.EVD,
			CaseClassification.CONFIRMED_UNKNOWN_SYMPTOMS,
			InvestigationStatus.PENDING,
			new Date(),
			rdcfEntities);
		assertEquals(1, getPersonFacade().getIndexList(new PersonCriteria(), null, null, null).size());

		getCaseFacade().delete(caze.getUuid(), new DeletionDetails(DeletionReason.OTHER_REASON, "test reason"));

		assertEquals(1, getPersonFacade().getIndexList(new PersonCriteria(), null, null, null).size());

		getCaseFacade().delete(caze2.getUuid(), new DeletionDetails(DeletionReason.OTHER_REASON, "test reason"));

		assertEquals(0, getPersonFacade().getIndexList(new PersonCriteria(), null, null, null).size());

		ContactDto contact = creator.createContact(user.toReference(), user.toReference(), person1.toReference(), caze, new Date(), new Date(), null);

		assertEquals(1, getPersonFacade().getIndexList(new PersonCriteria(), null, null, null).size());

		getContactFacade().delete(contact.getUuid(), new DeletionDetails(DeletionReason.OTHER_REASON, "test reason"));

		assertEquals(0, getPersonFacade().getIndexList(new PersonCriteria(), null, null, null).size());
	}

	@Test
	public void testGetMatchingNameDtos() {
		UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));

		// 1-3 = Active persons; 4 = Person without reference; 5-7 = Inactive persons
		PersonDto person1 = creator.createPerson("James", "Smith", Sex.MALE, 1980, 1, 1);
		PersonDto person2 = creator.createPerson("James", "Smith", Sex.MALE, 1979, 5, 12);
		PersonDto person3 = creator.createPerson("James", "Smith", Sex.MALE, 1980, 1, 5);
		PersonDto person4 = creator.createPerson("Maria", "Garcia", Sex.FEMALE, 1984, 12, 2);
		PersonDto person5 = creator.createPerson("Maria", "Garcia", Sex.UNKNOWN, 1984, 7, 12);
		PersonDto person6 = creator.createPerson("Maria", "Garcia", Sex.FEMALE, 1984, null, null);
		PersonDto person7 = creator.createPerson("James", "Smith", Sex.MALE, null, null, null);

		CaseDataDto activeCase = creator.createCase(user.toReference(), person1.toReference(), rdcf);
		creator.createContact(user.toReference(), person2.toReference(), activeCase);
		EventDto activeEvent = creator.createEvent(user.toReference());
		creator.createEventParticipant(activeEvent.toReference(), person3, user.toReference());

		CaseDataDto inactiveCase = creator.createCase(user.toReference(), person5.toReference(), rdcf);
		creator.createContact(user.toReference(), person6.toReference(), inactiveCase);
		EventDto inactiveEvent = creator.createEvent(user.toReference());
		creator.createEventParticipant(inactiveEvent.toReference(), person7, user.toReference());

		getCaseFacade().archive(inactiveCase.getUuid(), null);
		getEventFacade().archive(inactiveEvent.getUuid(), null);

		// Only persons that have active case, contact or event participant associations should be retrieved
		List<String> relevantNameUuids =
			getPersonFacade().getSimilarPersonDtos(new PersonSimilarityCriteria()).stream().map(dto -> dto.getUuid()).collect(Collectors.toList());
		assertThat(relevantNameUuids, hasSize(6));
		assertThat(
			relevantNameUuids,
			containsInAnyOrder(person1.getUuid(), person2.getUuid(), person3.getUuid(), person5.getUuid(), person6.getUuid(), person7.getUuid()));

		creator.createCase(user.toReference(), person4.toReference(), rdcf);
		getCaseFacade().dearchive(Collections.singletonList(inactiveCase.getUuid()), null);
		getEventFacade().dearchive(Collections.singletonList(inactiveEvent.getUuid()), null);

		PersonSimilarityCriteria criteria = new PersonSimilarityCriteria().sex(Sex.MALE).birthdateYYYY(1980).birthdateMM(1).birthdateDD(1);
		List<String> matchingUuids =
			getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(2));
		assertThat(matchingUuids, containsInAnyOrder(person1.getUuid(), person7.getUuid()));

		criteria.birthdateMM(null).birthdateDD(null);
		matchingUuids = getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(3));
		assertThat(matchingUuids, containsInAnyOrder(person1.getUuid(), person3.getUuid(), person7.getUuid()));

		criteria.sex(Sex.FEMALE).birthdateYYYY(1984);
		matchingUuids = getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(3));
		assertThat(matchingUuids, containsInAnyOrder(person4.getUuid(), person5.getUuid(), person6.getUuid()));

		criteria.sex(null);
		matchingUuids = getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(4));
		assertThat(matchingUuids, containsInAnyOrder(person4.getUuid(), person5.getUuid(), person6.getUuid(), person7.getUuid()));

		final String passportNr = "passportNr";
		final String otherPassportNr = "otherPassportNr";
		final String healthId = "healthId";
		final String otherHealthId = "otherHealthId";
		PersonDto person8 = creator.createPerson("James", "Smith", Sex.MALE, 1980, 1, 1, passportNr, healthId);
		PersonDto person9 = creator.createPerson("James", "Smith", Sex.MALE, 1980, 1, 1, null, otherHealthId);
		PersonDto person10 = creator.createPerson("Maria", "Garcia", Sex.FEMALE, 1970, 1, 1, passportNr, null);
		PersonDto person11 = creator.createPerson("John", "Doe", Sex.MALE, 1970, 1, 1, otherPassportNr, null);
		creator.createCase(user.toReference(), person8.toReference(), rdcf);
		creator.createCase(user.toReference(), person9.toReference(), rdcf);
		creator.createCase(user.toReference(), person10.toReference(), rdcf);
		creator.createCase(user.toReference(), person11.toReference(), rdcf);

		criteria.sex(Sex.MALE).birthdateYYYY(1980);
		criteria.passportNumber(passportNr);
		matchingUuids = getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(6));
		assertThat(
			matchingUuids,
			containsInAnyOrder(person1.getUuid(), person3.getUuid(), person7.getUuid(), person8.getUuid(), person9.getUuid(), person10.getUuid()));

		criteria.nationalHealthId(healthId).passportNumber(null);
		matchingUuids = getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(4));
		assertThat(matchingUuids, containsInAnyOrder(person1.getUuid(), person3.getUuid(), person7.getUuid(), person8.getUuid()));

		criteria.nationalHealthId(otherHealthId);
		matchingUuids = getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(4));
		assertThat(matchingUuids, containsInAnyOrder(person1.getUuid(), person3.getUuid(), person7.getUuid(), person9.getUuid()));

		criteria.passportNumber(otherPassportNr);
		matchingUuids = getPersonFacade().getSimilarPersonDtos(criteria).stream().map(person -> person.getUuid()).collect(Collectors.toList());
		assertThat(matchingUuids, hasSize(5));
		assertThat(matchingUuids, containsInAnyOrder(person1.getUuid(), person3.getUuid(), person7.getUuid(), person9.getUuid(), person11.getUuid()));
	}

	@Test
	/*
	 * If you need to change this test to make it pass, you probably changed the behaviour of the ExternalVisitsResource.
	 * Please note that other system used alongside with SORMAS are depending on this, so that their developers must be notified of any
	 * relevant API changes some time before they go into any test and productive system. Please inform the SORMAS core development team at
	 * https://gitter.im/SORMAS-Project!
	 */
	public void testIsValidPersonUuid() {
		final PersonDto person = creator.createPerson("James", "Smith", Sex.MALE, 1980, 1, 1);
		assertTrue(getPersonFacade().isValidPersonUuid(person.getUuid()));
		assertFalse(getPersonFacade().isValidPersonUuid("2341235234534"));
	}

	@Test
	/*
	 * If you need to change this test to make it pass, you probably changed the behaviour of the ExternalVisitsResource.
	 * Please note that other system used alongside with SORMAS are depending on this, so that their developers must be notified of any
	 * relevant API changes some time before they go into any test and productive system. Please inform the SORMAS core development team at
	 * https://gitter.im/SORMAS-Project!
	 */
	public void testGetFollowUpEndDatesContactsOnly() {
		UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.REST_EXTERNAL_VISITS_USER));

		creator.createPerson(); // Person without contact
		final PersonDto person1 = creator.createPerson();
		final PersonDto person2 = creator.createPerson();
		final ContactDto contact11 = creator.createContact(user.toReference(), person1.toReference(), DateHelper.subtractDays(new Date(), 41));
		final ContactDto contact12 = creator.createContact(user.toReference(), person1.toReference(), DateHelper.subtractDays(new Date(), 29));
		final ContactDto contact2 = creator.createContact(user.toReference(), person2.toReference(), DateHelper.subtractDays(new Date(), 21));

		contact11.setOverwriteFollowUpUntil(true);
		contact12.setOverwriteFollowUpUntil(true);
		contact2.setOverwriteFollowUpUntil(true);

		Date now = new Date();
		contact11.setFollowUpUntil(DateHelper.subtractDays(now, 20));
		contact12.setFollowUpUntil(DateHelper.subtractDays(now, 8));
		contact2.setFollowUpUntil(now);

		getContactFacade().save(contact11);
		getContactFacade().save(contact12);
		getContactFacade().save(contact2);

		List<PersonFollowUpEndDto> followUpEndDtos = getPersonFacade().getLatestFollowUpEndDates(null, false);

		assertThat(followUpEndDtos, hasSize(2));
		Optional<PersonFollowUpEndDto> result1 = followUpEndDtos.stream().filter(p -> p.getPersonUuid().equals(person1.getUuid())).findFirst();
		assertTrue(result1.isPresent());
		assertTrue(DateHelper.isSameDay(result1.get().getLatestFollowUpEndDate(), DateHelper.subtractDays(now, 8)));
		Optional<PersonFollowUpEndDto> result2 = followUpEndDtos.stream().filter(p -> p.getPersonUuid().equals(person2.getUuid())).findFirst();
		assertTrue(result2.isPresent());
		assertTrue(DateHelper.isSameDay(result2.get().getLatestFollowUpEndDate(), now));
		Date result3 = getPersonFacade().getLatestFollowUpEndDateByUuid(person1.getUuid());
		assertTrue(DateHelper.isSameDay(result3, DateHelper.subtractDays(now, 8)));
	}

	@Test
	/*
	 * If you need to change this test to make it pass, you probably changed the behaviour of the ExternalVisitsResource.
	 * Please note that other system used alongside with SORMAS are depending on this, so that their developers must be notified of any
	 * relevant API changes some time before they go into any test and productive system. Please inform the SORMAS core development team at
	 * https://gitter.im/SORMAS-Project!
	 */
	public void testGetPersonForJournal() {
		UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.CONTACT_SUPERVISOR));

		String phoneNumber = "+496211218490";
		String internationalPhoneNumber = "+49 621 1218490";

		final PersonDto person = creator.createPerson();
		person.setFirstName("Klaus");
		person.setLastName("Draufle");
		person.setSex(Sex.MALE);
		person.setEmailAddress("test@test.de");
		person.setPhone(phoneNumber);
		person.setBirthdateYYYY(2000);
		person.setBirthdateMM(6);
		person.setBirthdateDD(1);
		person.setSymptomJournalStatus(SymptomJournalStatus.REGISTERED);
		final ContactDto contact1 = creator.createContact(user.toReference(), person.toReference(), DateHelper.subtractDays(new Date(), 41));
		final ContactDto contact2 = creator.createContact(user.toReference(), person.toReference(), DateHelper.subtractDays(new Date(), 29));
		contact1.setOverwriteFollowUpUntil(true);
		contact2.setOverwriteFollowUpUntil(true);

		Date now = new Date();
		contact1.setFollowUpUntil(DateHelper.subtractDays(now, 20));
		contact2.setFollowUpUntil(DateHelper.subtractDays(now, 8));

		getPersonFacade().savePerson(person);
		getContactFacade().save(contact1);
		getContactFacade().save(contact2);

		JournalPersonDto exportPerson = getPersonFacade().getPersonForJournal(person.getUuid());
		assertEquals(person.getFirstName(), exportPerson.getFirstName());
		assertEquals(person.getLastName(), exportPerson.getLastName());
		assertEquals(person.getSex(), exportPerson.getSex());
		assertEquals(person.getEmailAddress(), exportPerson.getEmailAddress());
		assertEquals(internationalPhoneNumber, exportPerson.getPhone());
		assertEquals(person.getBirthdateYYYY(), exportPerson.getBirthdateYYYY());
		assertEquals(person.getBirthdateMM(), exportPerson.getBirthdateMM());
		assertEquals(person.getBirthdateDD(), exportPerson.getBirthdateDD());
		assertEquals(contact2.getFollowUpUntil(), exportPerson.getLatestFollowUpEndDate());
	}

	@Test
	public void testGetFollowUpEndDatesCasesOnly() {
		UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.REST_EXTERNAL_VISITS_USER));

		creator.createPerson(); // Person without contact
		final PersonDto person1 = creator.createPerson();
		final PersonDto person2 = creator.createPerson();
		final CaseDataDto case11 = creator.createCase(user.toReference(), person1.toReference(), rdcfEntities);
		final CaseDataDto case12 = creator.createCase(user.toReference(), person1.toReference(), rdcfEntities);
		final CaseDataDto case2 = creator.createCase(user.toReference(), person2.toReference(), rdcfEntities);

		Date now = new Date();
		case11.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 41));
		case11.setOverwriteFollowUpUntil(true);
		case12.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 29));
		case12.setOverwriteFollowUpUntil(true);
		case2.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 21));
		case2.setOverwriteFollowUpUntil(true);

		case11.setFollowUpUntil(DateHelper.subtractDays(now, 20));
		case12.setFollowUpUntil(DateHelper.subtractDays(now, 8));
		case2.setFollowUpUntil(now);

		getCaseFacade().save(case11);
		getCaseFacade().save(case12);
		getCaseFacade().save(case2);

		List<PersonFollowUpEndDto> followUpEndDtos = getPersonFacade().getLatestFollowUpEndDates(null, false);

		assertThat(followUpEndDtos, hasSize(2));
		Optional<PersonFollowUpEndDto> result1 = followUpEndDtos.stream().filter(p -> p.getPersonUuid().equals(person1.getUuid())).findFirst();
		assertTrue(result1.isPresent());
		assertTrue(DateHelper.isSameDay(result1.get().getLatestFollowUpEndDate(), DateHelper.subtractDays(now, 8)));
		Optional<PersonFollowUpEndDto> result2 = followUpEndDtos.stream().filter(p -> p.getPersonUuid().equals(person2.getUuid())).findFirst();
		assertTrue(result2.isPresent());
		assertTrue(DateHelper.isSameDay(result2.get().getLatestFollowUpEndDate(), now));
		Date result3 = getPersonFacade().getLatestFollowUpEndDateByUuid(person1.getUuid());
		assertTrue(DateHelper.isSameDay(result3, DateHelper.subtractDays(now, 8)));
	}

	@Test
	public void testGetFollowUpEndDatesContactsAndCases() {
		UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.REST_EXTERNAL_VISITS_USER));
		Date now = new Date();

		final PersonDto person1 = creator.createPerson();
		final PersonDto person2 = creator.createPerson();
		final PersonDto person3 = creator.createPerson();
		final PersonDto person4 = creator.createPerson();
		final ContactDto contact1 = creator.createContact(user.toReference(), person1.toReference(), DateHelper.subtractDays(now, 22));
		final ContactDto contact2 = creator.createContact(user.toReference(), person2.toReference(), DateHelper.subtractDays(now, 22));
		final ContactDto contact3 = creator.createContact(user.toReference(), person4.toReference());
		final ContactDto contact4 = creator.createContact(user.toReference(), person4.toReference(), DateHelper.subtractDays(now, 21));
		final CaseDataDto case1 = creator.createCase(user.toReference(), person1.toReference(), rdcfEntities);
		final CaseDataDto case2 = creator.createCase(user.toReference(), person2.toReference(), rdcfEntities);
		final CaseDataDto case3 = creator.createCase(user.toReference(), person2.toReference(), rdcfEntities);
		final CaseDataDto case4 = creator.createCase(user.toReference(), person3.toReference(), rdcfEntities);
		final CaseDataDto case5 = creator.createCase(user.toReference(), person3.toReference(), rdcfEntities);

		contact1.setOverwriteFollowUpUntil(true);
		contact2.setOverwriteFollowUpUntil(true);
		case1.setOverwriteFollowUpUntil(true);
		case1.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 23));
		case2.setOverwriteFollowUpUntil(true);
		case2.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 21));
		case3.setOverwriteFollowUpUntil(true);
		case3.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 23));
		case4.setOverwriteFollowUpUntil(true);
		case4.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 21));
		case5.setOverwriteFollowUpUntil(true);
		case5.getSymptoms().setOnsetDate(DateHelper.subtractDays(now, 21));
		contact3.setFollowUpStatus(FollowUpStatus.NO_FOLLOW_UP);
		contact3.setFollowUpUntil(null);
		contact4.setFollowUpStatus(FollowUpStatus.FOLLOW_UP);

		contact1.setFollowUpUntil(DateHelper.subtractDays(now, 1));
		case1.setFollowUpUntil(DateHelper.subtractDays(now, 2));
		contact2.setFollowUpUntil(DateHelper.subtractDays(now, 1));

		case2.setFollowUpUntil(now);
		case2.setFollowUpStatus(FollowUpStatus.CANCELED);
		case3.setFollowUpUntil(DateHelper.subtractDays(now, 2));

		case4.setFollowUpStatus(FollowUpStatus.CANCELED);
		case5.setFollowUpStatus(FollowUpStatus.CANCELED);
		contact4.setFollowUpUntil(now);

		getContactFacade().save(contact1);
		getContactFacade().save(contact2);
		getContactFacade().save(contact3);
		getContactFacade().save(contact4);
		getCaseFacade().save(case1);
		getCaseFacade().save(case2);
		getCaseFacade().save(case3);
		getCaseFacade().save(case4);
		getCaseFacade().save(case5);

		List<PersonFollowUpEndDto> followUpEndDtos = getPersonFacade().getLatestFollowUpEndDates(null, false);

		assertThat(followUpEndDtos, hasSize(4));

		Optional<PersonFollowUpEndDto> result1 = followUpEndDtos.stream().filter(p -> p.getPersonUuid().equals(person1.getUuid())).findFirst();
		assertTrue(result1.isPresent());
		assertTrue(DateHelper.isSameDay(result1.get().getLatestFollowUpEndDate(), DateHelper.subtractDays(now, 1)));

		Optional<PersonFollowUpEndDto> result2 = followUpEndDtos.stream().filter(p -> p.getPersonUuid().equals(person2.getUuid())).findFirst();
		assertTrue(result2.isPresent());
		assertNotNull(result2.get().getLatestFollowUpEndDate());
		assertTrue(DateHelper.isSameDay(result2.get().getLatestFollowUpEndDate(), DateHelper.subtractDays(now, 1)));

		Optional<PersonFollowUpEndDto> result3 = followUpEndDtos.stream().filter(p -> p.getPersonUuid().equals(person3.getUuid())).findFirst();
		assertTrue(result3.isPresent());
		assertNull(result3.get().getLatestFollowUpEndDate());
	}

	@Test
	public void testGetPersonsAfter() {

		int batchSize = 4;
		Date t1 = new Date();

		// 0. Make sure that empty result works
		assertThat(getPersonFacade().getPersonsAfter(t1), is(empty()));
		assertThat(getPersonFacade().getPersonsAfter(t1, batchSize, null), is(empty()));
		assertThat(getPersonFacade().getPersonsAfter(t1, batchSize, EntityDto.NO_LAST_SYNCED_UUID), is(empty()));

		// 1. Check one persons with two timestamps
		PersonDto person1 = creator.createPerson("First", "Person");
		creator.createContact(nationalUser.toReference(), person1.toReference());

		assertThat(getPersonFacade().getPersonsAfter(t1), contains(person1));
		assertThat(getPersonFacade().getPersonsAfter(t1, batchSize, null), contains(person1));
		assertThat(getPersonFacade().getPersonsAfter(t1, batchSize, EntityDto.NO_LAST_SYNCED_UUID), contains(person1));
		assertThat(getPersonFacade().getPersonsAfter(t1, batchSize, person1.getUuid()), contains(person1));
		assertThat(getPersonFacade().getPersonsAfter(person1.getChangeDate(), batchSize, EntityDto.NO_LAST_SYNCED_UUID), contains(person1));
		{
			List<PersonDto> result = getPersonFacade().getPersonsAfter(t1, batchSize, person1.getUuid());
			assertThat(result, contains(person1));
			assertThat(getPersonFacade().getPersonsAfter(result.get(0).getChangeDate(), batchSize, EntityDto.NO_LAST_SYNCED_UUID), contains(person1));
			// person1 still in result because of Contact reference
			assertThat(getPersonFacade().getPersonsAfter(result.get(0).getChangeDate(), batchSize, person1.getUuid()), contains(person1));
		}

		Date t2 = new Date();
		assertThat(getPersonFacade().getPersonsAfter(t2), is(empty()));
		assertThat(getPersonFacade().getPersonsAfter(t2, batchSize, null), is(empty()));
		assertThat(getPersonFacade().getPersonsAfter(t2, batchSize, EntityDto.NO_LAST_SYNCED_UUID), is(empty()));

		// 2. Check two persons with two timestamps
		PersonDto person2 = creator.createPerson("Second", "Person");
		creator.createContact(nationalUser.toReference(), person2.toReference());

		assertThat(getPersonFacade().getPersonsAfter(t1), contains(person1, person2));
		assertThat(getPersonFacade().getPersonsAfter(t2), contains(person2));

		// 3. Check with third person with TravelEntry
		Date t3 = new Date();
		PersonDto person3 = creator.createPerson("Third", "Person");
		creator
			.createTravelEntry(person3.toReference(), nationalUser.toReference(), Disease.CORONAVIRUS, rdcf.region, rdcf.district, rdcf.pointOfEntry);

		// 3a. Found by TravelEntry
		assertThat(getPersonFacade().getPersonsAfter(t1), contains(person1, person2, person3));
		assertThat(getPersonFacade().getPersonsAfter(t1, batchSize, EntityDto.NO_LAST_SYNCED_UUID), contains(person1, person2, person3));
		assertThat(getPersonFacade().getPersonsAfter(t3, batchSize, EntityDto.NO_LAST_SYNCED_UUID), contains(person3));

		// 3b. Exclude TravelEntries from mobileSync
		MockProducer.setMobileSync(true);
		List<PersonDto> personsAfterT1;
		personsAfterT1 = getPersonFacade().getPersonsAfter(t1, batchSize, EntityDto.NO_LAST_SYNCED_UUID);
		assertThat(personsAfterT1, contains(person1, person2));

		// 4. Test retrieval of persons for mobileSync with different changeDates and uuids
		PersonDto personRead1 = personsAfterT1.get(0);
		PersonDto personRead2 = personsAfterT1.get(1);

		assertThat(getPersonFacade().getPersonsAfter(t1, 1, EntityDto.NO_LAST_SYNCED_UUID), contains(person1));
		assertThat(getPersonFacade().getPersonsAfter(personRead1.getChangeDate(), batchSize, EntityDto.NO_LAST_SYNCED_UUID), contains(person2));
		assertThat(getPersonFacade().getPersonsAfter(personRead2.getChangeDate(), batchSize, EntityDto.NO_LAST_SYNCED_UUID), is(empty()));
		Date changeDateBeforePerson2 = new Date(personRead2.getChangeDate().getTime() - 1L);
		assertThat(getPersonFacade().getPersonsAfter(changeDateBeforePerson2, batchSize, EntityDto.NO_LAST_SYNCED_UUID), contains(person2));

		assertThat(getPersonFacade().getPersonsAfter(personRead2.getChangeDate(), batchSize, "AAAAAA-AAAAAA-AAAAAA-AAAAAA"), contains(person2));
		assertThat(getPersonFacade().getPersonsAfter(personRead2.getChangeDate(), batchSize, "ZZZZZZ-ZZZZZZ-ZZZZZZ-ZZZZZZ"), is(empty()));
		assertThat(getPersonFacade().getPersonsAfter(personRead2.getChangeDate(), batchSize, personRead2.getUuid()), is(empty()));
	}

	@Test
	public void testCreateWithoutUuid() {
		PersonDto person = new PersonDto();
		person.setFirstName("Fname");
		person.setLastName("Lname");
		person.setSex(Sex.UNKNOWN);
		person.setAddress(new LocationDto());
		person.setAddresses(Collections.singletonList(new LocationDto()));

		PersonDto savedPerson = getPersonFacade().savePerson(person);

		assertThat(savedPerson.getUuid(), not(isEmptyOrNullString()));
		assertThat(savedPerson.getAddress().getUuid(), not(isEmptyOrNullString()));
		assertThat(savedPerson.getAddresses().get(0).getUuid(), not(isEmptyOrNullString()));
	}

	@Test
	public void testGetMostRelevantFollowUpStatusByUuid() {
		PersonDto person = creator.createPerson();
		UserDto user = creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.REST_EXTERNAL_VISITS_USER));

		ContactDto contact1 = creator.createContact(user.toReference(), person.toReference());

		for (FollowUpStatus status : FollowUpStatus.values()) {
			contact1.setFollowUpStatus(status);
			getContactFacade().save(contact1);

			if (FollowUpStatus.COMPLETED.equals(status) || FollowUpStatus.NO_FOLLOW_UP.equals(status)) {
				// In this case the status is automatically updated to FOLLOW_UP, because the end of the follow up period has not yet been reached.
				assertThat(getPersonFacade().getMostRelevantFollowUpStatusByUuid(person.getUuid()), is(FollowUpStatus.FOLLOW_UP));
				continue;
			}
			assertThat(getPersonFacade().getMostRelevantFollowUpStatusByUuid(person.getUuid()), is(status));
		}

		ContactDto contact2 = creator.createContact(user.toReference(), person.toReference());
		CaseDataDto case1 = creator.createCase(user.toReference(), person.toReference(), rdcfEntities);

		updateFollowUpStatus(contact1, FollowUpStatus.FOLLOW_UP);
		for (FollowUpStatus status : FollowUpStatus.values()) {
			updateFollowUpStatus(case1, status);
			updateFollowUpStatus(contact2, status);
			// Other states must not interfere one with ongoing follow up
			assertThat(getPersonFacade().getMostRelevantFollowUpStatusByUuid(person.getUuid()), is(FollowUpStatus.FOLLOW_UP));
			updateFollowUpStatus(contact1, status);
			if (FollowUpStatus.COMPLETED.equals(status) || FollowUpStatus.NO_FOLLOW_UP.equals(status)) {
				// In this case the status is automatically updated to FOLLOW_UP, because the end of the follow up period has not yet been reached.
				assertThat(getPersonFacade().getMostRelevantFollowUpStatusByUuid(person.getUuid()), is(FollowUpStatus.FOLLOW_UP));
				updateFollowUpStatus(contact1, FollowUpStatus.FOLLOW_UP);
				continue;
			}
			// In that case one clear status can be calculated
			assertThat(getPersonFacade().getMostRelevantFollowUpStatusByUuid(person.getUuid()), is(status));
			updateFollowUpStatus(case1, FollowUpStatus.FOLLOW_UP);
			// Also ongoing case follow up must not be overwritten
			assertThat(getPersonFacade().getMostRelevantFollowUpStatusByUuid(person.getUuid()), is(FollowUpStatus.FOLLOW_UP));
			updateFollowUpStatus(case1, status);
			updateFollowUpStatus(contact1, FollowUpStatus.FOLLOW_UP);
		}

		updateFollowUpStatus(contact1, FollowUpStatus.LOST);
		updateFollowUpStatus(contact2, FollowUpStatus.CANCELED);
		updateFollowUpStatus(case1, FollowUpStatus.LOST);
		assertThat(getPersonFacade().getMostRelevantFollowUpStatusByUuid(person.getUuid()), is(FollowUpStatus.NO_FOLLOW_UP));

	}

	@Test
	public void testMergePersonsWithoutVaccinations() {
		PersonDto leadPerson = creator.createPerson("Alex", "Miller");
		PersonDto otherPerson = creator.createPerson("Max", "Smith");

		PersonContactDetailDto leadContactDetail =
			creator.createPersonContactDetail(leadPerson.toReference(), true, PersonContactDetailType.PHONE, "123");
		PersonContactDetailDto otherContactDetail =
			creator.createPersonContactDetail(otherPerson.toReference(), true, PersonContactDetailType.PHONE, "456");

		leadPerson.setPersonContactDetails(Collections.singletonList(leadContactDetail));
		otherPerson.setPersonContactDetails(Collections.singletonList(otherContactDetail));

		leadPerson = getPersonFacade().savePerson(leadPerson);
		otherPerson = getPersonFacade().savePerson(otherPerson);

		getPersonFacade().mergePerson(leadPerson, otherPerson);

		assertThat(leadPerson.getPersonContactDetails().size(), is(2));
		assertThat(
			leadPerson.getPersonContactDetails()
				.stream()
				.filter(PersonContactDetailDto::isPrimaryContact)
				.collect(Collectors.toList())
				.get(0)
				.getContactInformation(),
			is("123"));
	}

	public PersonDto createPersonWithImmunizationAndVaccination(String firstName, String lastName) {
		loginWith(nationalUser);
		final PersonDto person = creator.createPerson(firstName, lastName);

		final ImmunizationDto immunization = creator.createImmunizationDto(
			Disease.DENGUE,
			person.toReference(),
			nationalUser.toReference(),
			ImmunizationStatus.EXPIRED,
			MeansOfImmunization.VACCINATION,
			ImmunizationManagementStatus.SCHEDULED,
			rdcf);

		immunization.setNumberOfDoses(2);
		immunization.setStartDate(DateHelper.subtractDays(new Date(), 1));
		immunization.setVaccinations(
			Arrays.asList(
				creator.createVaccinationDto(nationalUser.toReference(), immunization.toReference(), new HealthConditionsDto()),
				creator.createVaccinationDto(nationalUser.toReference(), immunization.toReference(), new HealthConditionsDto())));
		getImmunizationFacade().save(immunization);

		return person;
	}

	@Test
	public void testMergePersonsWithVaccinations() {
		PersonDto leadPerson = createPersonWithImmunizationAndVaccination("firstName1", "lastName1");
		List<ImmunizationDto> leadPersonImmunizationDto = getImmunizationFacade().getByPersonUuids(Collections.singletonList(leadPerson.getUuid()));
		List<VaccinationDto> leadPersonVaccinations = leadPersonImmunizationDto.get(0).getVaccinations();

		PersonDto otherPerson = createPersonWithImmunizationAndVaccination("firstName2", "lastName2");
		List<ImmunizationDto> otherPersonImmunizationDto = getImmunizationFacade().getByPersonUuids(Collections.singletonList(otherPerson.getUuid()));
		List<VaccinationDto> otherPersonVaccinations = otherPersonImmunizationDto.get(0).getVaccinations();

		getPersonFacade().mergePerson(leadPerson, otherPerson);
		List<ImmunizationDto> mergedPersonImmunizationDtoList =
			getImmunizationFacade().getByPersonUuids(Collections.singletonList(leadPerson.getUuid()));

		Assert.assertEquals(mergedPersonImmunizationDtoList.size(), 2);
	}

	@Test
	public void testMergePersonWithVaccinationWithPersonWithoutVaccination() {
		PersonDto leadPersonWithVaccination = createPersonWithImmunizationAndVaccination("firstName3", "lastName3");
		PersonDto otherPersonWithoutVaccination = creator.createPerson("firstName4", "lastName4");

		getPersonFacade().mergePerson(leadPersonWithVaccination, otherPersonWithoutVaccination);
		List<ImmunizationDto> immunizationDtoList =
			getImmunizationFacade().getByPersonUuids(Collections.singletonList(leadPersonWithVaccination.getUuid()));

		Assert.assertEquals(immunizationDtoList.size(), 1);
		Assert.assertEquals(immunizationDtoList.get(0).getVaccinations().size(), 2);
	}

	@Test
	public void testMergePersonWithoutVaccinationWithPersonWithVaccination() {
		PersonDto leadPersonWithoutVaccination = creator.createPerson("firstName5", "lastName5");
		PersonDto otherPersonWithVaccination = createPersonWithImmunizationAndVaccination("firstName6", "lastName6");

		getPersonFacade().mergePerson(leadPersonWithoutVaccination, otherPersonWithVaccination);
		List<ImmunizationDto> immunizationDtoList =
			getImmunizationFacade().getByPersonUuids(Collections.singletonList(leadPersonWithoutVaccination.getUuid()));

		Assert.assertEquals(immunizationDtoList.size(), 1);
	}

	@Test
	public void testMergePersonWithoutVaccinationWithPersonWithoutVaccination() {
		PersonDto leadPersonWithoutVaccination = creator.createPerson("firstName7", "lastName7");
		PersonDto otherPersonWithoutVaccination = creator.createPerson("firstName8", "lastName8");

		getPersonFacade().mergePerson(leadPersonWithoutVaccination, otherPersonWithoutVaccination);
		List<ImmunizationDto> immunizationDtoList =
			getImmunizationFacade().getByPersonUuids(Collections.singletonList(leadPersonWithoutVaccination.getUuid()));

		// both persons are without immunization and vaccination so the merged person should not have immunization
		Assert.assertEquals(immunizationDtoList.size(), 0);
	}

	private void updateFollowUpStatus(ContactDto contact, FollowUpStatus status) {
		contact = getContactFacade().getByUuid(contact.getUuid());
		contact.setFollowUpStatus(status);
		getContactFacade().save(contact);
	}

	private void updateFollowUpStatus(CaseDataDto caze, FollowUpStatus status) {
		caze = getCaseFacade().getCaseDataByUuid(caze.getUuid());
		caze.setFollowUpStatus(status);
		getCaseFacade().save(caze);
	}

	@Test
	public void testSetMissingGeoCoordinates() {

		assertThat(getPersonFacade().setMissingGeoCoordinates(false), equalTo(0L));
	}

	@Test
	public void testGetExportList() {
		UserDto user = creator.createUser(rdcf, creator.getUserRoleReference(DefaultUserRole.REST_EXTERNAL_VISITS_USER));

		PersonDto casePerson = creator.createPerson("Test Fname", "Test Lname", p -> {
			p.setBirthdateYYYY(1999);
			p.setBirthdateMM(3);
			p.setBirthdateDD(28);
			p.setPresentCondition(PresentCondition.ALIVE);
			p.setSex(Sex.UNKNOWN);
			p.setNickname("TestNick");
			p.getAddress().setRegion(rdcf.region);
			p.getAddress().setDistrict(rdcf.district);
			p.getAddress().setFacility(rdcf.facility);
			p.getAddress().setCity("Test city");

			p.setPersonContactDetails(
				Arrays.asList(
					PersonContactDetailDto.build(
						p.toReference(),
						true,
						PersonContactDetailType.PHONE,
						PhoneNumberType.MOBILE,
						null,
						"12345678",
						"Test additional info",
						false,
						null,
						null),
					PersonContactDetailDto.build(
						p.toReference(),
						true,
						PersonContactDetailType.EMAIL,
						null,
						null,
						"test@email.com",
						"Test additional info",
						false,
						null,
						null)));
		});
		creator.createCase(user.toReference(), casePerson.toReference(), rdcf);

		PersonDto contactPerson = creator.createPerson();
		creator.createContact(user.toReference(), user.toReference(), contactPerson.toReference(), null, new Date(), new Date(), Disease.EVD, rdcf);

		List<PersonExportDto> casePersonExport = getPersonFacade().getExportList(new PersonCriteria(), 0, 100);

		assertThat(casePersonExport, hasSize(2));

		PersonExportDto exportedCasePerson = casePersonExport.stream().filter(p -> p.getUuid().equals(casePerson.getUuid())).findFirst().get();
		assertThat(exportedCasePerson.getUuid(), is(casePerson.getUuid()));
		assertThat(exportedCasePerson.getFirstName(), is(casePerson.getFirstName()));
		assertThat(exportedCasePerson.getLastName(), is(casePerson.getLastName()));
		assertThat(exportedCasePerson.getBirthdate().getDateOfBirthYYYY(), is(casePerson.getBirthdateYYYY()));
		assertThat(exportedCasePerson.getBirthdate().getDateOfBirthMM(), is(casePerson.getBirthdateMM()));
		assertThat(exportedCasePerson.getBirthdate().getDateOfBirthDD(), is(casePerson.getBirthdateDD()));
		assertThat(exportedCasePerson.getPresentCondition(), is(casePerson.getPresentCondition()));
		assertThat(exportedCasePerson.getSex(), is(casePerson.getSex()));
		assertThat(exportedCasePerson.getNickname(), is(casePerson.getNickname()));
		assertThat(exportedCasePerson.getRegion(), is(casePerson.getAddress().getRegion().getCaption()));
		assertThat(exportedCasePerson.getDistrict(), is(casePerson.getAddress().getDistrict().getCaption()));
		assertThat(exportedCasePerson.getFacility(), is(casePerson.getAddress().getFacility().getCaption()));
		assertThat(exportedCasePerson.getCity(), is(casePerson.getAddress().getCity()));
		assertThat(exportedCasePerson.getPhone(), is(casePerson.getPhone()));
		assertThat(exportedCasePerson.getEmailAddress(), is(casePerson.getEmailAddress()));

		// only contact persons
		List<PersonExportDto> contactPersonExport =
			getPersonFacade().getExportList(new PersonCriteria().personAssociation(PersonAssociation.CONTACT), 0, 100);
		assertThat(contactPersonExport, hasSize(1));
		assertThat(contactPersonExport.get(0).getUuid(), is(contactPerson.getUuid()));

		// filter by name
		PersonCriteria nameCriteria = new PersonCriteria();
		nameCriteria.setNameAddressPhoneEmailLike("Test Fname");
		List<PersonExportDto> exportByName = getPersonFacade().getExportList(nameCriteria, 0, 100);
		assertThat(exportByName, hasSize(1));
		assertThat(exportByName.get(0).getUuid(), is(casePerson.getUuid()));
	}

	@Test
	public void testGetPersonByContext() {
		UserReferenceDto userRef =
			creator.createUser(rdcfEntities, creator.getUserRoleReference(DefaultUserRole.REST_EXTERNAL_VISITS_USER)).toReference();
		PersonDto casePerson = creator.createPerson();
		CaseDataDto caze = creator.createCase(userRef, casePerson.toReference(), rdcfEntities);
		assertThat(getPersonFacade().getByContext(PersonContext.CASE, caze.getUuid()), equalTo(casePerson));

		PersonDto contactPerson = creator.createPerson();
		ContactDto contact = creator.createContact(userRef, contactPerson.toReference());

		assertThat(getPersonFacade().getByContext(PersonContext.CONTACT, contact.getUuid()), equalTo(contactPerson));

		PersonDto eventParticipantPerson = creator.createPerson();
		EventParticipantDto eventParticipant =
			creator.createEventParticipant(creator.createEvent(userRef).toReference(), eventParticipantPerson, userRef);

		assertThat(getPersonFacade().getByContext(PersonContext.EVENT_PARTICIPANT, eventParticipant.getUuid()), equalTo(eventParticipantPerson));
	}

	@Test
	public void testUserWithLimitedDiseaseSeeOnlyLimitedTravelEntry() {
		PersonCriteria criteria = new PersonCriteria();
		criteria.setPersonAssociation(PersonAssociation.TRAVEL_ENTRY);

		// CORONAVIRUS Travel Entry
		PersonDto personWithCorona = creator.createPerson("Person Coronavirus", "Test");
		creator.createTravelEntry(
			personWithCorona.toReference(),
			nationalUser.toReference(),
			Disease.CORONAVIRUS,
			rdcf.region,
			rdcf.district,
			rdcf.pointOfEntry);

		// DENGUE Travel Entry
		PersonDto personWithDengue = creator.createPerson("Person Dengue", "Test");
		creator.createTravelEntry(
			personWithDengue.toReference(),
			nationalUser.toReference(),
			Disease.DENGUE,
			rdcf.region,
			rdcf.district,
			rdcf.pointOfEntry);

		//National User with no restrictions can see all the travel entries
		List<PersonIndexDto> personIndexDtos = getPersonFacade().getIndexList(criteria, 0, 100, null);
		assertEquals(2, personIndexDtos.size());
		List<String> firstNames = personIndexDtos.stream().map(p -> p.getFirstName()).collect(Collectors.toList());
		assertTrue(firstNames.contains(personWithCorona.getFirstName()));
		assertTrue(firstNames.contains(personWithDengue.getFirstName()));

		//login with a user wiht limieted disease restrictions
		final UserDto user = creator.createUser(
			rdcf,
			"Limieted Disease",
			"National User",
			Disease.DENGUE,
			creator.getUserRoleReference(DefaultUserRole.NATIONAL_USER));
		loginWith(user);

		personIndexDtos = getPersonFacade().getIndexList(criteria, 0, 100, null);
		assertEquals(1, personIndexDtos.size());
		assertEquals(personWithDengue.getFirstName(), personIndexDtos.get(0).getFirstName());
	}

}
