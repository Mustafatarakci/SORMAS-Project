/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.backend.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventInvestigationStatus;
import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.api.user.DefaultUserRole;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.MockProducer;
import de.symeda.sormas.backend.TestDataCreator;

@RunWith(MockitoJUnitRunner.class)
public class EventFacadeEjbPseudonymizationTest extends AbstractBeanTest {

	private TestDataCreator.RDCF rdcf1;
	private TestDataCreator.RDCF rdcf2;
	private UserDto user1;
	private UserDto user2;
	private UserDto observerUser;

	@Override
	public void init() {

		super.init();

		rdcf1 = creator.createRDCF("Region 1", "District 1", "Community 1", "Facility 1", "Point of entry 1");
		user1 = creator.createUser(
			rdcf1.region.getUuid(),
			rdcf1.district.getUuid(),
			rdcf1.facility.getUuid(),
			"Surv",
			"Off1",
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_OFFICER));

		rdcf2 = creator.createRDCF("Region 2", "District 2", "Community 2", "Facility 2", "Point of entry 2");
		user2 = creator.createUser(
			rdcf2.region.getUuid(),
			rdcf2.district.getUuid(),
			rdcf2.facility.getUuid(),
			"Surv",
			"Off2",
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_OFFICER));

		observerUser = creator
			.createUser(null, null, null, null, "National", "Observer", creator.getUserRoleReference(DefaultUserRole.NATIONAL_OBSERVER));

		when(MockProducer.getPrincipal().getName()).thenReturn("SurvOff2");
	}

	@Test
	public void testEventInJurisdiction() {
		EventDto event = createEvent(user2, rdcf2);

		assertNotPseudonymized(getEventFacade().getEventByUuid(event.getUuid(), false));
	}

	@Test
	public void testEventOutsideJurisdiction() {
		EventDto event = createEvent(user1, rdcf1);

		assertPseudonymized(getEventFacade().getEventByUuid(event.getUuid(), false));
	}

	@Test
	public void testUpdatePseudonymizedEvent() {
		EventDto event = createEvent(user2, rdcf2);

		loginWith(observerUser);

		event.setConnectionNumber("updated");
		event.setResponsibleUser(null);
		getEventFacade().save(event);

		Event savedEvent = getEventService().getByUuid(event.getUuid());

		assertThat(savedEvent.getConnectionNumber(), is("Connect No."));
		assertThat(savedEvent.getResponsibleUser().getUuid(), is(user2.getUuid()));
	}

	@Test
	public void testUpdateWithPseudonymizedDto() {
		EventDto event = createEvent(user2, rdcf2);

		event.setPseudonymized(true);
		event.setResponsibleUser(null);
		getEventFacade().save(event);

		Event savedEvent = getEventService().getByUuid(event.getUuid());

		assertThat(savedEvent.getResponsibleUser().getUuid(), is(user2.getUuid()));
	}

	@Test
	public void testPseudonymizeGetByUuids() {
		EventDto event1 = createEvent(user2, rdcf2);
		EventDto event2 = createEvent(user1, rdcf1);

		List<EventDto> events = getEventFacade().getByUuids(Arrays.asList(event1.getUuid(), event2.getUuid()));

		assertNotPseudonymized(events.stream().filter(e -> e.getUuid().equals(event1.getUuid())).findFirst().get());
		assertPseudonymized(events.stream().filter(e -> e.getUuid().equals(event2.getUuid())).findFirst().get());
	}

	private EventDto createEvent(UserDto user, TestDataCreator.RDCF rdcf) {
		return creator.createEvent(EventStatus.SIGNAL, EventInvestigationStatus.PENDING, "Test title", "Test Description", user.toReference(), e -> {
			e.setResponsibleUser(user.toReference());
			e.setConnectionNumber("Connect No.");
		});
	}

	private void assertNotPseudonymized(EventDto event) {
		assertThat(event.getResponsibleUser(), is(user2));
	}

	private void assertPseudonymized(EventDto event) {
		assertThat(event.getResponsibleUser(), is(nullValue()));
	}
}
