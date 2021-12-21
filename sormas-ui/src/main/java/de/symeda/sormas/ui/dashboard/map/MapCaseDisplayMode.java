package de.symeda.sormas.ui.dashboard.map;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum MapCaseDisplayMode {

	CASE_ADDRESS,
	FACILITY_OR_CASE_ADDRESS,
	FACILITY;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
