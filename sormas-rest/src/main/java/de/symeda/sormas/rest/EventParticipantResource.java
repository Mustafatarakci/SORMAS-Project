/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.rest;

import java.util.Date;
import java.util.List;

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
import de.symeda.sormas.api.event.EventParticipantCriteria;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.event.EventParticipantIndexDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * @see <a href="https://jersey.java.net/documentation/latest/">Jersey
 *      documentation</a>
 * @see <a href=
 *      "https://jersey.java.net/documentation/latest/jaxrs-resources.html#d0e2051">Jersey
 *      documentation HTTP Methods</a>
 */
@Path("/eventparticipants")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class EventParticipantResource extends EntityDtoResource {

	@GET
	@Path("/all/{since}")
	public List<EventParticipantDto> getAllEventParticipantsAfter(@PathParam("since") long since) {
		List<EventParticipantDto> result = FacadeProvider.getEventParticipantFacade().getAllActiveEventParticipantsAfter(new Date(since));
		return result;
	}

	@GET
	@Path("/all/{since}/{size}/{lastSynchronizedUuid}")
	public List<EventParticipantDto> getAllEventParticipantsAfter(
		@PathParam("since") long since,
		@PathParam("size") int size,
		@PathParam("lastSynchronizedUuid") String lastSynchronizedUuid) {
		List<EventParticipantDto> result =
			FacadeProvider.getEventParticipantFacade().getAllActiveEventParticipantsAfter(new Date(since), size, lastSynchronizedUuid);
		return result;
	}

	@GET
	@Path("/{uuid}")
	public EventParticipantDto getByUuid(@PathParam("uuid") String uuid) {
		return FacadeProvider.getEventParticipantFacade().getByUuid(uuid);
	}

	@POST
	@Path("/query")
	public List<EventParticipantDto> getByUuids(List<String> uuids) {
		List<EventParticipantDto> result = FacadeProvider.getEventParticipantFacade().getByUuids(uuids);
		return result;
	}

	@POST
	@Path("/query/events")
	public List<EventParticipantDto> getByEventUuids(List<String> uuids) {
		List<EventParticipantDto> result = FacadeProvider.getEventParticipantFacade().getByEventUuids(uuids);
		return result;
	}

	@POST
	@Path("/query/persons")
	public List<EventParticipantDto> getByPersonUuids(List<String> uuids) {
		return FacadeProvider.getEventParticipantFacade().getByPersonUuids(uuids);
	}

	@POST
	@Path("/push")
	public List<PushResult> postEventParticipants(@Valid List<EventParticipantDto> dtos) {

		List<PushResult> result = savePushedDto(dtos, FacadeProvider.getEventParticipantFacade()::save);
		return result;
	}

	@GET
	@Path("/uuids")
	public List<String> getAllActiveUuids() {
		return FacadeProvider.getEventParticipantFacade().getAllActiveUuids();
	}

	@POST
	@Path("/indexList")
	public Page<EventParticipantIndexDto> getIndexList(
		@RequestBody CriteriaWithSorting<EventParticipantCriteria> criteriaWithSorting,
		@QueryParam("offset") int offset,
		@QueryParam("size") int size) {
		return FacadeProvider.getEventParticipantFacade()
			.getIndexPage(criteriaWithSorting.getCriteria(), offset, size, criteriaWithSorting.getSortProperties());
	}

	@GET
	@Path("/archived/{since}")
	public List<String> getArchivedUuidsSince(@PathParam("since") long since) {
		return FacadeProvider.getEventParticipantFacade().getArchivedUuidsSince(new Date(since));
	}

	@GET
	@Path("/deleted/{since}")
	public List<String> getDeletedUuidsSince(@PathParam("since") long since) {
		return FacadeProvider.getEventParticipantFacade().getDeletedUuidsSince(new Date(since));
	}

	@GET
	@Path("/obsolete/{since}")
	public List<String> getObsoleteUuidsSince(@PathParam("since") long since) {
		return FacadeProvider.getEventParticipantFacade().getObsoleteUuidsSince(new Date(since));
	}
}
