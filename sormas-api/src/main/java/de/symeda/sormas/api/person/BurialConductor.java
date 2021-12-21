package de.symeda.sormas.api.person;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum BurialConductor {

	FAMILY_COMMUNITY,
	OUTBREAK_TEAM;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
