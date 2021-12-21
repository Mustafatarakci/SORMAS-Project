package de.symeda.sormas.ui.dashboard.surveillance.components.epicurve;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum SurveillanceEpiCurveMode {

	CASE_STATUS,
	ALIVE_OR_DEAD;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
