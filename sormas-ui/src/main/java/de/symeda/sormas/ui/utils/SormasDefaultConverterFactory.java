package de.symeda.sormas.ui.utils;

import java.util.Date;
import java.util.Locale;

import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.data.util.converter.DefaultConverterFactory;
import com.vaadin.v7.data.util.converter.StringToEnumConverter;

@SuppressWarnings("serial")
public final class SormasDefaultConverterFactory extends DefaultConverterFactory {

	private static final DateConverter DATE_CONVERTER = new DateConverter();

	@Override
	protected Converter<String, ?> createStringConverter(Class<?> sourceType) {

		if (Enum.class.isAssignableFrom(sourceType)) {
			return new StringToEnumConverter() {

				@SuppressWarnings("rawtypes")
				@Override
				public String convertToPresentation(Enum value, Class<? extends String> targetType, Locale locale) throws ConversionException {
					if (value == null) {
						return null;
					}

					return SormasDefaultConverterFactory.enumToString(value, locale);
				}
			};
		}
		return super.createStringConverter(sourceType);
	}

	@Override
	protected Converter<Date, ?> createDateConverter(Class<?> sourceType) {
		if (Date.class == sourceType) {
			return DATE_CONVERTER;
		}

		return super.createDateConverter(sourceType);
	}

	public static String enumToString(Enum<?> value, Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		String enumString = value.toString();
		// we don't want to have this part of Vaadin magic
//        if (enumString.equals(value.name())) {
//            // FOO -> Foo
//            // FOO_BAR -> Foo bar
//            // _FOO -> _foo
//            String result = enumString.substring(0, 1).toUpperCase(locale);
//            result += enumString.substring(1).toLowerCase(locale).replace('_',
//                    ' ');
//            return result;
//        } else 
		{
			return enumString;
		}
	}
}
