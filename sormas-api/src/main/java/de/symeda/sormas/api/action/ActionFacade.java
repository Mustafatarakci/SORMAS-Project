package de.symeda.sormas.api.action;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.event.EventActionExportDto;
import de.symeda.sormas.api.event.EventActionIndexDto;
import de.symeda.sormas.api.event.EventCriteria;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface ActionFacade {

	ActionDto saveAction(@Valid ActionDto dto);

	ActionDto getByUuid(String uuid);

	void deleteAction(ActionDto ActionDto);

	List<ActionDto> getAllActionsAfter(Date date);

	List<ActionDto> getByUuids(List<String> uuids);

	List<String> getAllUuids();

	List<ActionStatEntry> getActionStats(ActionCriteria actionCriteria);

	List<ActionDto> getActionList(ActionCriteria criteria, Integer first, Integer max);

	List<ActionDto> getActionList(ActionCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties);

	List<EventActionIndexDto> getEventActionList(EventCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties);

	Page<EventActionIndexDto> getEventActionIndexPage(EventCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	Page<ActionDto> getActionPage(ActionCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<EventActionExportDto> getEventActionExportList(EventCriteria criteria, Integer first, Integer max);

	long countEventActions(EventCriteria criteria);

	long countActions(ActionCriteria criteria);
}
