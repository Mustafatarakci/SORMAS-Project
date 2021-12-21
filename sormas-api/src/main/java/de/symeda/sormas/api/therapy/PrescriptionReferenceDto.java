package de.symeda.sormas.api.therapy;

import de.symeda.sormas.api.ReferenceDto;

public class PrescriptionReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -5028702472324192079L;

	public PrescriptionReferenceDto() {

	}

	public PrescriptionReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public PrescriptionReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}
}
