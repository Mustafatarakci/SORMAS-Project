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
import de.symeda.sormas.api.infrastructure.pointofentry.PointOfEntryCriteria;
import de.symeda.sormas.api.infrastructure.pointofentry.PointOfEntryDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey documentation</a>
 * @see <a href="https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey documentation HTTP Methods</a>
 *
 */
@Path("/pointsofentry")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class PointOfEntryResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<PointOfEntryDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getPointOfEntryFacade().getAllAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<PointOfEntryDto> getByUuids(List<String> uuids) {
		List<PointOfEntryDto> result = FacadeProvider.getPointOfEntryFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/indexList")
	public Page<PointOfEntryDto> getIndexList(
		@RequestBody CriteriaWithSorting<PointOfEntryCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getPointOfEntryFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getPointOfEntryFacade().getAllUuids();
	}

	@POST
	@Path("/push")
	public List<PushResult> postPointOfEntries(@Valid List<PointOfEntryDto> dtos) {
		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getPointOfEntryFacade()::save);
		return result;
	}
}
