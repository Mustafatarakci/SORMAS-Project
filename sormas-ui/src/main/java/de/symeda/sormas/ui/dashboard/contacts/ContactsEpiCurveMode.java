package de.symeda.sormas.ui.dashboard.contacts;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ContactsEpiCurveMode {

	FOLLOW_UP_STATUS,
	CONTACT_CLASSIFICATION,
	FOLLOW_UP_UNTIL;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
