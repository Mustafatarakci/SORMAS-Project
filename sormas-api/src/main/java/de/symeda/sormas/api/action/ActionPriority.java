package de.symeda.sormas.api.action;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ActionPriority {

	HIGH,
	NORMAL,
	LOW;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
