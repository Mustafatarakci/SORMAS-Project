package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum RabiesType {

	FURIOUS_RABIES,
	PARALYTIC_RABIES;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
