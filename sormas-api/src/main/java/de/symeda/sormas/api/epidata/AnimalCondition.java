package de.symeda.sormas.api.epidata;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AnimalCondition {

	ALIVE,
	DEAD,
	PROCESSED,
	UNKNOWN;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
