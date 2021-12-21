package de.symeda.sormas.api.event;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface EventParticipantFacade {

	List<EventParticipantDto> getAllEventParticipantsByEventAfter(Date date, String eventUuid);

	List<EventParticipantDto> getAllActiveEventParticipantsByEvent(String eventUuid);

	List<EventParticipantDto> getAllActiveEventParticipantsAfter(Date date);

	EventParticipantDto getEventParticipantByUuid(String uuid);

	EventParticipantDto saveEventParticipant(@Valid EventParticipantDto dto);

	List<String> getAllActiveUuids();

	List<EventParticipantDto> getByUuids(List<String> uuids);

	void deleteEventParticipant(EventParticipantReferenceDto eventParticipantRef);

	List<EventParticipantIndexDto> getIndexList(
		EventParticipantCriteria eventParticipantCriteria,
		Integer first,
		Integer max,
		List<SortProperty> sortProperties);

	Page<EventParticipantIndexDto> getIndexPage(
		EventParticipantCriteria eventParticipantCriteria,
		Integer offset,
		Integer size,
		List<SortProperty> sortProperties);

	List<EventParticipantListEntryDto> getListEntries(EventParticipantCriteria eventParticipantCriteria, Integer first, Integer max);

	EventParticipantDto getByUuid(String uuid);

	void validate(EventParticipantDto eventParticipant);

	long count(EventParticipantCriteria eventParticipantCriteria);

	Map<String, Long> getContactCountPerEventParticipant(List<String> eventParticipantUuids, EventParticipantCriteria eventParticipantCriteria);

	boolean exists(String uuid);

	EventParticipantReferenceDto getReferenceByUuid(String uuid);

	EventParticipantReferenceDto getReferenceByEventAndPerson(String eventUuid, String personUuid);

	List<String> getDeletedUuidsSince(Date date);

	boolean isEventParticipantEditAllowed(String uuid);

	EventParticipantDto getFirst(EventParticipantCriteria eventParticipantCriteria);

	List<EventParticipantExportDto> getExportList(
		EventParticipantCriteria eventParticipantCriteria,
		Collection<String> selectedRows,
		int first,
		int max,
		Language userLanguage,
		ExportConfigurationDto exportConfiguration);

	List<EventParticipantDto> getByEventUuids(List<String> eventUuids);

	List<SimilarEventParticipantDto> getMatchingEventParticipants(EventParticipantCriteria criteria);

	List<EventParticipantDto> getByPersonUuids(List<String> personUuids);

	List<EventParticipantDto> getByEventAndPersons(String eventUuid, List<String> personUuids);
}
