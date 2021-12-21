package de.symeda.sormas.api;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum CaseMeasure {

	/**
	 * Number of cases
	 */
	CASE_COUNT,
	/**
	 * Number of cases per DistrictDto.CASE_INCIDENCE_DIVISOR; rounded to two decimal places with rounding mode half up
	 */
	CASE_INCIDENCE;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	};
}
