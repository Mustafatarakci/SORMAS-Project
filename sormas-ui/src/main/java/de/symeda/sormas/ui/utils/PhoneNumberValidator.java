package de.symeda.sormas.ui.utils;

import com.vaadin.v7.data.validator.AbstractValidator;

import de.symeda.sormas.api.utils.DataHelper;

@SuppressWarnings("serial")
public class PhoneNumberValidator extends AbstractValidator<String> {

	public PhoneNumberValidator(String errorMessage) {
		super(errorMessage);
	}

	@Override
	protected boolean isValidValue(String phoneNumber) {
		return DataHelper.isValidPhoneNumber(phoneNumber);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}
}
