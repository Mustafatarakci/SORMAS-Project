package de.symeda.sormas.api.clinicalcourse;

import de.symeda.sormas.api.ReferenceDto;

public class ClinicalCourseReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -2664896907352864261L;

	public ClinicalCourseReferenceDto() {

	}

	public ClinicalCourseReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public ClinicalCourseReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}
}
