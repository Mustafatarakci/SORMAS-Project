package de.symeda.sormas.api.infrastructure.region;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.GeoLocationFacade;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface RegionFacade extends GeoLocationFacade<RegionDto, RegionIndexDto, RegionReferenceDto, RegionCriteria> {

	List<RegionReferenceDto> getAllActiveByServerCountry();

	List<RegionReferenceDto> getAllActiveByCountry(String countryUuid);

	List<RegionReferenceDto> getAllActiveByArea(String areaUuid);

	List<RegionReferenceDto> getAllActiveAsReference();

	Page<RegionIndexDto> getIndexPage(RegionCriteria regionCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);


	RegionReferenceDto getRegionReferenceById(int id);

	List<RegionDto> getByName(String name, boolean includeArchivedEntities);

	List<String> getNamesByIds(List<Long> regionIds);

	boolean isUsedInOtherInfrastructureData(Collection<String> regionUuids);
}
