package de.symeda.sormas.api.infrastructure.district;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.GeoLocationFacade;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface DistrictFacade extends GeoLocationFacade<DistrictDto, DistrictIndexDto, DistrictReferenceDto, DistrictCriteria> {

	List<DistrictReferenceDto> getAllActiveByArea(String areaUuid);

	List<DistrictReferenceDto> getAllActiveByRegion(String regionUuid);

	int getCountByRegion(String regionUuid);

	Page<DistrictIndexDto> getIndexPage(DistrictCriteria districtCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	DistrictReferenceDto getDistrictReferenceById(long id);

	List<DistrictReferenceDto> getAllActiveAsReference();

	List<DistrictReferenceDto> getByName(String name, RegionReferenceDto regionRef, boolean includeArchivedEntities);

	List<String> getNamesByIds(List<Long> districtIds);

	String getFullEpidCodeForDistrict(String districtUuid);

	boolean isUsedInOtherInfrastructureData(Collection<String> districtUuids);

	boolean hasArchivedParentInfrastructure(Collection<String> districtUuids);

	Map<String, String> getRegionUuidsForDistricts(List<DistrictReferenceDto> districts);
}
