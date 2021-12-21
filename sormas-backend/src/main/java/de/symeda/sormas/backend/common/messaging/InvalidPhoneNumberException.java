package de.symeda.sormas.backend.common.messaging;

@SuppressWarnings("serial")
public class InvalidPhoneNumberException extends Exception {

	public InvalidPhoneNumberException(String message, Throwable cause) {
		super(message, cause);
	}
}
