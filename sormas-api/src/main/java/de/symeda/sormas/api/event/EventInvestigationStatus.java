package de.symeda.sormas.api.event;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum EventInvestigationStatus {

	PENDING,
	ONGOING,
	DONE,
	DISCARDED;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public String toShortString() {
		return I18nProperties.getEnumCaptionShort(this);
	}
}
