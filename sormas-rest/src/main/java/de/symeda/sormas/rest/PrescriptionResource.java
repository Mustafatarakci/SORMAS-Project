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
import javax.ws.rs.core.MediaType;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.PushResult;
import de.symeda.sormas.api.caze.CriteriaWithSorting;
import de.symeda.sormas.api.therapy.PrescriptionCriteria;
import de.symeda.sormas.api.therapy.PrescriptionDto;
import de.symeda.sormas.api.therapy.PrescriptionIndexDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Path("/prescriptions")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class PrescriptionResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<PrescriptionDto> getAllPrescriptions(@PathParam("since") long since) {
		return FacadeProvider.getPrescriptionFacade().getAllActivePrescriptionsAfter(new Date(since));
	}

	@POST
	@Path("/push")
	public List<PushResult> postPrescriptions(@Valid List<PrescriptionDto> dtos) {
		return savePushedDto(dtos, FacadeProvider.getPrescriptionFacade()::savePrescription);
	}

	@GET
	@Path("/uuids")
	public List<String> getAllActiveUuids() {
		return FacadeProvider.getPrescriptionFacade().getAllActiveUuids();
	}

	@POST
	@Path("/query")
	public List<PrescriptionDto> getByUuids(List<String> uuids) {
		return FacadeProvider.getPrescriptionFacade().getByUuids(uuids);
	}

	@POST
	@Path("/indexList")
	public List<PrescriptionIndexDto> getIndexList(@RequestBody CriteriaWithSorting<PrescriptionCriteria> criteriaWithSorting) {
		return FacadeProvider.getPrescriptionFacade().getIndexList(criteriaWithSorting.getCriteria());
	}
}
