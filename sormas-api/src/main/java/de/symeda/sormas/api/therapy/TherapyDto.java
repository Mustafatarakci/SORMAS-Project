package de.symeda.sormas.api.therapy;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.utils.DataHelper;

public class TherapyDto extends EntityDto {

	private static final long serialVersionUID = -1467303502817738376L;

	public static final String I18N_PREFIX = "Therapy";

	public static TherapyDto build() {

		TherapyDto therapy = new TherapyDto();
		therapy.setUuid(DataHelper.createUuid());
		return therapy;
	}

	public TherapyReferenceDto toReference() {
		return new TherapyReferenceDto(getUuid());
	}
}
