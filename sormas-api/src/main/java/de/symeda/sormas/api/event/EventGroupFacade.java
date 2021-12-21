package de.symeda.sormas.api.event;

import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface EventGroupFacade {

	EventGroupReferenceDto getReferenceByUuid(String uuid);

	boolean exists(String uuid);

	boolean isArchived(String uuid);

	EventGroupDto getEventGroupByUuid(String uuid);

	List<EventGroupReferenceDto> getCommonEventGroupsByEvents(List<EventReferenceDto> eventReferences);

	List<EventGroupIndexDto> getIndexList(EventGroupCriteria eventGroupCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	long count(EventGroupCriteria eventGroupCriteria);

	Page<EventGroupIndexDto> getIndexPage(
		@NotNull EventGroupCriteria eventGroupCriteria,
		Integer offset,
		Integer size,
		List<SortProperty> sortProperties);

	EventGroupDto saveEventGroup(@Valid @NotNull EventGroupDto eventGroup);

	void linkEventToGroup(EventReferenceDto eventReference, EventGroupReferenceDto eventGroupReference);

	void linkEventToGroups(EventReferenceDto eventReference, List<EventGroupReferenceDto> eventGroupReferences);

	void linkEventsToGroup(List<EventReferenceDto> eventReferences, EventGroupReferenceDto eventGroupReference);

	void linkEventsToGroups(List<EventReferenceDto> eventReferences, List<EventGroupReferenceDto> eventGroupReferences);

	void unlinkEventGroup(EventReferenceDto eventReference, EventGroupReferenceDto eventGroupReference);

	void deleteEventGroup(String uuid);

	void archiveOrDearchiveEventGroup(String uuid, boolean archive);

	List<RegionReferenceDto> getEventGroupRelatedRegions(String uuid);

	void notifyEventEventGroupCreated(EventGroupReferenceDto eventGroupReference);

	void notifyEventAddedToEventGroup(EventGroupReferenceDto eventGroupReference, List<EventReferenceDto> eventReferences);

	void notifyEventRemovedFromEventGroup(EventGroupReferenceDto eventGroupReference, List<EventReferenceDto> eventReferences);
}
