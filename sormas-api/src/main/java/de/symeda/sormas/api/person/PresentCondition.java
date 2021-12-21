package de.symeda.sormas.api.person;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum PresentCondition {

	ALIVE,
	DEAD,
	BURIED,
	UNKNOWN;

	public boolean isDeceased() {
		return this == DEAD || this == BURIED;
	}

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
