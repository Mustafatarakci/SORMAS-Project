package de.symeda.sormas.ui.importer;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;

public class ImportErrorException extends Exception {

	private static final long serialVersionUID = -5852533615013283186L;

	public ImportErrorException(String value, String column) {
		super(I18nProperties.getString(Strings.errorInvalidValue) + " " + value + " " + I18nProperties.getString(Strings.inColumn) + " " + column);
	}

	public ImportErrorException(String message) {
		super(message);
	}
}
