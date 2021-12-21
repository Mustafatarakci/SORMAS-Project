package de.symeda.sormas.api.report;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;

public class WeeklyReportEntryDto extends EntityDto {

	private static final long serialVersionUID = 7863410150359837423L;

	public static final String I18N_PREFIX = "WeeklyReportEntry";

	private Disease disease;
	private Integer numberOfCases;

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	/**
	 * For informants the number of cases reported by the user.
	 * For officers the number of cases reported by the user and all related informants.
	 */
	public Integer getNumberOfCases() {
		return numberOfCases;
	}

	public void setNumberOfCases(Integer numberOfCases) {
		this.numberOfCases = numberOfCases;
	}
}
