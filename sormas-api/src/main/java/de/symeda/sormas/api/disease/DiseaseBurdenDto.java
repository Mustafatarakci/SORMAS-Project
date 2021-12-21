package de.symeda.sormas.api.disease;

import java.io.Serializable;

import de.symeda.sormas.api.Disease;

public class DiseaseBurdenDto implements Serializable {

	private static final long serialVersionUID = 2430932452606853497L;

	public static final String I18N_PREFIX = "DiseaseBurden";

	public static final String DISEASE = "disease";
	public static final String CASE_COUNT = "caseCount";
	public static final String PREVIOUS_CASE_COUNT = "previousCaseCount";
	public static final String CASES_DIFFERENCE = "casesDifference";
	public static final String CASES_DIFFERENCE_PERCENTAGE = "casesDifferencePercentage";
	public static final String EVENT_COUNT = "eventCount";
	public static final String OUTBREAK_DISTRICT_COUNT = "outbreakDistrictCount";
	public static final String CASE_DEATH_COUNT = "caseDeathCount";
	public static final String CASE_FATALITY_RATE = "caseFatalityRate";
	public static final String LAST_REPORTED_DISTRICT_NAME = "lastReportedDistrictName";

	private Disease disease;
	private Long caseCount;
	private Long previousCaseCount;
	private Long eventCount;
	private Long outbreakDistrictCount;
	private Long caseDeathCount;
	private String lastReportedDistrictName;

	public DiseaseBurdenDto(
		Disease disease,
		Long caseCount,
		Long previousCaseCount,
		Long eventCount,
		Long outbreakDistrictCount,
		Long caseDeathCount,
		String lastReportedDistrictName) {

		this.disease = disease;
		this.caseCount = caseCount;
		this.previousCaseCount = previousCaseCount;
		this.eventCount = eventCount;
		this.outbreakDistrictCount = outbreakDistrictCount;
		this.caseDeathCount = caseDeathCount;
		this.lastReportedDistrictName = lastReportedDistrictName;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public Long getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(Long caseCount) {
		this.caseCount = caseCount;
	}

	public Long getPreviousCaseCount() {
		return previousCaseCount;
	}

	public void setPreviousCaseCount(Long previousCaseCount) {
		this.previousCaseCount = previousCaseCount;
	}

	public Long getCasesDifference() {
		return getCaseCount() - getPreviousCaseCount();
	}

	public Float getCasesDifferencePercentage() {
		float percentage = 0f;

		if (getPreviousCaseCount() == 0 && getCaseCount() > 0)
			percentage = 100f;
		else if (getCaseCount() == 0 && getPreviousCaseCount() > 0)
			percentage = -100f;
		else
			percentage = (float) getCasesDifference() / (float) (getPreviousCaseCount() == 0 ? 1 : getPreviousCaseCount()) * 100;

		return Math.round(percentage * 10) / 10.0f;
	}

	public Long getEventCount() {
		return eventCount;
	}

	public void setEventCount(Long eventCount) {
		this.eventCount = eventCount;
	}

	public Long getOutbreakDistrictCount() {
		return outbreakDistrictCount;
	}

	public void setOutbreakDistrictCount(Long outbreakDistrictCount) {
		this.outbreakDistrictCount = outbreakDistrictCount;
	}

	public Long getCaseDeathCount() {
		return caseDeathCount;
	}

	public void setCaseDeathCount(Long caseDeathCount) {
		this.caseDeathCount = caseDeathCount;
	}

	public float getCaseFatalityRate() {

		float cfrPercentage = 100f * ((float) getCaseDeathCount() / (float) (getCaseCount() == 0 ? 1 : getCaseCount()));
		cfrPercentage = Math.round(cfrPercentage * 100) / 100f;
		return cfrPercentage;
	}

	public String getLastReportedDistrictName() {
		return lastReportedDistrictName;
	}

	public void setLastReportedDistrictName(String name) {
		this.lastReportedDistrictName = name;
	}

	public Boolean hasCount() {
		return (caseCount + previousCaseCount + eventCount + outbreakDistrictCount) > 0;
	}
}
