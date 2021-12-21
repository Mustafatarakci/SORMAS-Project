package de.symeda.sormas.rest;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
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
import de.symeda.sormas.api.infrastructure.region.RegionCriteria;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import de.symeda.sormas.api.infrastructure.region.RegionIndexDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey documentation</a>
 * @see <a href="https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey documentation HTTP Methods</a>
 *
 */
@Path("/regions")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class RegionResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<RegionDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getRegionFacade().getAllAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<RegionDto> getByUuids(List<String> uuids) {
		List<RegionDto> result = FacadeProvider.getRegionFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/push")
	public List<PushResult> postRegions(@Valid List<RegionDto> dtos) {
		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getRegionFacade()::save);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getRegionFacade().getAllUuids();
	}

	@POST
	@Path("/indexList")
	public Page<RegionIndexDto> getIndexList(
		@RequestBody CriteriaWithSorting<RegionCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getRegionFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}
}
