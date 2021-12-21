package de.symeda.sormas.ui.utils;

import com.vaadin.v7.ui.renderers.HtmlRenderer;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class V7BooleanRenderer extends HtmlRenderer {

	@Override
	public JsonValue encode(String value) {

		if (value != null && !value.isEmpty()) {
			if (value.equals("true")) {
				return super.encode(I18nProperties.getString(Strings.yes));
			} else {
				return super.encode(I18nProperties.getString(Strings.no));
			}
		} else {
			return null;
		}
	}

}
