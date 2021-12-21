package de.symeda.sormas.backend.infrastructure.facility;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;

public class FacilityServiceTest extends AbstractBeanTest {

	@Override
	public void init() {
		getFacilityService().createConstantFacilities();
	}

	@Test
	public void testSpecialFacilitiesExist() {

		FacilityService facilityService = getBean(FacilityService.class);
		Facility otherFacility = facilityService.getByUuid(FacilityDto.OTHER_FACILITY_UUID);
		assertNotNull(otherFacility);
		Facility noneFacility = facilityService.getByUuid(FacilityDto.NONE_FACILITY_UUID);
		assertNotNull(noneFacility);
	}

	@Test
	public void testGetHealthFacilitiesByName() {

		Region region = creator.createRegion("Region");
		District district = creator.createDistrict("District", region);
		District otherDistrict = creator.createDistrict("Other District", region);
		Community community = creator.createCommunity("Community", district);
		Community otherCommunity = creator.createCommunity("Other Community", otherDistrict);
		creator.createFacility("Facility", region, district, community);

		assertThat(getFacilityService().getFacilitiesByNameAndType("Facility", district, community, null, true), hasSize(1));
		assertThat(getFacilityService().getFacilitiesByNameAndType(" Facility ", district, community, null, true), hasSize(1));
		assertThat(getFacilityService().getFacilitiesByNameAndType("facility", district, null, null, true), hasSize(1));
		assertThat(getFacilityService().getFacilitiesByNameAndType("FACILITY", district, null, null, true), hasSize(1));
		assertThat(getFacilityService().getFacilitiesByNameAndType("Facility", otherDistrict, otherCommunity, null, true), empty());
		assertThat(getFacilityService().getFacilitiesByNameAndType("Redcliffe Church", district, community, null, true), empty());
	}
}
