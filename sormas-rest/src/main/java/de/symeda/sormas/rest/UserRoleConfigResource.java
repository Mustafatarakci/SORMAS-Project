package de.symeda.sormas.rest;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.user.UserRoleConfigDto;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey
 *      documentation</a>
 * @see <a href=
 *      "https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey
 *      documentation HTTP Methods</a>
 *
 */
@Path("/userroles")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed("USER")
public class UserRoleConfigResource {

	@GET
	@Path("/all/{since}")
	public List<UserRoleConfigDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getUserRoleConfigFacade().getAllAfter(new Date(since));
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getUserRoleConfigFacade().getAllUuids();
	}

	@GET
	@Path("/deleted/{since}")
	public List<String> getDeletedUuids(@PathParam("since") long since) {
		return FacadeProvider.getUserRoleConfigFacade().getDeletedUuids(new Date(since));
	}
}
