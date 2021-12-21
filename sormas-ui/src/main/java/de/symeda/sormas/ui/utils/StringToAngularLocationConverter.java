package de.symeda.sormas.ui.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

import com.vaadin.v7.data.util.converter.StringToDoubleConverter;

public final class StringToAngularLocationConverter extends StringToDoubleConverter {

	private static final long serialVersionUID = -8697124581004777191L;

	protected NumberFormat getFormat(Locale locale) {

		if (locale == null) {
			locale = Locale.getDefault();
		}

		DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		numberFormat.setGroupingUsed(false);
		numberFormat.setMaximumFractionDigits(5);

		return numberFormat;
	}

	@Override
	protected Number convertToNumber(String value, Class<? extends Number> targetType, Locale locale) throws ConversionException {

		return Optional.ofNullable(value).map(v -> v.replace(',', '.')).map(v -> super.convertToNumber(v, targetType, Locale.ENGLISH)).orElse(null);
	}
}
