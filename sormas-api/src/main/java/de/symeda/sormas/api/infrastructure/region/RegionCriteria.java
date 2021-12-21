package de.symeda.sormas.api.infrastructure.region;

import java.io.Serializable;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class RegionCriteria extends BaseCriteria implements Serializable, Cloneable {

	private static final long serialVersionUID = 5249729838631831239L;

	private String nameEpidLike;
	private EntityRelevanceStatus relevanceStatus;
	private CountryReferenceDto country;

	@IgnoreForUrl
	public String getNameEpidLike() {
		return nameEpidLike;
	}

	public RegionCriteria nameEpidLike(String nameEpidLike) {
		this.nameEpidLike = nameEpidLike;
		return this;
	}

	public RegionCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
		return this;
	}

	@IgnoreForUrl
	public EntityRelevanceStatus getRelevanceStatus() {
		return relevanceStatus;
	}

	public CountryReferenceDto getCountry() {
		return country;
	}

	public RegionCriteria country(CountryReferenceDto country) {
		this.country = country;

		return this;
	}
}
