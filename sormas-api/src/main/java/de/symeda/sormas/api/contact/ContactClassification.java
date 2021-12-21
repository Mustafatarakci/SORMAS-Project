package de.symeda.sormas.api.contact;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ContactClassification {

	UNCONFIRMED,
	CONFIRMED,
	NO_CONTACT;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public String toShortString() {
		return I18nProperties.getEnumCaptionShort(this);
	}
}
