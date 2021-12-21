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
import de.symeda.sormas.api.contact.ContactCriteria;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactIndexDto;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey
 *      documentation</a>
 * @see <a href=
 *      "https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey
 *      documentation HTTP Methods</a>
 *
 */
@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class ContactResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<ContactDto> getAllContacts(@PathParam("since") long since) {
		return FacadeProvider.getContactFacade().getAllActiveContactsAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<ContactDto> getByUuids(List<String> uuids) {

		List<ContactDto> result = FacadeProvider.getContactFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/query/persons")
	public List<ContactDto> getByPersonUuids(List<String> uuids) {
		return FacadeProvider.getContactFacade().getByPersonUuids(uuids);
	}

	@POST
	@Path("/push")
	public List<PushResult> postContacts(@Valid List<ContactDto> dtos) {

		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getContactFacade()::saveContact);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllActiveUuids() {
		return FacadeProvider.getContactFacade().getAllActiveUuids();
	}

	@GET
	@Path("/deleted/{since}")
	public List<String> getDeletedUuidsSince(@PathParam("since") long since) {
		return FacadeProvider.getContactFacade().getDeletedUuidsSince(new Date(since));
	}

	@POST
	@Path("/indexList")
	public Page<ContactIndexDto> getIndexList(
		@RequestBody CriteriaWithSorting<ContactCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getContactFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

	@POST
	@Path("/externalData")
	public Response updateExternalData(@Valid List<ExternalDataDto> externalData) {
		try {
			FacadeProvider.getContactFacade().updateExternalData(externalData);
			return Response.status(Response.Status.OK).build();
		} catch (ExternalDataUpdateException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/delete")
	public List<String> delete(List<String> uuids) {
		return FacadeProvider.getContactFacade().deleteContacts(uuids);
	}

	@GET
	@Path("/{uuid}")
	public ContactDto getByUuid(@PathParam("uuid") String uuid) {
		return FacadeProvider.getContactFacade().getContactByUuid(uuid);
	}

}
