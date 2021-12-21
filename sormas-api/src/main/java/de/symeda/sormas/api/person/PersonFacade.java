package de.symeda.sormas.api.person;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.contact.FollowUpStatus;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface PersonFacade {

	List<PersonDto> getPersonsAfter(Date date);

	List<PersonDto> getDeathsBetween(Date fromDate, Date toDate, DistrictReferenceDto districtRef, Disease disease);

	PersonReferenceDto getReferenceByUuid(String uuid);

	JournalPersonDto getPersonForJournal(String uuid);

	PersonDto savePerson(@Valid PersonDto dto);

	PersonDto savePerson(@Valid PersonDto source, boolean skipValidation);

	DataHelper.Pair<CaseClassification, PersonDto> savePersonWithoutNotifyingExternalJournal(@Valid PersonDto source);

	void validate(PersonDto dto);

	List<String> getAllUuids();

	PersonDto getPersonByUuid(String uuid);

	List<PersonDto> getByUuids(List<String> uuids);

	/**
	 * Returns a list with the names of all persons that the user has access to and that match the criteria.
	 * This only includes persons that are associated with an active case, contact or event participant.
	 * 
	 * @return
	 */
	List<SimilarPersonDto> getSimilarPersonDtos(UserReferenceDto user, PersonSimilarityCriteria criteria);

	boolean checkMatchingNameInDatabase(UserReferenceDto userRef, PersonSimilarityCriteria criteria);

	Boolean isValidPersonUuid(String personUuid);

	List<PersonFollowUpEndDto> getLatestFollowUpEndDates(Date since, boolean forSymptomsJournal);

	Date getLatestFollowUpEndDateByUuid(String uuid);

	FollowUpStatus getMostRelevantFollowUpStatusByUuid(String uuid);

	boolean setSymptomJournalStatus(String personUuid, SymptomJournalStatus status);

	List<PersonIndexDto> getIndexList(PersonCriteria criteria, Integer offset, Integer limit, List<SortProperty> sortProperties);

	List<PersonExportDto> getExportList(PersonCriteria criteria, int first, int max);

	Page<PersonIndexDto> getIndexPage(PersonCriteria personCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	long count(PersonCriteria criteria);

	boolean exists(String uuid);

	boolean doesExternalTokenExist(String externalToken, String personUuid);

	long setMissingGeoCoordinates(boolean overwriteExistingCoordinates);

	boolean isSharedWithoutOwnership(String uuid);

	List<PersonDto> getByExternalIds(List<String> externalIds);

	void updateExternalData(@Valid List<ExternalDataDto> externalData) throws ExternalDataUpdateException;

	void mergePerson(PersonDto leadPerson, PersonDto otherPerson);

	PersonDto getByContext(PersonContext context, String contextUuid);
}
