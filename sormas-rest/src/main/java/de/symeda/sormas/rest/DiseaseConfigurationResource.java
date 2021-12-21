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

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.disease.DiseaseConfigurationDto;

@Path("/diseaseconfigurations")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed("USER")
public class DiseaseConfigurationResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<DiseaseConfigurationDto> getAllDiseaseConfigurations(@PathParam("since") long since) {
		return FacadeProvider.getDiseaseConfigurationFacade().getAllAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<DiseaseConfigurationDto> getByUuids(List<String> uuids) {
		return FacadeProvider.getDiseaseConfigurationFacade().getByUuids(uuids);
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getDiseaseConfigurationFacade().getAllUuids();
	}

	@GET
	@Path("/diseaseNames")
	public List<Disease> getDiseases(
		@QueryParam("active") boolean active,
		@QueryParam("primary") boolean primary,
		@QueryParam("caseBased") boolean caseBased) {
		return FacadeProvider.getDiseaseConfigurationFacade().getAllDiseases(active, primary, caseBased);
	}

}
