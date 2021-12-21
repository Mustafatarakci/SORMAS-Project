package de.symeda.sormas.ui.utils;

import com.vaadin.v7.data.validator.AbstractValidator;

@SuppressWarnings("serial")
public class UserPhoneNumberValidator extends AbstractValidator<String> {

	public UserPhoneNumberValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	protected boolean isValidValue(String phoneNumber) {
		return phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.startsWith("+");
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}
}
