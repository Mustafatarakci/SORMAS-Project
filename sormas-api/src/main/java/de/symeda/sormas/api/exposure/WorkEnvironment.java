package de.symeda.sormas.api.exposure;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum WorkEnvironment {

	UNKNOWN,
	OPEN_SPACE_OFFICE,
	FOOD_SECTOR,
	BUILDING_SECTOR,
	LOGISTICS_CENTER,
	OTHER;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
