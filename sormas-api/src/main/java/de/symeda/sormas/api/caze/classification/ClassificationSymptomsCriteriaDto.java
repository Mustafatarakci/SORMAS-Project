package de.symeda.sormas.api.caze.classification;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.symptoms.SymptomsDto;

public class ClassificationSymptomsCriteriaDto extends ClassificationCaseCriteriaDto {

	private static final long serialVersionUID = 6880120976447372375L;

	public ClassificationSymptomsCriteriaDto() {
		super();
	}

	public ClassificationSymptomsCriteriaDto(String propertyId) {
		super(propertyId, SymptomState.YES);
	}

	public ClassificationSymptomsCriteriaDto(String propertyId, Object... propertyValues) {
		super(propertyId, propertyValues);
	}

	@Override
	protected Class<? extends EntityDto> getInvokeClass() {
		return SymptomsDto.class;
	}

	@Override
	protected Object getInvokeObject(CaseDataDto caze) {
		return caze.getSymptoms();
	}

	@Override
	public String buildDescription() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(I18nProperties.getPrefixCaption(SymptomsDto.I18N_PREFIX, propertyId));
		if (!(propertyValues.get(0) instanceof SymptomState)) {
			appendDescValues(stringBuilder);
		}
		return stringBuilder.toString();
	}
}
