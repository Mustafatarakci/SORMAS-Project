package de.symeda.sormas.rest;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.classification.DiseaseClassificationCriteriaDto;

@Path("/classification")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed("USER")
public class ClassificationResource {

	@GET
	@Path("/all/{since}")
	public List<DiseaseClassificationCriteriaDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getCaseClassificationFacade().getAllSince(new Date(since));
	}
}
