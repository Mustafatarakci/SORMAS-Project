package de.symeda.sormas.ui.utils;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.v7.ui.renderers.HtmlRenderer;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.HtmlHelper;
import elemental.json.JsonValue;

@SuppressWarnings("serial")
public class V7CaseUuidRenderer extends HtmlRenderer {

	private final boolean withCreateCaseIfEmpty;

	public V7CaseUuidRenderer(boolean withCreateCaseIfEmpty) {
		this.withCreateCaseIfEmpty = withCreateCaseIfEmpty;
	}

	@Override
	public JsonValue encode(String value) {

		if (StringUtils.isBlank(value) && withCreateCaseIfEmpty) {
			String createCase = String.format(
				"%s %s",
				HtmlHelper
					.buildHyperlinkTitle(I18nProperties.getString(Strings.headingCreateNewCase), I18nProperties.getCaption(Captions.actionCreate)),
				VaadinIcons.EDIT.getHtml());
			return super.encode(createCase);
		} else if (StringUtils.isNotBlank(value)) {
			return super.encode(HtmlHelper.buildHyperlinkTitle(value, DataHelper.getShortUuid(value)));
		} else {
			return null;
		}
	}
}
