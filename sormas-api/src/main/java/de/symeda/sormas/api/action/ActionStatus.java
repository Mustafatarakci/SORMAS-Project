package de.symeda.sormas.api.action;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ActionStatus {

	PENDING,
	IN_PROGRESS,
	DONE;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
