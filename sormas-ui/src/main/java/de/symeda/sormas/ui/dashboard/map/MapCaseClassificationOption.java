package de.symeda.sormas.ui.dashboard.map;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum MapCaseClassificationOption {

	ALL_CASES,
	CONFIRMED_CASES_ONLY;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
