package de.symeda.sormas.api.infrastructure.district;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;

public class DistrictIndexDto extends EntityDto {

	private static final long serialVersionUID = -1445387465599056704L;
	public static final int CASE_INCIDENCE_DIVISOR = 100000;

	public static final String I18N_PREFIX = "District";

	public static final String NAME = "name";
	public static final String EPID_CODE = "epidCode";
	public static final String POPULATION = "population";
	public static final String GROWTH_RATE = "growthRate";
	public static final String REGION = "region";
	public static final String EXTERNAL_ID = "externalID";

	private String name;
	private String epidCode;
	private Integer population;
	private Float growthRate;
	private RegionReferenceDto region;
	private String externalID;

	public DistrictIndexDto() {
		super();
	}

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

	public RegionReferenceDto getRegion() {
		return region;
	}

	public void setRegion(RegionReferenceDto region) {
		this.region = region;
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

	public DistrictReferenceDto toReference() {
		return new DistrictReferenceDto(getUuid(), name, externalID);
	}

	public static DistrictIndexDto build() {
		DistrictIndexDto dto = new DistrictIndexDto();
		dto.setUuid(DataHelper.createUuid());
		return dto;
	}
}
