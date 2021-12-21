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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.PushResult;
import de.symeda.sormas.api.report.WeeklyReportDto;

@Path("/weeklyreports")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed("USER")
public class WeeklyReportResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<WeeklyReportDto> getAllWeeklyReports(@Context SecurityContext sc, @PathParam("since") long since) {
		return FacadeProvider.getWeeklyReportFacade().getAllWeeklyReportsAfter(new Date(since));
	}

	@POST
	@Path("/query")
	public List<WeeklyReportDto> getByUuids(@Context SecurityContext sc, List<String> uuids) {
		List<WeeklyReportDto> result = FacadeProvider.getWeeklyReportFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/push")
	public List<PushResult> postWeeklyReports(@Valid List<WeeklyReportDto> dtos) {
		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getWeeklyReportFacade()::saveWeeklyReport);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllUuids(@Context SecurityContext sc) {
		return FacadeProvider.getWeeklyReportFacade().getAllUuids();
	}
}
