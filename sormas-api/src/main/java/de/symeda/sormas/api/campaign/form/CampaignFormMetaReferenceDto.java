package de.symeda.sormas.api.campaign.form;

import de.symeda.sormas.api.ReferenceDto;

public class CampaignFormMetaReferenceDto extends ReferenceDto {

	public CampaignFormMetaReferenceDto() {
	}

	public CampaignFormMetaReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public CampaignFormMetaReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}
}
