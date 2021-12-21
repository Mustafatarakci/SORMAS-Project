package de.symeda.sormas.api.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

// This class provides general XSS-Prevention methods using Jsoup.clean
public class HtmlHelper {

	public static final Whitelist EVENTACTION_WHITELIST =
		Whitelist.relaxed().addTags("hr", "font").addAttributes("font", "size", "face", "color").addAttributes("div", "align");

	private static final String HYPERLINK_TAG = "a";
	private static final String SPAN_TAG = "span";
	private static final String TITLE_ATTRIBUTE = "title";

	public static String cleanHtml(String string) {
		return (string == null) ? "" : Jsoup.clean(string, Whitelist.none());
	}

	public static String cleanHtml(String string, Whitelist whitelist) {
		return (string == null) ? "" : Jsoup.clean(string, whitelist);
	}

	/**
	 * @param attributeKey
	 *            For example {@code title}.
	 * @param attributeValue
	 *            The value for the given {@code attributeKey}.
	 * @return Built syntax for a HTML tag attribute with possible html tags in {@code attributeValue} escaped to prevent HTML injection.
	 */
	public static String cleanHtmlAttribute(String attributeKey, String attributeValue) {

		return String.format("%s='%s'", attributeKey, HtmlHelper.cleanHtml(attributeValue));
	}

	// this method should be used for i18n-strings and captions so that custom whitelist rules can be added when needed
	public static String cleanI18nString(String string) {
		return (string == null) ? "" : Jsoup.clean(string, Whitelist.basic());
	}

	public static String cleanHtmlRelaxed(String string) {
		return (string == null) ? "" : Jsoup.clean(string, Whitelist.relaxed());
	}

	/**
	 * @param title
	 *            Title for {@code a} tag (hover text).
	 * @param caption
	 *            Caption of the {@code a} tag (visual text).
	 * @return Generated hyperlink with possible html tags escaped to prevent HTML injection.
	 */
	public static String buildHyperlinkTitle(String title, String caption) {
		return buildTitle(HYPERLINK_TAG, title, caption);
	}

	public static String buildTitle(String title, String caption) {
		return buildTitle(SPAN_TAG, title, caption);
	}

	private static String buildTitle(String tag, String title, String caption) {

		// Build tag with title attribute
		String result = String.format("<%s %s>%s</%s>", tag, cleanHtmlAttribute(TITLE_ATTRIBUTE, title), HtmlHelper.cleanHtml(caption), tag);

		// Prevent breakout in tag attributes: only allow the intended tag attribute
		result = Jsoup.clean(result, Whitelist.none().addTags(tag).addAttributes(tag, TITLE_ATTRIBUTE));
		return result;
	}
}
