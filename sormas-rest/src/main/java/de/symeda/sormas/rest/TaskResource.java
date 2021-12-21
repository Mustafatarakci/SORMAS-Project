package de.symeda.sormas.rest;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.PushResult;
import de.symeda.sormas.api.caze.CriteriaWithSorting;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.task.TaskCriteria;
import de.symeda.sormas.api.task.TaskDto;
import de.symeda.sormas.api.task.TaskIndexDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey
 *      documentation</a>
 * @see <a href=
 *      "https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey
 *      documentation HTTP Methods</a>
 *
 */
@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class TaskResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<TaskDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getTaskFacade().getAllActiveTasksAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<TaskDto> getByUuids(List<String> uuids) {

		List<TaskDto> result = FacadeProvider.getTaskFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/push")
	public List<PushResult> postTasks(@Valid List<TaskDto> dtos) {

		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getTaskFacade()::saveTask);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllActiveUuids() {
		return FacadeProvider.getTaskFacade().getAllActiveUuids();
	}

	@POST
	@Path("/indexList")
	public Page<TaskIndexDto> getIndexList(
		@RequestBody CriteriaWithSorting<TaskCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getTaskFacade().getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

	@GET
	@Path("/{uuid}")
	public TaskDto getByUuid(@PathParam("uuid") String uuid) {
		return FacadeProvider.getTaskFacade().getByUuid(uuid);
	}

	@GET
	@Path("/archived/{since}")
	public List<String> getArchivedUuidsSince(@PathParam("since") long since) {
		return FacadeProvider.getTaskFacade().getArchivedUuidsSince(new Date(since));
	}

	@POST
	@Path("/delete")
	public List<String> delete(List<String> uuids) {
		return FacadeProvider.getTaskFacade().deleteTasks(uuids);
	}

}
