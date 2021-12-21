package de.symeda.sormas.api.infrastructure.facility;

import java.io.Serializable;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class FacilityCriteria extends BaseCriteria implements Serializable, Cloneable {

	private static final long serialVersionUID = 3958619224286048978L;

	private CountryReferenceDto country;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private CommunityReferenceDto community;
	private String nameCityLike;
	private FacilityTypeGroup typeGroup;
	private FacilityType type;
	private EntityRelevanceStatus relevanceStatus;

	public CountryReferenceDto getCountry() {
		return country;
	}

	public FacilityCriteria country(CountryReferenceDto country) {
		this.country = country;

		return this;
	}

	public FacilityCriteria region(RegionReferenceDto region) {
		this.region = region;
		return this;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public FacilityCriteria district(DistrictReferenceDto district) {
		this.district = district;
		return this;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public FacilityCriteria community(CommunityReferenceDto community) {
		this.community = community;
		return this;
	}

	public CommunityReferenceDto getCommunity() {
		return community;
	}

	public FacilityCriteria typeGroup(FacilityTypeGroup typeGroup) {
		this.typeGroup = typeGroup;
		return this;
	}

	@IgnoreForUrl
	public FacilityTypeGroup getTypeGroup() {
		return typeGroup;
	}

	public FacilityCriteria type(FacilityType type) {
		this.type = type;
		return this;
	}

	@IgnoreForUrl
	public FacilityType getType() {
		return type;
	}

	public FacilityCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
		return this;
	}

	@IgnoreForUrl
	public EntityRelevanceStatus getRelevanceStatus() {
		return relevanceStatus;
	}

	@IgnoreForUrl
	public String getNameCityLike() {
		return nameCityLike;
	}

	public FacilityCriteria nameCityLike(String nameCityLike) {
		this.nameCityLike = nameCityLike;
		return this;
	}
}
