package de.symeda.sormas.api.outbreak;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface OutbreakFacade {

	List<String> getActiveUuidsAfter(Date date);

	List<String> getInactiveUuidsAfter(Date date);

	List<OutbreakDto> getActiveAfter(Date date);

	List<OutbreakDto> getActive(OutbreakCriteria criteria);

	List<OutbreakDto> getActiveByRegionAndDisease(RegionReferenceDto region, Disease disease);

	OutbreakDto getActiveByDistrictAndDisease(DistrictReferenceDto district, Disease disease);

	Page<OutbreakDto> getIndexPage(OutbreakCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	boolean hasOutbreak(DistrictReferenceDto district, Disease disease);

	OutbreakDto saveOutbreak(@Valid OutbreakDto outbreakDto);

	void deleteOutbreak(OutbreakDto outbreakDto);

	/**
	 * @return The freshly started outbreak or an existing one if already started
	 */
	OutbreakDto startOutbreak(DistrictReferenceDto district, Disease disease);

	/**
	 * @return The ended outbreak or null if none was active
	 */
	OutbreakDto endOutbreak(DistrictReferenceDto district, Disease disease);

	Map<Disease, Long> getOutbreakDistrictCountByDisease(OutbreakCriteria criteria);

	Long getOutbreakDistrictCount(OutbreakCriteria criteria);
}
