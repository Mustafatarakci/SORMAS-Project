/*
 * ******************************************************************************
 * * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * *
 * * This program is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.symeda.sormas.backend.campaign.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.symeda.sormas.api.campaign.CampaignDto;
import de.symeda.sormas.api.campaign.data.CampaignFormDataDto;
import de.symeda.sormas.api.campaign.data.CampaignFormDataEntry;
import de.symeda.sormas.api.campaign.form.CampaignFormMetaDto;
import de.symeda.sormas.api.user.DefaultUserRole;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.TestDataCreator;

public class CampaignFormMetaDataFacadeEjbTest extends AbstractBeanTest {

	@Test
	public void testSaveCampaignFormData() throws Exception {

		final TestDataCreator.RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
		final UserDto user = creator.createUser(rdcf, creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		ObjectMapper mapper = new ObjectMapper();

		final CampaignDto campaign = creator.createCampaign(user);

		final CampaignFormMetaDto campaignForm = creator.createCampaignForm(campaign);

		String formData = creator.getCampaignFormData();

		CampaignFormDataDto newCampaignFormDataDto = creator.buildCampaignFormDataDto(campaign, campaignForm, rdcf, formData);
		newCampaignFormDataDto = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);

		assertNotNull(newCampaignFormDataDto);
		assertEquals(Arrays.asList(mapper.readValue(formData, CampaignFormDataEntry[].class)), newCampaignFormDataDto.getFormValues());
		assertEquals(rdcf.region.getUuid(), newCampaignFormDataDto.getRegion().getUuid());
		assertEquals(rdcf.district.getUuid(), newCampaignFormDataDto.getDistrict().getUuid());
		assertEquals(rdcf.community.getUuid(), newCampaignFormDataDto.getCommunity().getUuid());

		String newFormData = "[{\"id\":\"teamNumber\",\"value\":\"12\"},{\"id\":\"namesOfTeamMembers\",\"value\":\"Ekkehard Rosin\"},"
			+ "{\"id\":\"monitorName\",\"value\":\"Ralf Windisch\"},{\"id\":\"agencyName\",\"value\":\"Sormas Institut\"},{\"id\":\"orgaName\",\"value\":null}]";
		String newFormDataWithoutNullValue = "[{\"id\":\"teamNumber\",\"value\":\"12\"},{\"id\":\"namesOfTeamMembers\",\"value\":\"Ekkehard Rosin\"},"
			+ "{\"id\":\"monitorName\",\"value\":\"Ralf Windisch\"},{\"id\":\"agencyName\",\"value\":\"Sormas Institut\"}]";

		newCampaignFormDataDto.setFormValues(Arrays.asList(mapper.readValue(newFormData, CampaignFormDataEntry[].class)));

		CampaignFormDataDto updatedCampaignFormData = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);

		assertNotNull(updatedCampaignFormData);
		assertEquals(
			Arrays.asList(mapper.readValue(newFormDataWithoutNullValue, CampaignFormDataEntry[].class)),
			updatedCampaignFormData.getFormValues());
	}

	@Test
	public void testGetCampaignFormDataByUuid() throws Exception {
		final TestDataCreator.RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
		final UserDto user = creator.createUser(rdcf, creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		ObjectMapper mapper = new ObjectMapper();

		final CampaignDto campaign = creator.createCampaign(user);

		final CampaignFormMetaDto campaignForm = creator.createCampaignForm(campaign);

		String formData = creator.getCampaignFormData();

		CampaignFormDataDto newCampaignFormDataDto = creator.buildCampaignFormDataDto(campaign, campaignForm, rdcf, formData);
		newCampaignFormDataDto = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);

		CampaignFormDataDto retrievedCampaignFormDataDto = getCampaignFormDataFacade().getCampaignFormDataByUuid(newCampaignFormDataDto.getUuid());

		assertNotNull(retrievedCampaignFormDataDto);
		assertEquals(Arrays.asList(mapper.readValue(formData, CampaignFormDataEntry[].class)), retrievedCampaignFormDataDto.getFormValues());
	}

	@Test
	public void testDeleteCampaignFormData() throws Exception {
		final TestDataCreator.RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
		final UserDto user = creator.createUser(rdcf, creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		ObjectMapper mapper = new ObjectMapper();

		final CampaignDto campaign = creator.createCampaign(user);

		final CampaignFormMetaDto campaignForm = creator.createCampaignForm(campaign);

		String formData = creator.getCampaignFormData();

		CampaignFormDataDto newCampaignFormDataDto = creator.buildCampaignFormDataDto(campaign, campaignForm, rdcf, formData);
		newCampaignFormDataDto = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);

		assertNotNull(newCampaignFormDataDto);
		assertEquals(Arrays.asList(mapper.readValue(formData, CampaignFormDataEntry[].class)), newCampaignFormDataDto.getFormValues());

		getCampaignFormDataFacade().deleteCampaignFormData(newCampaignFormDataDto.getUuid());

		CampaignFormDataDto deletedCampaignFormDataDto = getCampaignFormDataFacade().getCampaignFormDataByUuid(newCampaignFormDataDto.getUuid());

		assertNull(deletedCampaignFormDataDto);
	}
}
