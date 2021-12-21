package de.symeda.sormas.api.infrastructure.district;

import de.symeda.sormas.api.InfrastructureDataReferenceDto;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;

public class DistrictReferenceDto extends InfrastructureDataReferenceDto implements StatisticsGroupingKey {

	private static final long serialVersionUID = 8990957700033431836L;

	public DistrictReferenceDto() {
	}

	public DistrictReferenceDto(String uuid) {
		super(uuid);
	}

	public DistrictReferenceDto(String uuid, String caption, String externalId) {
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
		int captionComparison = this.getCaption().compareTo(((DistrictReferenceDto) o).getCaption());
		if (captionComparison != 0) {
			return captionComparison;
		} else {
			return this.getUuid().compareTo(((DistrictReferenceDto) o).getUuid());
		}
	}
}
