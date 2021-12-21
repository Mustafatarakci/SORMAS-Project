package de.symeda.sormas.api.event;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum EventStatus {

	SIGNAL,
	EVENT,
	SCREENING,
	CLUSTER,
	DROPPED;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public String toShortString() {
		return I18nProperties.getEnumCaptionShort(this);
	}
}
