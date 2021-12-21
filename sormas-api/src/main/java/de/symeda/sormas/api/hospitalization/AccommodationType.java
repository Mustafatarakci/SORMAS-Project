package de.symeda.sormas.api.hospitalization;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AccommodationType {

	WARD,
	ICU;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
