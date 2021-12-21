package de.symeda.sormas.api.task;

import java.util.Date;

import org.joda.time.DateTime;

public final class TaskHelper {

	private TaskHelper() {
		// Hide Utility Class Constructor
	}

	public static Date getDefaultSuggestedStart() {
		return new DateTime().toDate();
	}

	public static Date getDefaultDueDate() {
		return new DateTime().plusDays(1).toDate();
	}
}
