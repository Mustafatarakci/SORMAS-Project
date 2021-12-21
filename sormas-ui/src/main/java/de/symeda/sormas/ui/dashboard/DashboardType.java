package de.symeda.sormas.ui.dashboard;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum DashboardType {

	SURVEILLANCE,
	CONTACTS,
	CAMPAIGNS;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
