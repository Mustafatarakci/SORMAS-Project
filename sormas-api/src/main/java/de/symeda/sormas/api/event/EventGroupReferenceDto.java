package de.symeda.sormas.api.event;

import de.symeda.sormas.api.ReferenceDto;

public class EventGroupReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = 2430932452606853497L;

	public EventGroupReferenceDto() {

	}

	public EventGroupReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public EventGroupReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}

	@Override
	public String getCaption() {
		return super.getCaption();
	}

}
