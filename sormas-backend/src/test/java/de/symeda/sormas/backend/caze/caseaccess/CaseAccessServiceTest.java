/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.backend.caze.caseaccess;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.TestDataCreator;
import de.symeda.sormas.backend.access.AbstractEntityAccess;
import de.symeda.sormas.backend.access.AccessibleEntity;

public class CaseAccessServiceTest extends AbstractBeanTest {

	private TestDataCreator.RDCF rdcf1;
	private UserDto survOff1;
	private TestDataCreator.RDCF rdcf2;
	private UserDto survOff2;
	private TestDataCreator.RDCF rdcf3;
	private UserDto survOff3;

	@Before
	public void init() {
		super.init();
		rdcf1 = creator.createRDCF("reg1", "dis1", "com1", "fac1", "poe1");
		rdcf2 = creator.createRDCF("reg2", "dis2", "com2", "fac2", "poe2");
		rdcf3 = creator.createRDCF("reg3", "dis3", "com3", "fac3", "poe3");
		survOff1 = creator.createUser(rdcf1, "SurvOff", "Rdcf1", UserRole.SURVEILLANCE_OFFICER);
		survOff2 = creator.createUser(rdcf2, "SurvOff", "Rdcf2", UserRole.SURVEILLANCE_OFFICER);
		survOff3 = creator.createUser(rdcf3, "SurvOff", "Rdcf3", UserRole.SURVEILLANCE_OFFICER);
	}

	@Test
	public void testGetAccessibleCases() {
		CaseDataDto caseReg1 = creator.createCase(survOff1.toReference(), rdcf1, null);
		CaseDataDto caseReg1And2 = creator.createCase(survOff1.toReference(), rdcf2, caze -> {
			caze.setRegion(rdcf3.region);
			caze.setDistrict(rdcf3.district);
			caze.setHealthFacility(rdcf3.facility);
			caze.setPointOfEntry(rdcf3.pointOfEntry);
		});
		CaseDataDto caseReg1User3 = creator.createCase(survOff3.toReference(), rdcf1, null);
		CaseDataDto caseReg1User2 = creator.createCase(survOff1.toReference(), rdcf1, caze -> {
			caze.setSurveillanceOfficer(survOff2.toReference());
		});
		CaseDataDto caseReg3 = creator.createCase(survOff3.toReference(), rdcf3, null);

		List<AccessibleEntity> accessibleEntities = getCaseAccessService().getAccessibleCases(getUserService().getByUuid(survOff1.getUuid()));
		assertThat(accessibleEntities.size(), is(4));
		assertFalse(accessibleEntities.stream().anyMatch(e -> e.getId().equals(getCaseService().getIdByUuid(caseReg3.getUuid()))));
	}

	@Test
	public void testInsertAccessEntries() {

		// One entry containing responsible and assigned user, as well as the responsible jurisdiction
		creator.createCase(survOff1.toReference(), rdcf1, null);
		List<CaseAccess> caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(1));
		assertTrue(caseAccesses.get(0).isPrimaryData());

