package de.symeda.sormas.ui.utils;

import com.vaadin.v7.ui.renderers.HtmlRenderer;

import de.symeda.sormas.api.utils.HtmlHelper;
import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class V7ShortStringRenderer extends HtmlRenderer {

	private final int length;

	public V7ShortStringRenderer(int length) {
		this.length = length;
	}

	@Override
	public JsonValue encode(String value) {

		if (value != null && !value.isEmpty()) {
			if (value.length() > length) {
				value = value.substring(0, length);
				value += "...";
			}
			return super.encode(HtmlHelper.cleanHtml(value));
		} else {
			return null;
		}
	}
}
