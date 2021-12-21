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
import de.symeda.sormas.api.sample.AdditionalTestCriteria;
import de.symeda.sormas.api.sample.AdditionalTestDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Path("/additionaltests")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class AdditionalTestResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<AdditionalTestDto> getAllAdditionalTests(@PathParam("since") long since) {
		return FacadeProvider.getAdditionalTestFacade().getAllActiveAdditionalTestsAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<AdditionalTestDto> getByUuids(List<String> uuids) {
		List<AdditionalTestDto> result = FacadeProvider.getAdditionalTestFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/push")
	public List<PushResult> postAdditionalTests(@Valid List<AdditionalTestDto> dtos) {
		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getAdditionalTestFacade()::saveAdditionalTest);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllActiveUuids() {
		return FacadeProvider.getAdditionalTestFacade().getAllActiveUuids();
	}

	@POST
	@Path("/indexList")
	public Page<AdditionalTestDto> getIndexList(
		@RequestBody CriteriaWithSorting<AdditionalTestCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getAdditionalTestFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

}
