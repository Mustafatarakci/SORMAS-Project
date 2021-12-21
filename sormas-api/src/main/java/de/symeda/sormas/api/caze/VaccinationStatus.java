package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum VaccinationStatus {

	VACCINATED,
	UNVACCINATED,
	UNKNOWN;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
