package de.symeda.sormas.backend.infrastructure.district;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import de.symeda.sormas.backend.common.InfrastructureAdo;
import de.symeda.sormas.backend.feature.FeatureConfiguration;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.region.Region;

@Entity
public class District extends InfrastructureAdo {

	private static final long serialVersionUID = -6057113756091470463L;

	public static final String TABLE_NAME = "district";

	public static final String NAME = "name";
	public static final String REGION = "region";
	public static final String EPID_CODE = "epidCode";
	public static final String COMMUNITIES = "communities";
	public static final String GROWTH_RATE = "growthRate";
	public static final String EXTERNAL_ID = "externalID";
	public static final String FEATURE_CONFIGURATIONS = "featureConfigurations";

	private String name;
	private Region region;
	private String epidCode;
	private List<Community> communities;
	private Float growthRate;
	private String externalID;

	private List<FeatureConfiguration> featureConfigurations;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(nullable = false)
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getEpidCode() {
		return epidCode;
	}

	public void setEpidCode(String epidCode) {
		this.epidCode = epidCode;
	}

	@OneToMany(mappedBy = Community.DISTRICT, cascade = {}, fetch = FetchType.LAZY)
	@OrderBy(District.NAME)
	public List<Community> getCommunities() {
		return communities;
	}

	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}

	public Float getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(Float growthRate) {
		this.growthRate = growthRate;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	@OneToMany(mappedBy = FeatureConfiguration.DISTRICT, fetch = FetchType.LAZY)
	public List<FeatureConfiguration> getFeatureConfigurations() {
		return featureConfigurations;
	}

	public void setFeatureConfigurations(List<FeatureConfiguration> featureConfigurations) {
		this.featureConfigurations = featureConfigurations;
	}

	@Override
	public String toString() {
		return getName();
	}
}
