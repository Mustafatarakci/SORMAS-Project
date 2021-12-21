package de.symeda.sormas.api.task;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum TaskStatus {

	PENDING,
	DONE,
	REMOVED,
	NOT_EXECUTABLE;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
