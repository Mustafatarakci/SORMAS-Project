package de.symeda.sormas.api.caze.classification;

import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseLogic;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.utils.DateHelper;

/**
 * Classification criteria that is applicable when any immunization associated with the case person
 * with the same disease as the case has a vaccination with a vaccination date within the date range
 * specified by daysBeforeStartDate and the start date of the case
 */
public class ClassificationVaccinationDateNotInStartDateRangeDto extends ClassificationCaseCriteriaDto {

	private static final long serialVersionUID = -8817472226784147694L;

	private int daysBeforeStartDate;

	public ClassificationVaccinationDateNotInStartDateRangeDto(int daysBeforeStartDate) {
		this.daysBeforeStartDate = daysBeforeStartDate;
	}

	@Override
	public boolean eval(CaseDataDto caze, PersonDto person, List<PathogenTestDto> sampleTests, List<EventDto> events, Date lastVaccinationDate) {

		Date startDate = CaseLogic.getStartDate(caze.getSymptoms().getOnsetDate(), caze.getReportDate());
		Date lowerThresholdDate = DateHelper.subtractDays(startDate, daysBeforeStartDate);

		return lastVaccinationDate == null
			|| !(lastVaccinationDate.equals(lowerThresholdDate)
				|| lastVaccinationDate.equals(startDate)
				|| (lastVaccinationDate.after(lowerThresholdDate) && lastVaccinationDate.before(startDate)));
	}

	@Override
	public String buildDescription() {

		return I18nProperties.getString(Strings.classificationLastVaccinationDateWithin) + " " + daysBeforeStartDate + " "
			+ I18nProperties.getString(Strings.classificationDaysBeforeCaseStart);
	}

	public int getDaysBeforeStartDate() {
		return daysBeforeStartDate;
	}

	public void setDaysBeforeStartDate(int daysBeforeStartDate) {
		this.daysBeforeStartDate = daysBeforeStartDate;
	}
}
