package de.symeda.sormas.api.event;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolException;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;

@Remote
public interface EventFacade {

	List<EventDto> getAllActiveEventsAfter(Date date);

	Map<Disease, Long> getEventCountByDisease(EventCriteria eventCriteria);

	EventDto getEventByUuid(String uuid, boolean detailedReferences);

	EventDto saveEvent(@Valid @NotNull EventDto dto);

	EventReferenceDto getReferenceByUuid(String uuid);

	EventReferenceDto getReferenceByEventParticipant(String uuid);

	List<String> getAllActiveUuids();

	List<EventDto> getByUuids(List<String> uuids);

	void deleteEvent(String eventUuid) throws ExternalSurveillanceToolException;

	List<String> deleteEvents(List<String> eventUuids);

	long count(EventCriteria eventCriteria);

	List<EventIndexDto> getIndexList(EventCriteria eventCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	Page<EventIndexDto> getIndexPage(@NotNull EventCriteria eventCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<EventExportDto> getExportList(EventCriteria eventCriteria, Collection<String> selectedRows, Integer first, Integer max);

	boolean isArchived(String caseUuid);

	boolean isDeleted(String eventUuid);

	void archiveOrDearchiveEvent(String eventUuid, boolean archive);

	List<String> getArchivedUuidsSince(Date since);

	List<String> getDeletedUuidsSince(Date since);

	void archiveAllArchivableEvents(int daysAfterEventsGetsArchived);

	Boolean isEventEditAllowed(String eventUuid);

	boolean exists(String uuid);

	boolean doesExternalTokenExist(String externalToken, String eventUuid);

	String getUuidByCaseUuidOrPersonUuid(String value);

	Set<String> getAllSubordinateEventUuids(String eventUuid);

	Set<String> getAllSuperordinateEventUuids(String eventUuid);

	Set<String> getAllEventUuidsByEventGroupUuid(String eventGroupUuid);

	String getFirstEventUuidWithOwnershipHandedOver(List<String> eventUuids);

	void validate(EventDto dto) throws ValidationRuntimeException;

	Set<RegionReferenceDto> getAllRegionsRelatedToEventUuids(List<String> uuids);

	void updateExternalData(@Valid List<ExternalDataDto> externalData) throws ExternalDataUpdateException;

	List<String> getSubordinateEventUuids(List<String> uuids);

	boolean hasRegionAndDistrict(String eventUuid);

	boolean hasAnyEventParticipantWithoutJurisdiction(String eventUuid);
}
