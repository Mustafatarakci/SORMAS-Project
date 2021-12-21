package de.symeda.sormas.api.caze.classification;

import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.person.ApproximateAgeType.ApproximateAgeHelper;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.PathogenTestDto;

public class ClassificationPersonAgeBetweenYearsCriteriaDto extends ClassificationCriteriaDto {

	private static final long serialVersionUID = 7306888279187764644L;

	protected Integer lowerYearsThreshold;
	protected Integer upperYearsThreshold;

	public ClassificationPersonAgeBetweenYearsCriteriaDto() {

	}

	public ClassificationPersonAgeBetweenYearsCriteriaDto(Integer lowerYearsThreshold, Integer upperYearsThreshold) {
		this.lowerYearsThreshold = lowerYearsThreshold;
		this.upperYearsThreshold = upperYearsThreshold;
	}

	@Override
	public boolean eval(CaseDataDto caze, PersonDto person, List<PathogenTestDto> sampleTests, List<EventDto> events, Date lastVaccinationDate) {
		Integer approximateAge = ApproximateAgeHelper.getAgeYears(person.getApproximateAge(), person.getApproximateAgeType());
		if (approximateAge == null) {
			return false;
		}

		if (lowerYearsThreshold != null && approximateAge < lowerYearsThreshold) {
			return false;
		}
		if (upperYearsThreshold != null && approximateAge > upperYearsThreshold) {
			return false;
		}

		return true;
	}

	@Override
	public String buildDescription() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(I18nProperties.getString(Strings.classificationPersonAged)).append(" ");
		if (lowerYearsThreshold != null && upperYearsThreshold != null) {
			stringBuilder.append(I18nProperties.getString(Strings.between))
				.append(" ")
				.append(lowerYearsThreshold)
				.append(" ")
				.append(I18nProperties.getString(Strings.and))
				.append(" ")
				.append(upperYearsThreshold)
				.append(" ")
				.append(I18nProperties.getString(Strings.years));
		} else if (lowerYearsThreshold != null) {
			stringBuilder.append(lowerYearsThreshold).append(" ").append(I18nProperties.getString(Strings.classificationYearsOrMore));
		} else if (upperYearsThreshold != null) {
			stringBuilder.append(upperYearsThreshold).append(" ").append(I18nProperties.getString(Strings.classificationYearsOrLess));
		}

		return stringBuilder.toString();
	}

	public Integer getLowerThreshold() {
		return lowerYearsThreshold;
	}

	public void setLowerThreshold(Integer lowerThreshold) {
		this.lowerYearsThreshold = lowerThreshold;
	}

	public Integer getUpperThreshold() {
		return upperYearsThreshold;
	}

	public void setUpperThreshold(Integer upperThreshold) {
		this.upperYearsThreshold = upperThreshold;
	}
}
