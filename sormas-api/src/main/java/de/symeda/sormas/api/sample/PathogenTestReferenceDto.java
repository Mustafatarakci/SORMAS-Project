package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.ReferenceDto;

public class PathogenTestReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -5213210080802372054L;

	public PathogenTestReferenceDto() {

	}

	public PathogenTestReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public PathogenTestReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}
}
