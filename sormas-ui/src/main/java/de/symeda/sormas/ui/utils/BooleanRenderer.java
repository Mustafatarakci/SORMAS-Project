package de.symeda.sormas.ui.utils;

import com.vaadin.ui.renderers.TextRenderer;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class BooleanRenderer extends TextRenderer {

	@Override
	public JsonValue encode(Object value) {

		if (value != null && (value.getClass().equals(boolean.class) || Boolean.class.isAssignableFrom(value.getClass()))) {
			if ((Boolean) value) {
				return super.encode(I18nProperties.getString(Strings.yes));
			} else {
				return super.encode(I18nProperties.getString(Strings.no));
			}
		} else {
			return null;
		}
	}
}
