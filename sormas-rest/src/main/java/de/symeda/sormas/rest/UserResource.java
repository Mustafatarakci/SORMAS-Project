package de.symeda.sormas.rest;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CriteriaWithSorting;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.task.TaskContextIndex;
import de.symeda.sormas.api.user.UserCriteria;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserReferenceWithTaskNumbersDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey documentation</a>
 * @see <a href="https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey documentation HTTP Methods</a>
 *
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed("USER")
public class UserResource {

	@GET
	@Path("/all/{since}")
	public List<UserDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getUserFacade().getAllAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<UserDto> getByUuids(List<String> uuids) {
		List<UserDto> result = FacadeProvider.getUserFacade().getByUuids(uuids);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getUserFacade().getAllUuids();
	}

	@POST
	@Path("/indexList")
	public Page<UserDto> getIndexList(
		@RequestBody CriteriaWithSorting<UserCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getUserFacade().getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

	@GET
	@Path("/{uuid}")
	public UserDto getByUuid(@PathParam("uuid") String uuid) {
		return FacadeProvider.getUserFacade().getByUuid(uuid);
	}

	@POST
	@Path("/userReferenceWithNoOfTask")
	public List<UserReferenceWithTaskNumbersDto> getUsersWithTaskNumbers(@RequestBody TaskContextIndex taskContextIndex) {
		return FacadeProvider.getUserFacade().getAssignableUsersWithTaskNumbers(taskContextIndex);
	}
}