		// Two entries, one containing the different place of stay jurisdiction
		CaseDataDto caseDifferingJurisdictions = creator.createCase(survOff2.toReference(), rdcf2, caze -> {
			caze.setRegion(rdcf3.region);
			caze.setDistrict(rdcf3.district);
			caze.setHealthFacility(rdcf3.facility);
			caze.setPointOfEntry(rdcf3.pointOfEntry);
		});
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(3));

		// Make sure that facility and point of entry are stored in primary data
		caseAccesses.stream().filter(c -> c.getCaze().getUuid().equals(caseDifferingJurisdictions.getUuid())).forEach(c -> {
			if (caseDifferingJurisdictions.getResponsibleRegion().getUuid().equals(c.getRegion().getUuid())) {
				assertNotNull(c.getFacility());
				assertNotNull(c.getPointOfEntry());
			} else {
				assertNull(c.getFacility());
				assertNull(c.getPointOfEntry());
			}
		});
	}

	@Test
	public void testUpdateAccessEntries() {

		CaseDataDto caze = creator.createCase(survOff1.toReference(), rdcf1, null);
		List<CaseAccess> caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(1));
		assertThat(caseAccesses.get(0).getReportingUser().getUuid(), is(survOff1.getUuid()));

		caze.setReportingUser(survOff2.toReference());
		caze = getCaseFacade().saveCase(caze);
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(1));
		assertThat(caseAccesses.get(0).getReportingUser().getUuid(), is(survOff2.getUuid()));

		caze.setRegion(rdcf3.region);
		caze.setDistrict(rdcf3.district);
		caze.setHealthFacility(rdcf3.facility);
		caze.setPointOfEntry(rdcf3.pointOfEntry);
		caze = getCaseFacade().saveCase(caze);
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		CaseAccess primaryAccess = caseAccesses.stream().filter(AbstractEntityAccess::isPrimaryData).findFirst().orElseThrow(RuntimeException::new);
		CaseAccess secondaryAccess = caseAccesses.stream().filter(a -> !a.isPrimaryData()).findFirst().orElseThrow(RuntimeException::new);
		assertThat(primaryAccess.getRegion().getUuid(), is(rdcf1.region.getUuid()));
		assertThat(primaryAccess.getFacility().getUuid(), is(rdcf3.facility.getUuid()));
		assertThat(primaryAccess.getPointOfEntry().getUuid(), is(rdcf3.pointOfEntry.getUuid()));
		assertThat(secondaryAccess.getRegion().getUuid(), is(rdcf3.region.getUuid()));
		assertThat(secondaryAccess.getDistrict().getUuid(), is(rdcf3.district.getUuid()));
		assertNull(secondaryAccess.getFacility());
		assertNull(secondaryAccess.getPointOfEntry());

		caze.setRegion(rdcf2.region);
		caze.setDistrict(rdcf2.district);
		caze.setHealthFacility(rdcf2.facility);
		caze.setPointOfEntry(rdcf2.pointOfEntry);
		caze.setResponsibleRegion(rdcf3.region);
		caze.setResponsibleDistrict(rdcf3.district);
		caze.setResponsibleCommunity(rdcf3.community);
		caze = getCaseFacade().saveCase(caze);
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		primaryAccess = caseAccesses.stream().filter(AbstractEntityAccess::isPrimaryData).findFirst().orElseThrow(RuntimeException::new);
		secondaryAccess = caseAccesses.stream().filter(a -> !a.isPrimaryData()).findFirst().orElseThrow(RuntimeException::new);
		assertThat(primaryAccess.getRegion().getUuid(), is(rdcf3.region.getUuid()));
		assertThat(primaryAccess.getDistrict().getUuid(), is(rdcf3.district.getUuid()));
		assertThat(primaryAccess.getCommunity().getUuid(), is(rdcf3.community.getUuid()));
		assertThat(secondaryAccess.getRegion().getUuid(), is(rdcf2.region.getUuid()));
		assertThat(secondaryAccess.getDistrict().getUuid(), is(rdcf2.district.getUuid()));

		caze.setRegion(null);
		caze.setDistrict(null);
		caze.setHealthFacility(rdcf3.facility);
		caze.setPointOfEntry(rdcf3.pointOfEntry);
		getCaseFacade().saveCase(caze);
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		secondaryAccess = caseAccesses.stream().filter(a -> !a.isPrimaryData()).findFirst().orElseThrow(RuntimeException::new);
		assertNull(secondaryAccess.getRegion());
		assertNull(secondaryAccess.getDistrict());
	}

	@Test
	public void testDeleteEmptyAccessEntries() {

		CaseDataDto caze = creator.createCase(survOff1.toReference(), rdcf1, c -> {
			c.setRegion(rdcf2.region);
			c.setDistrict(rdcf2.district);
			c.setCommunity(rdcf2.community);
			c.setHealthFacility(rdcf2.facility);
			c.setPointOfEntry(null);
		});
		List<CaseAccess> caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));

		caze.setRegion(null);
		caze.setDistrict(null);
		caze.setCommunity(null);
		caze.setHealthFacility(rdcf1.facility);
		getCaseFacade().saveCase(caze);
		getCaseAccessService().deleteEmptyAccessEntries();
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(1));
	}

	@Test
	public void testInsertAccessEntryForContact() {

		CaseDataDto caze = creator.createCase(survOff1.toReference(), rdcf1, null);
		assertThat(getCaseAccessService().getAll().size(), is(1));

		creator.createContact(survOff1.toReference(), creator.createPerson().toReference(), caze, c -> {
			c.setContactOfficer(survOff2.toReference());
		});
		List<CaseAccess> caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		CaseAccess contactAccess = caseAccesses.stream().filter(a -> a.getContact() != null).findFirst().orElseThrow(RuntimeException::new);
		assertThat(contactAccess.getReportingUser().getUuid(), is(survOff1.getUuid()));
		assertThat(contactAccess.getAssignedUser().getUuid(), is(survOff2.getUuid()));

		creator.createContact(survOff1.toReference(), creator.createPerson().toReference(), caze, c -> {
			c.setRegion(rdcf2.region);
			c.setDistrict(rdcf2.district);
			c.setCommunity(rdcf2.community);
		});
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(3));
		List<CaseAccess> accessWithJurisdiction =
			caseAccesses.stream().filter(a -> a.getContact() != null && a.getRegion() != null).collect(Collectors.toList());
		assertThat(accessWithJurisdiction.size(), is(1));
		assertThat(accessWithJurisdiction.get(0).getRegion().getUuid(), is(rdcf2.region.getUuid()));
		assertThat(accessWithJurisdiction.get(0).getDistrict().getUuid(), is(rdcf2.district.getUuid()));
		assertThat(accessWithJurisdiction.get(0).getCommunity().getUuid(), is(rdcf2.community.getUuid()));
	}

	@Test
	public void testUpdateAccessEntryForContact() {

		CaseDataDto caze = creator.createCase(survOff1.toReference(), rdcf1, null);
		ContactDto contact = creator.createContact(survOff1.toReference(), creator.createPerson().toReference(), caze, c -> {
			c.setRegion(rdcf2.region);
			c.setDistrict(rdcf2.district);
			c.setCommunity(rdcf2.community);
		});
		assertThat(getCaseAccessService().getAll().size(), is(2));

		contact.setContactOfficer(survOff2.toReference());
		contact.setRegion(rdcf3.region);
		contact.setDistrict(rdcf3.district);
		contact.setCommunity(rdcf3.community);
		contact = getContactFacade().saveContact(contact);
		List<CaseAccess> caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		CaseAccess contactAccess = caseAccesses.stream().filter(a -> a.getContact() != null).findFirst().orElseThrow(RuntimeException::new);
		assertThat(contactAccess.getAssignedUser().getUuid(), is(survOff2.getUuid()));
		assertThat(contactAccess.getRegion().getUuid(), is(rdcf3.region.getUuid()));
		assertThat(contactAccess.getDistrict().getUuid(), is(rdcf3.district.getUuid()));
		assertThat(contactAccess.getCommunity().getUuid(), is(rdcf3.community.getUuid()));

		contact.setRegion(null);
		contact.setDistrict(null);
		contact.setCommunity(null);
		contact = getContactFacade().saveContact(contact);
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		contactAccess = caseAccesses.stream().filter(a -> a.getContact() != null).findFirst().orElseThrow(RuntimeException::new);
		assertNull(contactAccess.getRegion());
		assertNull(contactAccess.getDistrict());
		assertNull(contactAccess.getCommunity());

		CaseDataDto caze2 = creator.createCase(survOff2.toReference(), rdcf2, null);
		contact.setCaze(caze2.toReference());
		contact = getContactFacade().saveContact(contact);
		caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(3));
		assertThat(
			caseAccesses.stream().filter(a -> a.getContact() != null).findFirst().orElseThrow(RuntimeException::new).getCaze().getUuid(),
			is(caze2.getUuid()));

		contact.setDisease(Disease.EVD);
		contact.setCaze(null);
		contact.setRegion(rdcf1.region);
		contact.setDistrict(rdcf1.district);
		getContactFacade().saveContact(contact);
		assertThat(getCaseAccessService().getAll().size(), is(2));
	}

	@Test
	public void testInsertAccessEntryForSample() {

		CaseDataDto caze = creator.createCase(survOff1.toReference(), rdcf1, null);
		assertThat(getCaseAccessService().getAll().size(), is(1));

		creator.createSample(caze.toReference(), survOff1.toReference(), rdcf2.facility);
		List<CaseAccess> caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		CaseAccess sampleAccess = caseAccesses.stream().filter(a -> a.getSample() != null).findFirst().orElseThrow(RuntimeException::new);
		assertThat(sampleAccess.getFacility().getUuid(), is(rdcf2.facility.getUuid()));

		creator.createSample(caze.toReference(), survOff3.toReference(), rdcf3.facility);
		assertThat(getCaseAccessService().getAll().size(), is(3));
	}

	@Test
	public void testUpdateAccessEntryForSample() {

		CaseDataDto caze = creator.createCase(survOff1.toReference(), rdcf1, null);
		SampleDto sample = creator.createSample(caze.toReference(), survOff1.toReference(), rdcf2.facility);
		assertThat(getCaseAccessService().getAll().size(), is(2));

		sample.setLab(rdcf3.facility);
		getSampleFacade().saveSample(sample);
		List<CaseAccess> caseAccesses = getCaseAccessService().getAll();
		assertThat(caseAccesses.size(), is(2));
		CaseAccess sampleAccess = caseAccesses.stream().filter(a -> a.getSample() != null).findFirst().orElseThrow(RuntimeException::new);
		assertThat(sampleAccess.getFacility().getUuid(), is(rdcf3.facility.getUuid()));
	}
}
