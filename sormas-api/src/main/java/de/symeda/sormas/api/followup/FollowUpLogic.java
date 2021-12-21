package de.symeda.sormas.api.followup;

import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.visit.VisitDto;
import de.symeda.sormas.api.visit.VisitStatus;

public final class FollowUpLogic {

	public static final int ALLOWED_DATE_OFFSET = 30;

	private FollowUpLogic() {
		// Hide Utility Class Constructor
	}

	public static int getNumberOfRequiredVisitsSoFar(Date reportDate, Date followUpUntil) {

		if (followUpUntil == null) {
			return 0;
		}

		Date now = new Date();
		if (now.before(followUpUntil)) {
			return DateHelper.getDaysBetween(DateHelper.addDays(reportDate, 1), now);
		} else {
			return DateHelper.getDaysBetween(DateHelper.addDays(reportDate, 1), followUpUntil);
		}
	}

	public static FollowUpPeriodDto getFollowUpStartDate(Date reportDate, List<SampleDto> samples) {
		Date earliestSampleDate = null;
		for (SampleDto sample : samples) {
			if (sample.getPathogenTestResult() == PathogenTestResultType.POSITIVE
				&& (earliestSampleDate == null || sample.getSampleDateTime().before(earliestSampleDate))) {
				earliestSampleDate = sample.getSampleDateTime();
			}
		}
		return getFollowUpStartDate(reportDate, earliestSampleDate);
	}

	public static FollowUpPeriodDto getFollowUpStartDate(Date reportDate, Date earliestSampleDate) {
		if (earliestSampleDate != null && earliestSampleDate.before(reportDate)) {
			return new FollowUpPeriodDto(earliestSampleDate, FollowUpStartDateType.EARLIEST_SAMPLE_COLLECTION_DATE);
		}

		return new FollowUpPeriodDto(reportDate, FollowUpStartDateType.REPORT_DATE);
	}

	public static FollowUpPeriodDto calculateFollowUpUntilDate(
		FollowUpPeriodDto followUpPeriod,
		Date overwriteUntilDate,
		List<VisitDto> visits,
		int followUpDuration,
		boolean allowFreeOverwrite) {

		Date standardUntilDate = DateHelper.addDays(followUpPeriod.getFollowUpStartDate(), followUpDuration);
		Date untilDate = overwriteUntilDate != null ? overwriteUntilDate : standardUntilDate;

		Date lastVisitDate = null;
		boolean additionalVisitNeeded = true;
		for (VisitDto visit : visits) {
			if (lastVisitDate == null || DateHelper.getStartOfDay(visit.getVisitDateTime()).after(DateHelper.getStartOfDay(lastVisitDate))) {
				lastVisitDate = visit.getVisitDateTime();
			}
			if (additionalVisitNeeded
				&& !DateHelper.getStartOfDay(visit.getVisitDateTime()).before(DateHelper.getStartOfDay(untilDate))
				&& visit.getVisitStatus() == VisitStatus.COOPERATIVE) {
				additionalVisitNeeded = false;
			}
		}

		// Follow-up until needs to be extended to the date after the last visit if there is no cooperative visit after the follow-up until date
		if (additionalVisitNeeded && lastVisitDate != null && untilDate.before(DateHelper.addDays(lastVisitDate, 1))) {
			untilDate = DateHelper.addDays(lastVisitDate, 1);
		}

		// If the follow-up until date is before the standard follow-up until date for some reason (e.g. because the report date, last contact
		// date or symptom onset date were changed), and allowFreeOverwrite is false, set it to the standard follow-up until date
		if (!allowFreeOverwrite && DateHelper.getStartOfDay(untilDate).before(standardUntilDate)) {
			untilDate = standardUntilDate;
		}

		followUpPeriod.setFollowUpEndDate(untilDate);
		return followUpPeriod;
	}

}
