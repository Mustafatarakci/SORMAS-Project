package de.symeda.sormas.ui.utils;

import com.vaadin.data.ValueProvider;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.HtmlHelper;

@SuppressWarnings("serial")
public class ReferenceDtoHtmlProvider implements ValueProvider<ReferenceDto, String> {

	@Override
	public String apply(ReferenceDto source) {

		final String html;
		if (source != null) {
			html = String.format(
				"%s (%s)",
				HtmlHelper.buildHyperlinkTitle(source.getUuid(), DataHelper.getShortUuid(source.getUuid())),
				HtmlHelper.cleanHtml(source.getCaption()));
		} else {
			html = "";
		}
		return html;
	}
}
