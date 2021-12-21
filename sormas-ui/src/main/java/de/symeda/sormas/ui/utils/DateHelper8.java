package de.symeda.sormas.ui.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateHelper8 {

	private DateHelper8() {
		// Hide Utility Class Constructor
	}

	public static LocalDate toLocalDate(Date date) {
		if (date != null) {
			return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		} else {
			return null;
		}
	}

	public static Date toDate(LocalDate date) {
		if (date != null) {
			return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} else {
			return null;
		}
	}

	public static Date toDate(LocalDateTime dateTime) {
		return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
}
