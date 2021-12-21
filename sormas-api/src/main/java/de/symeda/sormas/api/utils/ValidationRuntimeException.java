package de.symeda.sormas.api.utils;

import java.util.List;
import java.util.Map;

import javax.ejb.ApplicationException;

/*
 * ATTENTION: Does not do a rollback when thrown because this class is used in
 * case import where no rollback may be done (in order to continue with the import
 * when validation of a single case fails).
 * Make sure to call this before changing backend data (e.g. when using it to
 * validate transfered cases).
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = false)
public class ValidationRuntimeException extends RuntimeException {

	private Map<List<String>, String> propertyErrors;

	public ValidationRuntimeException(String message) {
		super(message);
	}

	public ValidationRuntimeException(Map<List<String>, String> propertyErrors) {
		this.propertyErrors = propertyErrors;
	}

	public ValidationRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public Map<List<String>, String> getPropertyErrors() {
		return propertyErrors;
	}
}
