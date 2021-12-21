package de.symeda.sormas.api.contact;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ContactStatus {

	ACTIVE,
	/**
	 * converted to case
	 */
	CONVERTED,
	/**
	 * case disproved or not a contact
	 */
	DROPPED;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
