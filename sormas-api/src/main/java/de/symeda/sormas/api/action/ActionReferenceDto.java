package de.symeda.sormas.api.action;

import de.symeda.sormas.api.ReferenceDto;

public class ActionReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = 2430932452606853497L;

	public ActionReferenceDto() {

	}

	public ActionReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public ActionReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}

	@Override
	public String getCaption() {
		return super.getCaption();
	}
}
