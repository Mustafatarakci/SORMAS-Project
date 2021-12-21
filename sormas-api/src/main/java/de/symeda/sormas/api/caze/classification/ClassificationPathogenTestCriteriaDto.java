package de.symeda.sormas.api.caze.classification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestType;

public class ClassificationPathogenTestCriteriaDto extends ClassificationCaseCriteriaDto {

	private static final long serialVersionUID = 856637988490366395L;

	private List<PathogenTestType> testTypes;

	public ClassificationPathogenTestCriteriaDto() {
		super();
	}

	public ClassificationPathogenTestCriteriaDto(String propertyId, List<PathogenTestType> testTypes, Object... propertyValues) {

		super(propertyId, propertyValues);
		this.testTypes = testTypes;
	}

	@Override
	protected Class<? extends EntityDto> getInvokeClass() {
		return PathogenTestDto.class;
	}

	@Override
	public boolean eval(CaseDataDto caze, PersonDto person, List<PathogenTestDto> pathogenTests, List<EventDto> events, Date lastVaccinationDate) {

		for (PathogenTestDto pathogenTest : pathogenTests) {
			if (!testTypes.contains(pathogenTest.getTestType())) {
				continue;
			}

			Method method = null;
			try {
				method = getInvokeClass().getMethod("get" + propertyId.substring(0, 1).toUpperCase() + propertyId.substring(1));
			} catch (NoSuchMethodException e) {
				try {
					method = getInvokeClass().getMethod("is" + propertyId.substring(0, 1).toUpperCase() + propertyId.substring(1));
				} catch (NoSuchMethodException newE) {
					throw new RuntimeException(newE);
				}
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			}

			try {
				Object value = method.invoke(pathogenTest);
				if (propertyValues.contains(value)) {
					return true;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}

		return false;
	}

	@Override
	public String buildDescription() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, propertyId));
		if (testTypes != null && !testTypes.isEmpty()) {
			stringBuilder.append(" ").append(I18nProperties.getString(Strings.classificationCriteriaForTestType)).append(" ");
			for (int i = 0; i < testTypes.size(); i++) {
				if (i > 0) {
					if (i == testTypes.size() - 1) {
						stringBuilder.append(" <b>").append(I18nProperties.getString(Strings.or).toUpperCase()).append("</b> ");
					} else {
						stringBuilder.append(", ");
					}
				}
				stringBuilder.append(testTypes.get(i).toString());
			}
		}

		return stringBuilder.toString();
	}

	public List<PathogenTestType> getTestTypes() {
		return testTypes;
	}

	public void setTestTypes(List<PathogenTestType> testTypes) {
		this.testTypes = testTypes;
	}
}
