package de.symeda.sormas.backend.campaign.data;

import de.symeda.sormas.backend.AbstractBeanTest;

public class CampaignFormMetaDataFacadeEjbTest extends AbstractBeanTest {

	// FIXME remove line comments once sonarcube quality gate is adjsuted
//	@Test
//	@Ignore("Remove ignore once we have replaced H2 - #2526")
//	public void testSaveCampaignFormData() throws Exception {
//
//		final TestDataCreator.RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
//		final UserDto user = creator.createUser(rdcf, UserRole.SURVEILLANCE_SUPERVISOR);
//		ObjectMapper mapper = new ObjectMapper();
//
//		final CampaignDto campaign = creator.createCampaign(user);
//
//		final CampaignFormMetaDto campaignForm = creator.createCampaignForm(campaign);
//
//		String formData = creator.getCampaignFormData();
//
//		CampaignFormDataDto newCampaignFormDataDto = creator.buildCampaignFormDataDto(campaign, campaignForm, rdcf, formData);
//		newCampaignFormDataDto = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);
//
//		assertNotNull(newCampaignFormDataDto);
//		assertEquals(Arrays.asList(mapper.readValue(formData, CampaignFormDataEntry[].class)), newCampaignFormDataDto.getFormValues());
//		assertEquals(rdcf.region.getUuid(), newCampaignFormDataDto.getRegion().getUuid());
//		assertEquals(rdcf.district.getUuid(), newCampaignFormDataDto.getDistrict().getUuid());
//		assertEquals(rdcf.community.getUuid(), newCampaignFormDataDto.getCommunity().getUuid());
//
//		String newFormData = "[{\"id\":\"teamNumber\",\"value\":\"12\"},{\"id\":\"namesOfTeamMembers\",\"value\":\"Ekkehard Rosin\"},"
//			+ "{\"id\":\"monitorName\",\"value\":\"Ralf Windisch\"},{\"id\":\"agencyName\",\"value\":\"Sormas Institut\"},{\"id\":\"orgaName\",\"value\":null}]";
//
//		newCampaignFormDataDto.setFormValues(Arrays.asList(mapper.readValue(newFormData, CampaignFormDataEntry[].class)));
//
//		CampaignFormDataDto updatedCampaignFormData = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);
//
//		assertNotNull(updatedCampaignFormData);
//		assertEquals(Arrays.asList(mapper.readValue(newFormData, CampaignFormDataEntry[].class)), updatedCampaignFormData.getFormValues());
//	}
//
//	@Test
//	@Ignore("Remove ignore once we have replaced H2 - #2526")
//	public void testGetCampaignFormDataByUuid() throws Exception {
//		final TestDataCreator.RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
//		final UserDto user = creator.createUser(rdcf, UserRole.SURVEILLANCE_SUPERVISOR);
//		ObjectMapper mapper = new ObjectMapper();
//
//		final CampaignDto campaign = creator.createCampaign(user);
//
//		final CampaignFormMetaDto campaignForm = creator.createCampaignForm(campaign);
//
//		String formData = creator.getCampaignFormData();
//
//		CampaignFormDataDto newCampaignFormDataDto = creator.buildCampaignFormDataDto(campaign, campaignForm, rdcf, formData);
//		newCampaignFormDataDto = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);
//
//		CampaignFormDataDto retrievedCampaignFormDataDto = getCampaignFormDataFacade().getCampaignFormDataByUuid(newCampaignFormDataDto.getUuid());
//
//		assertNotNull(retrievedCampaignFormDataDto);
//		assertEquals(Arrays.asList(mapper.readValue(formData, CampaignFormDataEntry[].class)), retrievedCampaignFormDataDto.getFormValues());
//	}
//
//	@Test
//	@Ignore("Remove ignore once we have replaced H2 - #2526")
//	public void testDeleteCampaignFormData() throws Exception {
//		final TestDataCreator.RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
//		final UserDto user = creator.createUser(rdcf, UserRole.SURVEILLANCE_SUPERVISOR);
//		ObjectMapper mapper = new ObjectMapper();
//
//		final CampaignDto campaign = creator.createCampaign(user);
//
//		final CampaignFormMetaDto campaignForm = creator.createCampaignForm(campaign);
//
//		String formData = creator.getCampaignFormData();
//
//		CampaignFormDataDto newCampaignFormDataDto = creator.buildCampaignFormDataDto(campaign, campaignForm, rdcf, formData);
//		newCampaignFormDataDto = getCampaignFormDataFacade().saveCampaignFormData(newCampaignFormDataDto);
//
//		assertNotNull(newCampaignFormDataDto);
//		assertEquals(Arrays.asList(mapper.readValue(formData, CampaignFormDataEntry[].class)), newCampaignFormDataDto.getFormValues());
//
//		getCampaignFormDataFacade().deleteCampaignFormData(newCampaignFormDataDto.getUuid());
//
//		CampaignFormDataDto deletedCampaignFormDataDto = getCampaignFormDataFacade().getCampaignFormDataByUuid(newCampaignFormDataDto.getUuid());
//
//		assertNull(deletedCampaignFormDataDto);
//	}
}
