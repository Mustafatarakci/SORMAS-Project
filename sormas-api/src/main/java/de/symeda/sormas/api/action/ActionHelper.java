package de.symeda.sormas.api.action;

import java.util.Date;

import org.joda.time.DateTime;

public final class ActionHelper {

	private ActionHelper() {
		// Hide Utility Class Constructor
	}

	public static Date getDefaultDate() {
		return new DateTime().toDate();
	}
}
