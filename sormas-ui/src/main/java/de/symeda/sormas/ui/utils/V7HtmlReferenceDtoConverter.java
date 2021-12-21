package de.symeda.sormas.ui.utils;

import java.util.Locale;

import com.vaadin.v7.data.util.converter.Converter;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.HtmlHelper;

@SuppressWarnings("serial")
public class V7HtmlReferenceDtoConverter implements Converter<String, ReferenceDto> {

	@Override
	public ReferenceDto convertToModel(String value, Class<? extends ReferenceDto> targetType, Locale locale) throws ConversionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String convertToPresentation(ReferenceDto value, Class<? extends String> targetType, Locale locale) throws ConversionException {

		final String html;
		if (value != null) {
			html = String.format(
				"%s (%s)",
				HtmlHelper.buildHyperlinkTitle(value.getUuid(), DataHelper.getShortUuid(value.getUuid())),
				HtmlHelper.cleanHtml(value.getCaption()));
		} else {
			html = "";
		}
		return html;
	}

	@Override
	public Class<ReferenceDto> getModelType() {
		return ReferenceDto.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
}
