package de.symeda.sormas.api.infrastructure.facility;

import de.symeda.sormas.api.InfrastructureDataReferenceDto;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;

public class FacilityReferenceDto extends InfrastructureDataReferenceDto implements StatisticsGroupingKey {

	private static final long serialVersionUID = -7987228795475507196L;

	public FacilityReferenceDto() {
	}

	public FacilityReferenceDto(String uuid) {
		super(uuid);
	}

	public FacilityReferenceDto(String uuid, String caption, String externalId) {
		super(uuid, caption, externalId);
	}

	@Override
	public int keyCompareTo(StatisticsGroupingKey o) {
		if (o == null) {
			throw new NullPointerException("Can't compare to null.");
		}

		if (this.equals(o)) {
			return 0;
		}
		int captionComparison = this.getCaption().compareTo(((FacilityReferenceDto) o).getCaption());
		if (captionComparison != 0) {
			return captionComparison;
		} else {
			return this.getUuid().compareTo(((FacilityReferenceDto) o).getUuid());
		}
	}
}
