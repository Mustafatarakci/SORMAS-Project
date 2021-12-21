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
import de.symeda.sormas.api.action.ActionCriteria;
import de.symeda.sormas.api.action.ActionDto;
import de.symeda.sormas.api.caze.CriteriaWithSorting;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.event.EventActionIndexDto;
import de.symeda.sormas.api.event.EventCriteria;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey
 *      documentation</a>
 * @see <a href=
 *      "https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey
 *      documentation HTTP Methods</a>
 *
 */
@Path("/actions")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class ActionResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<ActionDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getActionFacade().getAllActionsAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<ActionDto> getByUuids(List<String> uuids) {

		List<ActionDto> result = FacadeProvider.getActionFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/push")
	public List<PushResult> postActions(@Valid List<ActionDto> dtos) {

		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getActionFacade()::saveAction);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getActionFacade().getAllUuids();
	}

	@POST
	@Path("/indexEventActionList")
	public Page<EventActionIndexDto> getEventActionIndexList(
		@RequestBody CriteriaWithSorting<EventCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getActionFacade()
			.getEventActionIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

	@POST
	@Path("/indexActionList")
	public Page<ActionDto> getActionList(
		@RequestBody CriteriaWithSorting<ActionCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getActionFacade()
			.getActionPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

}
