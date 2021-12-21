package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum PathogenTestResultType {

	INDETERMINATE,
	PENDING,
	NEGATIVE,
	POSITIVE,
	NOT_DONE;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
