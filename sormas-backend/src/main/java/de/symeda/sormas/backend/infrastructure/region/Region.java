package de.symeda.sormas.backend.infrastructure.region;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import de.symeda.sormas.backend.common.InfrastructureAdo;
import de.symeda.sormas.backend.infrastructure.area.Area;
import de.symeda.sormas.backend.infrastructure.country.Country;
import de.symeda.sormas.backend.infrastructure.district.District;

@Entity
public class Region extends InfrastructureAdo {

	private static final long serialVersionUID = -2958216667876104358L;

	public static final String TABLE_NAME = "region";

	public static final String NAME = "name";
	public static final String EPID_CODE = "epidCode";
	public static final String DISTRICTS = "districts";
	public static final String GROWTH_RATE = "growthRate";
	public static final String EXTERNAL_ID = "externalID";
	public static final String AREA = "area";
	public static final String COUNTRY = "country";

	private String name;
	private String epidCode;
	private List<District> districts;
	private Float growthRate;
	private String externalID;
	private Area area;
	private Country country;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEpidCode() {
		return epidCode;
	}

	public void setEpidCode(String epidCode) {
		this.epidCode = epidCode;
	}

	@OneToMany(mappedBy = District.REGION, cascade = {}, fetch = FetchType.LAZY)
	@OrderBy(District.NAME)
	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

	@Override
	public String toString() {
		return getName();
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

	@ManyToOne
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@ManyToOne
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
