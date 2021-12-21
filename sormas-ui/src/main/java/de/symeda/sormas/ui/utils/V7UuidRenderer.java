package de.symeda.sormas.ui.utils;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.v7.ui.renderers.HtmlRenderer;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.HtmlHelper;
import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class V7UuidRenderer extends HtmlRenderer {

	@Override
	public JsonValue encode(String value) {

		if (StringUtils.isNotBlank(value)) {
			return super.encode(HtmlHelper.buildHyperlinkTitle(value, DataHelper.getShortUuid(value)));
		} else {
			return null;
		}
	}
}
