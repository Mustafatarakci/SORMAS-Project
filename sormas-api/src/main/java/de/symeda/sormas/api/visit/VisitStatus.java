package de.symeda.sormas.api.visit;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum VisitStatus {

	UNAVAILABLE,
	UNCOOPERATIVE,
	COOPERATIVE,;

	public String getName() {
		return this.name();
	}

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public String toShortString() {
		return I18nProperties.getEnumCaptionShort(this);
	}
}
