package de.symeda.sormas.api.infrastructure.community;

import java.io.Serializable;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class CommunityCriteria extends BaseCriteria implements Serializable, Cloneable {

	private static final long serialVersionUID = 7815180508529134182L;

	private CountryReferenceDto country;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private String nameLike;
	private EntityRelevanceStatus relevanceStatus;

	public CountryReferenceDto getCountry() {
		return country;
	}

	public CommunityCriteria country(CountryReferenceDto country) {
		this.country = country;

		return this;
	}

	public CommunityCriteria region(RegionReferenceDto region) {
		this.region = region;
		return this;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public CommunityCriteria district(DistrictReferenceDto district) {
		this.district = district;
		return this;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	@IgnoreForUrl
	public String getNameLike() {
		return nameLike;
	}

	public CommunityCriteria nameLike(String nameLike) {
		this.nameLike = nameLike;
		return this;
	}

	public CommunityCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
		return this;
	}

	@IgnoreForUrl
	public EntityRelevanceStatus getRelevanceStatus() {
		return relevanceStatus;
	}
}
