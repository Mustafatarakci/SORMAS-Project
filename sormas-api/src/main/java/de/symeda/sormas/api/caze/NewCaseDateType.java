package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.criteria.CriteriaDateType;

/**
 * Contains the different types of date that are used to determine the start date of a case.
 * Normally, symptom onset (ONSET) date is considered first, then case report (REPORT) date.
 */
public enum NewCaseDateType
	implements
	CriteriaDateType {

	MOST_RELEVANT,
	ONSET,
	REPORT;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
