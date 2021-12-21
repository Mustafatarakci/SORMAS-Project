package de.symeda.sormas.api.infrastructure.community;

import de.symeda.sormas.api.InfrastructureDataReferenceDto;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;

public class CommunityReferenceDto extends InfrastructureDataReferenceDto implements StatisticsGroupingKey {

	private static final long serialVersionUID = -8833267932522978860L;

	public CommunityReferenceDto() {
		super();
	}

	public CommunityReferenceDto(String uuid) {
		super(uuid);
	}

	public CommunityReferenceDto(String uuid, String caption, String externalId) {
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
		int captionComparison = this.getCaption().compareTo(((CommunityReferenceDto) o).getCaption());
		if (captionComparison != 0) {
			return captionComparison;
		} else {
			return this.getUuid().compareTo(((CommunityReferenceDto) o).getUuid());
		}
	}
}
