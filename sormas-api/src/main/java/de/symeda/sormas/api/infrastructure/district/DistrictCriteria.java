package de.symeda.sormas.api.infrastructure.district;

import java.io.Serializable;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class DistrictCriteria extends BaseCriteria implements Serializable, Cloneable {

	private static final long serialVersionUID = -1794892073657582900L;

	private CountryReferenceDto country;
	private RegionReferenceDto region;
	private String nameEpidLike;
	private EntityRelevanceStatus relevanceStatus;

	public CountryReferenceDto getCountry() {
		return country;
	}

	public DistrictCriteria country(CountryReferenceDto country) {
		this.country = country;

		return this;
	}

	public DistrictCriteria region(RegionReferenceDto region) {
		this.region = region;
		return this;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	@IgnoreForUrl
	public String getNameEpidLike() {
		return nameEpidLike;
	}

	public DistrictCriteria nameEpidLike(String nameEpidLike) {
		this.nameEpidLike = nameEpidLike;
		return this;
	}

	public DistrictCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
		return this;
	}

	@IgnoreForUrl
	public EntityRelevanceStatus getRelevanceStatus() {
		return relevanceStatus;
	}
}
