package de.symeda.sormas.api.event;

import java.util.Date;

import de.symeda.sormas.api.action.ActionMeasure;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateFormatHelper;

public final class EventHelper {

	private EventHelper() {
		// Hide Utility Class Constructor
	}

	public static String buildInstitutionalPartnerTypeString(
		InstitutionalPartnerType institutionalPartnerType,
		String institutionalPartnerTypeDetails) {

		if (institutionalPartnerType == InstitutionalPartnerType.OTHER) {
			return DataHelper.toStringNullable(institutionalPartnerTypeDetails);
		}

		return DataHelper.toStringNullable(institutionalPartnerType);
	}

	public static String buildEventDateString(Date eventStartDate, Date eventEndDate) {

		if (eventStartDate == null) {
			return "";
		} else if (eventEndDate == null) {
			return DateFormatHelper.formatLocalDateTime(eventStartDate);
		} else {
			return String.format("%s - %s", DateFormatHelper.formatLocalDateTime(eventStartDate), DateFormatHelper.formatLocalDateTime(eventEndDate));
		}
	}

	public static String buildMeansOfTransportString(MeansOfTransport meansOfTransport, String meansOfTransportDetails) {

		if (meansOfTransport == MeansOfTransport.OTHER) {
			return DataHelper.toStringNullable(meansOfTransportDetails);
		}

		return DataHelper.toStringNullable(meansOfTransport);
	}

	public static String buildEventActionTitleString(ActionMeasure actionMeasure, String actionTitle) {
		return actionMeasure == null || actionMeasure == ActionMeasure.OTHER ? actionTitle : actionMeasure.toString();
	}

	public static Date getStartOrEndDate(Date eventStartDate, Date eventEndDate) {
		return eventStartDate != null ? eventStartDate : eventEndDate;
	}

}
