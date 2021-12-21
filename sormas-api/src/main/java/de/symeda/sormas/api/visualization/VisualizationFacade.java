package de.symeda.sormas.api.visualization;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Remote;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;

@Remote
public interface VisualizationFacade {

	String buildTransmissionChainJson(
		Date fromDate,
		Date toDate,
		RegionReferenceDto region,
		DistrictReferenceDto district,
		Collection<Disease> diseases,
		Language language);

	Long getContactCount(Date fromDate, Date toDate, RegionReferenceDto region, DistrictReferenceDto district, Collection<Disease> diseases);
}
