package de.symeda.sormas.backend.infrastructure.community;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.symeda.sormas.backend.common.InfrastructureAdo;
import de.symeda.sormas.backend.infrastructure.district.District;

@Entity
public class Community extends InfrastructureAdo {

	private static final long serialVersionUID = 1971053920357795693L;

	public static final String TABLE_NAME = "community";

	public static final String NAME = "name";
	public static final String DISTRICT = "district";
	public static final String GROWTH_RATE = "growthRate";
	public static final String EXTERNAL_ID = "externalID";

	private String name;
	private District district;
	private Float growthRate;
	private String externalID;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(nullable = false)
	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
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

	@Override
	public String toString() {
		return getName();
	}
}
