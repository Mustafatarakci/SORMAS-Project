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
import de.symeda.sormas.api.infrastructure.country.CountryCriteria;
import de.symeda.sormas.api.infrastructure.country.CountryDto;
import de.symeda.sormas.api.infrastructure.country.CountryIndexDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey documentation</a>
 * @see <a href="https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey documentation HTTP Methods</a>
 *
 */
@Path("/countries")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class CountryResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<CountryDto> getAll(@PathParam("since") long since) {
		return FacadeProvider.getCountryFacade().getAllAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<CountryDto> getByUuids(List<String> uuids) {
		return FacadeProvider.getCountryFacade().getByUuids(uuids);
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids() {
		return FacadeProvider.getCountryFacade().getAllUuids();
	}

	@POST
	@Path("/push")
	public List<PushResult> postCountries(@Valid List<CountryDto> dtos) {
		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getCountryFacade()::save);
		return result;
	}

	@POST
	@Path("/indexList")
	public Page<CountryIndexDto> getIndexList(
		@RequestBody CriteriaWithSorting<CountryCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getCountryFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}
}
