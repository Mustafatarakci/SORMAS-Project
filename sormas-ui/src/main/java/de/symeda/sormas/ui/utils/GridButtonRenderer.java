package de.symeda.sormas.ui.utils;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.v7.ui.renderers.HtmlRenderer;

import de.symeda.sormas.api.utils.HtmlHelper;
import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class GridButtonRenderer extends HtmlRenderer {

	@Override
	public JsonValue encode(String value) {

		if (!StringUtils.isEmpty(value)) {
			//@formatter:off
    		value = "<div class='v-button v-widget primary v-button-primary' tabindex='0' role='button'>"
    				+ "<span class='v-button-wrap'>"
    				+ "<span class='v-button-caption'>" + HtmlHelper.cleanHtml(value) + "</span></span></div>";
//	    	value = "<a class='v-button v-button-primary' title='" + value + "'>" + value + "</a>";
    		//@formatter:on

			return super.encode(value);
		} else {
			return null;
		}
	}
}
