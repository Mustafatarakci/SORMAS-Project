package de.symeda.sormas.api.statistics;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum StatisticsCaseSubAttribute {

	YEAR(true, true),
	QUARTER(true, true),
	MONTH(true, true),
	EPI_WEEK(true, true),
	QUARTER_OF_YEAR(true, true),
	MONTH_OF_YEAR(true, true),
	EPI_WEEK_OF_YEAR(true, true),
	DATE_RANGE(true, false),
	REGION(false, true),
	DISTRICT(false, true),
	COMMUNITY(false, true),
	FACILITY(false, true),
	PERSON_REGION(false, false),
	PERSON_DISTRICT(false, false),
	PERSON_COMMUNITY(false, false),
	PERSON_CITY(false, false),
	PERSON_POSTCODE(false, false);

	private boolean usedForFilters;
	private boolean usedForGrouping;

	StatisticsCaseSubAttribute(boolean usedForFilters, boolean usedForGrouping) {
		this.usedForFilters = usedForFilters;
		this.usedForGrouping = usedForGrouping;
	}

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public boolean isUsedForFilters() {
		return usedForFilters;
	}

	public boolean isUsedForGrouping() {
		return usedForGrouping;
	}
}
