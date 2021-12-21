package de.symeda.sormas.ui.utils;

import com.vaadin.v7.ui.renderers.HtmlRenderer;

import de.symeda.sormas.api.utils.HtmlHelper;
import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class PercentageRenderer extends HtmlRenderer {

	@Override
	public JsonValue encode(String value) {
		if (value != null && !value.isEmpty()) {
			return super.encode(HtmlHelper.cleanHtml(value) + "%");
		} else {
			return null;
		}
	}

}
