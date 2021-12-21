package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum VaccinationInfoSource {

	VACCINATION_CARD,
	ORAL_COMMUNICATION,
	NO_EVIDENCE,
	UNKNOWN;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
