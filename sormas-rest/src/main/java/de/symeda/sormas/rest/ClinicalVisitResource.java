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
import de.symeda.sormas.api.clinicalcourse.ClinicalVisitCriteria;
import de.symeda.sormas.api.clinicalcourse.ClinicalVisitDto;
import de.symeda.sormas.api.clinicalcourse.ClinicalVisitIndexDto;
import de.symeda.sormas.api.common.Page;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Path("/clinicalvisits")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class ClinicalVisitResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<ClinicalVisitDto> getAllVisits(@PathParam("since") long since) {
		return FacadeProvider.getClinicalVisitFacade().getAllActiveClinicalVisitsAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<ClinicalVisitDto> getByUuids(List<String> uuids) {

		List<ClinicalVisitDto> result = FacadeProvider.getClinicalVisitFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/push")
	public List<PushResult> postVisits(@Valid List<ClinicalVisitDto> dtos) {

		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getClinicalVisitFacade()::saveClinicalVisit);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllActiveUuids() {
		return FacadeProvider.getClinicalVisitFacade().getAllActiveUuids();
	}

	@POST
	@Path("/indexList")
	public Page<ClinicalVisitIndexDto> getIndexList(
		@RequestBody CriteriaWithSorting<ClinicalVisitCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getClinicalVisitFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}
}
