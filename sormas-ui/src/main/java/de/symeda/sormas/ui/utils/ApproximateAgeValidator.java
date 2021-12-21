package de.symeda.sormas.ui.utils;

import java.util.function.Supplier;

import com.vaadin.v7.data.validator.AbstractValidator;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.person.ApproximateAgeType;

@SuppressWarnings("serial")
public class ApproximateAgeValidator extends AbstractValidator<Integer> {

	private TextField ageField;
	private Supplier<ApproximateAgeType> ageTypeSupplier;

	public ApproximateAgeValidator(TextField ageField, Supplier<ApproximateAgeType> ageTypeSupplier, String errorMessage) {
		super(errorMessage);
		this.ageField = ageField;
		this.ageTypeSupplier = ageTypeSupplier;
	}

	public ApproximateAgeValidator(TextField ageField, ComboBox ageTypeField, String errorMessage) {
		this(ageField, () -> (ApproximateAgeType) ageTypeField.getValue(), errorMessage);
	}

	@Override
	protected boolean isValidValue(Integer age) {
		ApproximateAgeType ageType = ageTypeSupplier.get();

		if (!ApproximateAgeType.YEARS.equals(ageType) || age == null) {
			return true;
		} else {
			return age <= 150;
		}
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
}
