package de.symeda.sormas.ui.utils;

import java.util.Optional;

import com.vaadin.ui.renderers.TextRenderer;

import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class ShortStringRenderer extends TextRenderer {

	private final int length;

	public ShortStringRenderer(int length) {
		this.length = length;
	}

	@Override
	public JsonValue encode(Object value) {

		String val = Optional.ofNullable(value).map(o -> o.toString()).filter(s -> !s.isEmpty()).map(s -> {
			if (s.length() <= length) {
				return s;
			} else {
				return s.substring(0, length) + "...";
			}
		}).orElse(null);

		return super.encode(val);
	}
}
