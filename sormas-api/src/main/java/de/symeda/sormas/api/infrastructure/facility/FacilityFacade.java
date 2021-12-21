package de.symeda.sormas.api.infrastructure.facility;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.InfrastructureBaseFacade;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface FacilityFacade extends InfrastructureBaseFacade<FacilityDto, FacilityIndexDto, FacilityReferenceDto, FacilityCriteria> {

	List<FacilityReferenceDto> getActiveFacilitiesByCommunityAndType(
		CommunityReferenceDto community,
		FacilityType type,
		boolean includeOtherFacility,
		boolean includeNoneFacility);

	List<FacilityReferenceDto> getActiveFacilitiesByDistrictAndType(
		DistrictReferenceDto district,
		FacilityType type,
		boolean includeOtherFacility,
		boolean includeNoneFacility);

	List<FacilityReferenceDto> getActiveHospitalsByCommunity(CommunityReferenceDto community, boolean includeOtherFacility);

	List<FacilityReferenceDto> getActiveHospitalsByDistrict(DistrictReferenceDto district, boolean includeOtherFacility);

	List<FacilityReferenceDto> getAllActiveLaboratories(boolean includeOtherFacility);

	List<FacilityDto> getAllByRegionAfter(String regionUuid, Date date);

	List<FacilityDto> getAllWithoutRegionAfter(Date date);

	FacilityReferenceDto getFacilityReferenceById(long id);

	List<FacilityReferenceDto> getByNameAndType(
		String name,
		DistrictReferenceDto districtRef,
		CommunityReferenceDto communityRef,
		FacilityType type,
		boolean includeArchivedEntities);

	List<FacilityReferenceDto> getLaboratoriesByName(String name, boolean includeArchivedEntities);

	boolean hasArchivedParentInfrastructure(Collection<String> facilityUuids);

	Map<String, String> getDistrictUuidsForFacilities(List<FacilityReferenceDto> facilities);

	Map<String, String> getCommunityUuidsForFacilities(List<FacilityReferenceDto> facilities);

	List<FacilityReferenceDto> getByExternalIdAndType(String id, FacilityType type, boolean includeArchivedEntities);

	Page<FacilityIndexDto> getIndexPage(FacilityCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<FacilityExportDto> getExportList(FacilityCriteria facilityCriteria, Integer first, Integer max);
}
