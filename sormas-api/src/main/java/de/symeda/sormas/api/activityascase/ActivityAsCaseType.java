package de.symeda.sormas.api.activityascase;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.HideForCountries;
import de.symeda.sormas.api.utils.HideForCountriesExcept;

public enum ActivityAsCaseType {

	WORK,
	@HideForCountries
	TRAVEL,
	@HideForCountries
	SPORT,
	@HideForCountries
	VISIT,
	@HideForCountries
	GATHERING,
	HABITATION,
	@HideForCountries
	PERSONAL_SERVICES,
	@HideForCountriesExcept
	CARED_FOR,
	OTHER,
	UNKNOWN;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

}
