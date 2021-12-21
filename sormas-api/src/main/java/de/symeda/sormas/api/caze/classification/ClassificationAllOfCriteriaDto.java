package de.symeda.sormas.api.caze.classification;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.PathogenTestDto;

public class ClassificationAllOfCriteriaDto extends ClassificationCriteriaDto implements ClassificationCollectiveCriteria {

	private static final long serialVersionUID = -6427002056924376593L;

	/**
	 * Always draws all sub criteria in one component instead of providing a dedicated
	 * component for all of them.
	 */
	protected boolean drawSubCriteriaTogether = false;

	protected List<ClassificationCriteriaDto> subCriteria;

	public ClassificationAllOfCriteriaDto() {

	}

	public ClassificationAllOfCriteriaDto(ClassificationCriteriaDto... criteria) {
		this.subCriteria = Arrays.asList(criteria);
	}

	public ClassificationAllOfCriteriaDto(boolean drawSubCriteriaTogether, ClassificationCriteriaDto... criteria) {

		this(criteria);
		this.drawSubCriteriaTogether = drawSubCriteriaTogether;
	}

	@Override
	public boolean eval(CaseDataDto caze, PersonDto person, List<PathogenTestDto> sampleTests, List<EventDto> events, Date lastVaccinationDate) {

		for (ClassificationCriteriaDto classificationCriteriaDto : subCriteria) {
			if (!classificationCriteriaDto.eval(caze, person, sampleTests, events, lastVaccinationDate))
				return false;
		}

		return true;
	}

	@Override
	public String buildDescription() {
		return getCriteriaName();
	}

	@Override
	public String getCriteriaName() {
		return "<b>" + I18nProperties.getString(Strings.classificationAllOf).toUpperCase() + "</b>";
	}

	@Override
	public List<ClassificationCriteriaDto> getSubCriteria() {
		return subCriteria;
	}

	/**
	 * Has a different buildDescription method to display all sub criteria in one line, with the sub criteria separated
	 * by an "AND". Functionality is identical to ClassificationAllOfCriteria.
	 */
	public static class ClassificationAllOfCompactCriteriaDto extends ClassificationAllOfCriteriaDto implements ClassificationCompactCriteria {

		private static final long serialVersionUID = 3761118522728690578L;

		public ClassificationAllOfCompactCriteriaDto(ClassificationCriteriaDto... criteria) {
			super(criteria);
		}

		@Override
		public String buildDescription() {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < subCriteria.size(); i++) {
				if (i > 0) {
					if (i + 1 < subCriteria.size()) {
						stringBuilder.append(", ");
					} else {
						stringBuilder.append(" <b>").append(I18nProperties.getString(Strings.and).toUpperCase()).append("</b> ");
					}
				}

				stringBuilder.append(subCriteria.get(i).buildDescription());
			}

			return stringBuilder.toString();
		}
	}

	public void setSubCriteria(List<ClassificationCriteriaDto> subCriteria) {
		this.subCriteria = subCriteria;
	}

	public boolean isDrawSubCriteriaTogether() {
		return drawSubCriteriaTogether;
	}

	public void setDrawSubCriteriaTogether(boolean drawSubCriteriaTogether) {
		this.drawSubCriteriaTogether = drawSubCriteriaTogether;
	}
}
