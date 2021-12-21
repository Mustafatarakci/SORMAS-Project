package de.symeda.sormas.api.campaign;

import de.symeda.sormas.api.ReferenceDto;

public class CampaignReferenceDto extends ReferenceDto {

	public CampaignReferenceDto() {
	}

	public CampaignReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public CampaignReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}
}
