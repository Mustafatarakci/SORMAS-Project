package de.symeda.sormas.ui.utils;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import com.vaadin.v7.data.validator.AbstractValidator;

public class NumberNumericValueValidator extends AbstractValidator<String> {

	private BigDecimal minValue;
	private BigDecimal maxValue;
	private boolean decimalAllowed;

	public NumberNumericValueValidator(String errorMessage) {
		this(errorMessage, null, null, true);
	}

	public NumberNumericValueValidator(String errorMessage, Number minValue, Number maxValue) {
		this(errorMessage, minValue, maxValue, true);
	}

	public NumberNumericValueValidator(String errorMessage, Number minValue, Number maxValue, boolean decimalAllowed) {
		super(errorMessage);

		if (minValue != null) {
			this.minValue = new BigDecimal(minValue.toString());
		}

		if (maxValue != null) {
			this.maxValue = new BigDecimal(maxValue.toString());
		}

		this.decimalAllowed = decimalAllowed;
	}

	@Override
	protected boolean isValidValue(String number) {
		if (StringUtils.isBlank(number)) {
			return true;
		}

		Number parsedNumber;
		try {
			parsedNumber = Integer.valueOf(number);
		} catch (NumberFormatException ie) {
			try {
				parsedNumber = Long.valueOf(number);
			} catch (NumberFormatException le) {
				if (!decimalAllowed) {
					return false;
				}
				try {
					parsedNumber = Float.valueOf(number);
				} catch (NumberFormatException fe) {
					try {
						parsedNumber = Double.valueOf(number);
					} catch (NumberFormatException de) {
						return false;
					}
				}
			}
		}

		return validateRange(parsedNumber);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	private boolean validateRange(Number number) {
		BigDecimal decimalNumber = new BigDecimal(number.toString());

		if (minValue != null && minValue.compareTo(decimalNumber) > 0) {
			return false;
		}

		if (maxValue != null && maxValue.compareTo(decimalNumber) < 0) {
			return false;
		}

		return true;
	}

	@Override
	protected boolean isValidType(Object value) {
		return super.isValidType(value) || Number.class.isAssignableFrom(value.getClass());
	}
}
