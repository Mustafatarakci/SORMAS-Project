package de.symeda.sormas.api.task;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface TaskFacade {

	TaskDto saveTask(@Valid TaskDto dto);

	List<TaskDto> getAllActiveTasksAfter(Date date);

	List<TaskDto> getAllByCase(CaseReferenceDto caseRef);

	Page<TaskIndexDto> getIndexPage(TaskCriteria taskCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<TaskDto> getAllByContact(ContactReferenceDto contactRef);

	List<TaskDto> getAllByEvent(EventReferenceDto eventRef);

	List<TaskDto> getAllPendingByCase(CaseReferenceDto caseDataDto);

	List<TaskDto> getByUuids(List<String> uuids);

	long getPendingTaskCountByContact(ContactReferenceDto contactDto);

	long getPendingTaskCountByEvent(EventReferenceDto eventDto);

	Map<String, Long> getPendingTaskCountPerUser(List<String> userUuids);

	TaskDto getByUuid(String uuid);

	List<String> getAllActiveUuids();

	void deleteTask(TaskDto taskDto);

	List<String> deleteTasks(List<String> taskUuids);

	long count(TaskCriteria criteria);

	List<TaskIndexDto> getIndexList(TaskCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties);

	List<TaskExportDto> getExportList(TaskCriteria criteria, Collection<String> selectedRows, int first, int max);

	void sendNewAndDueTaskMessages();

	void updateArchived(List<String> taskUuids, boolean archived);

	List<DistrictReferenceDto> getDistrictsByTaskUuids(List<String> taskUuids, Long limit);

	List<String> getArchivedUuidsSince(Date since);
}
