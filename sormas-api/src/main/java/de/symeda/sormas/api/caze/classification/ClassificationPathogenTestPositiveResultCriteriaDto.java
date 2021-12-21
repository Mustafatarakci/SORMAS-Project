package de.symeda.sormas.api.caze.classification;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;

public class ClassificationPathogenTestPositiveResultCriteriaDto extends ClassificationCriteriaDto {

	private static final long serialVersionUID = 3811127784970509183L;

	protected Disease testedDisease;
	protected List<PathogenTestType> pathogenTestTypes;

	public ClassificationPathogenTestPositiveResultCriteriaDto() {

	}

	public ClassificationPathogenTestPositiveResultCriteriaDto(Disease testedDisease, PathogenTestType... pathogenTestTypes) {

		this.testedDisease = testedDisease;
		this.pathogenTestTypes = Arrays.asList(pathogenTestTypes);
	}

	@Override
	public boolean eval(CaseDataDto caze, PersonDto person, List<PathogenTestDto> pathogenTests, List<EventDto> events, Date lastVaccinationDate) {

		for (PathogenTestDto pathogenTest : pathogenTests) {
			if (pathogenTest.getTestResult() == PathogenTestResultType.POSITIVE && pathogenTestTypes.contains(pathogenTest.getTestType())) {
				if (testedDisease == null || pathogenTest.getTestedDisease() == testedDisease) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String buildDescription() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(I18nProperties.getString(Strings.classificationOnePositiveTestResult)).append(" ");
		for (int i = 0; i < pathogenTestTypes.size(); i++) {
			if (i > 0) {
				if (i < pathogenTestTypes.size() - 1) {
					stringBuilder.append(", ");
				} else {
					stringBuilder.append(" <b>").append(I18nProperties.getString(Strings.or).toUpperCase()).append("</b> ");
				}
			}

			stringBuilder.append(pathogenTestTypes.get(i).toString());
		}

		if (testedDisease != null) {
			stringBuilder.append(" ").append(I18nProperties.getString(Strings.classificationForDisease)).append(" ").append(testedDisease.toString());
		}

		return stringBuilder.toString();
	}

	public List<PathogenTestType> getPathogenTestTypes() {
		return pathogenTestTypes;
	}

	public void setSampleTestTypes(List<PathogenTestType> pathogenTestTypes) {
		this.pathogenTestTypes = pathogenTestTypes;
	}
}
