package de.symeda.sormas.api.event;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum RiskLevel {

	LOW,
	MODERATE,
	HIGH,
	UNKNOWN;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
