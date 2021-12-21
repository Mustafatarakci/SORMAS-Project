package de.symeda.sormas.api.infrastructure.region;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.infrastructure.area.AreaReferenceDto;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;

public class RegionIndexDto extends EntityDto {

	private static final long serialVersionUID = -199144233786408125L;

	public static final String I18N_PREFIX = "Region";
	public static final String NAME = "name";
	public static final String EPID_CODE = "epidCode";
	public static final String POPULATION = "population";
	public static final String GROWTH_RATE = "growthRate";
	public static final String EXTERNAL_ID = "externalID";
	public static final String AREA = "area";
	public static final String COUNTRY = "country";

	private String name;
	private String epidCode;
	private Integer population;
	private Float growthRate;
	private String externalID;
	private AreaReferenceDto area;
	private CountryReferenceDto country;

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

	@Override
	public String toString() {
		return getName();
	}

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

	public Float getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(Float growthRate) {
		this.growthRate = growthRate;
	}

	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	public AreaReferenceDto getArea() {
		return area;
	}

	public void setArea(AreaReferenceDto area) {
		this.area = area;
	}

	public CountryReferenceDto getCountry() {
		return country;
	}

	public void setCountry(CountryReferenceDto country) {
		this.country = country;
	}

	public RegionReferenceDto toReference() {
		return new RegionReferenceDto(getUuid(), name, externalID);
	}

	public static RegionIndexDto build() {

		RegionIndexDto dto = new RegionIndexDto();
		dto.setUuid(DataHelper.createUuid());
		return dto;
	}
}
