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
import javax.ws.rs.core.Response;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.PushResult;
import de.symeda.sormas.api.caze.CriteriaWithSorting;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.person.PersonCriteria;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonIndexDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey
 *      documentation</a>
 * @see <a href=
 *      "https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey
 *      documentation HTTP Methods</a>
 *
 */
@Path("/persons")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class PersonResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<PersonDto> getAllPersons(@PathParam("since") long since) {
		return FacadeProvider.getPersonFacade().getPersonsAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<PersonDto> getByUuids(List<String> uuids) {
		return FacadeProvider.getPersonFacade().getByUuids(uuids);
	}

	@POST
	@Path("/query/byExternalIds")
	public List<PersonDto> getByExternalIds(List<String> externalIds) {
		return FacadeProvider.getPersonFacade().getByExternalIds(externalIds);
	}

	@POST
	@Path("/push")
	public List<PushResult> postPersons(@Valid List<PersonDto> dtos) {
		return savePushedDto(dtos, FacadeProvider.getPersonFacade()::savePerson);
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getPersonFacade().getAllUuids();
	}

	@GET
	@Path("/{uuid}")
	public PersonDto getByUuid(@PathParam("uuid") String uuid) {
		return FacadeProvider.getPersonFacade().getPersonByUuid(uuid);
	}

	@POST
	@Path("/indexList")
	public Page<PersonIndexDto> getIndexList(
		@RequestBody CriteriaWithSorting<PersonCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getPersonFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

	@POST
	@Path("/externalData")
	public Response updateExternalData(@Valid List<ExternalDataDto> externalData) {
		try {
			FacadeProvider.getPersonFacade().updateExternalData(externalData);
			return Response.status(Response.Status.OK).build();
		} catch (ExternalDataUpdateException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
