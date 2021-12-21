package de.symeda.sormas.api.action;

import de.symeda.sormas.api.i18n.I18nProperties;

/**
 * Context of an action.
 */
public enum ActionContext {

	// XXX: add here new contexts (case, contact, …)
	EVENT;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
