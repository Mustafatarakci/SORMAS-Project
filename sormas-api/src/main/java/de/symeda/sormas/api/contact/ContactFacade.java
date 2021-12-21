package de.symeda.sormas.api.contact;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.dashboard.DashboardContactDto;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.followup.FollowUpPeriodDto;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.visit.VisitSummaryExportDto;

@Remote
public interface ContactFacade {

	List<ContactDto> getAllActiveContactsAfter(Date date);

	ContactDto getContactByUuid(String uuid);

	Boolean isValidContactUuid(String uuid);

	ContactDto saveContact(@Valid ContactDto dto);

	ContactDto saveContact(@Valid ContactDto dto, boolean handleChanges, boolean handleCaseChanges);

	ContactReferenceDto getReferenceByUuid(String uuid);

	List<String> getAllActiveUuids();

	void generateContactFollowUpTasks();

	List<ContactDto> getByUuids(List<String> uuids);

	Long countContactsForMap(RegionReferenceDto regionRef, DistrictReferenceDto districtRef, Disease disease, Date from, Date to);

	List<MapContactDto> getContactsForMap(RegionReferenceDto regionRef, DistrictReferenceDto districtRef, Disease disease, Date from, Date to);

	void deleteContact(String contactUuid);

	List<String> deleteContacts(List<String> contactUuids);

	FollowUpPeriodDto calculateFollowUpUntilDate(ContactDto contactDto, boolean ignoreOverwrite);

	List<ContactIndexDto> getIndexList(ContactCriteria contactCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	List<ContactListEntryDto> getEntriesList(String personUuid, Integer first, Integer max);

	Page<ContactIndexDto> getIndexPage(ContactCriteria contactCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<ContactIndexDetailedDto> getIndexDetailedList(
		ContactCriteria contactCriteria,
		Integer first,
		Integer max,
		List<SortProperty> sortProperties);

	List<ContactExportDto> getExportList(
		ContactCriteria contactCriteria,
		Collection<String> selectedRows,
		int first,
		int max,
		ExportConfigurationDto exportConfiguration,
		Language userLanguage);

	List<VisitSummaryExportDto> getVisitSummaryExportList(
		ContactCriteria contactCriteria,
		Collection<String> selectedRows,
		int first,
		int max,
		Language userLanguage);

	long countMaximumFollowUpDays(ContactCriteria contactCriteria);

	List<DashboardContactDto> getContactsForDashboard(
		RegionReferenceDto regionRef,
		DistrictReferenceDto districtRef,
		Disease disease,
		Date from,
		Date to);

	Map<ContactStatus, Long> getNewContactCountPerStatus(ContactCriteria contactCriteria);

	Map<ContactClassification, Long> getNewContactCountPerClassification(ContactCriteria contactCriteria);

	Map<FollowUpStatus, Long> getNewContactCountPerFollowUpStatus(ContactCriteria contactCriteria);

	int getFollowUpUntilCount(ContactCriteria contactCriteria);

	long count(ContactCriteria contactCriteria);

	List<String> getDeletedUuidsSince(Date since);

	boolean isDeleted(String contactUuid);

	List<ContactFollowUpDto> getContactFollowUpList(
		ContactCriteria contactCriteria,
		Date referenceDate,
		int interval,
		Integer first,
		Integer max,
		List<SortProperty> sortProperties);

	int[] getContactCountsByCasesForDashboard(List<Long> contactIds);

	/**
	 * @param caseUuids
	 *            The cases to check, how much of them where created from a contact.
	 * @return Number of cases, that resulted from a contact.
	 */
	int getNonSourceCaseCountForDashboard(List<String> caseUuids);

	void validate(ContactDto contact);

	List<SimilarContactDto> getMatchingContacts(ContactSimilarityCriteria criteria);

	boolean isContactEditAllowed(String contactUuid);

	boolean exists(String uuid);

	boolean doesExternalTokenExist(String externalToken, String contactUuid);

	List<ContactDto> getByPersonUuids(List<String> personUuids);

	void mergeContact(String leadUuid, String otherUuid);

	void deleteContactAsDuplicate(String uuid, String duplicateOfUuid);

	List<MergeContactIndexDto[]> getContactsForDuplicateMerging(ContactCriteria criteria, boolean showDuplicatesWithDifferentRegion);

	void updateCompleteness(String uuid);

	void updateExternalData(@Valid List<ExternalDataDto> externalData) throws ExternalDataUpdateException;
}
