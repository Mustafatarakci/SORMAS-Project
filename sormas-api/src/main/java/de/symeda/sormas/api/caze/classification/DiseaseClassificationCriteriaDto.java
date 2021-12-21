package de.symeda.sormas.api.caze.classification;

import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;

public class DiseaseClassificationCriteriaDto extends EntityDto {

	private static final long serialVersionUID = 8800921617332187938L;

	private Disease disease;
	private ClassificationCriteriaDto suspectCriteria;
	private ClassificationCriteriaDto probableCriteria;
	private ClassificationCriteriaDto confirmedCriteria;
	private ClassificationCriteriaDto confirmedNoSymptomsCriteria;
	private ClassificationCriteriaDto confirmedUnknownSymptomsCriteria;
	private ClassificationCriteriaDto notACaseCriteria;

	public DiseaseClassificationCriteriaDto() {

	}

	public DiseaseClassificationCriteriaDto(
		Disease disease,
		Date changeDate,
		ClassificationCriteriaDto suspectCriteria,
		ClassificationCriteriaDto probableCriteria,
		ClassificationCriteriaDto confirmedCriteria,
		ClassificationCriteriaDto confirmedNoSymptomsCriteria,
		ClassificationCriteriaDto confirmedUnknownSymptomsCriteria,
		ClassificationCriteriaDto notACaseCriteria) {

		super(changeDate, changeDate, null);
		this.disease = disease;
		this.suspectCriteria = suspectCriteria;
		this.probableCriteria = probableCriteria;
		this.confirmedCriteria = confirmedCriteria;
		this.confirmedNoSymptomsCriteria = confirmedNoSymptomsCriteria;
		this.confirmedUnknownSymptomsCriteria = confirmedUnknownSymptomsCriteria;
		this.notACaseCriteria = notACaseCriteria;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public ClassificationCriteriaDto getSuspectCriteria() {
		return suspectCriteria;
	}

	public void setSuspectCriteria(ClassificationCriteriaDto suspectCriteria) {
		this.suspectCriteria = suspectCriteria;
	}

	public ClassificationCriteriaDto getProbableCriteria() {
		return probableCriteria;
	}

	public void setProbableCriteria(ClassificationCriteriaDto probableCriteria) {
		this.probableCriteria = probableCriteria;
	}

	public ClassificationCriteriaDto getConfirmedCriteria() {
		return confirmedCriteria;
	}

	public void setConfirmedCriteria(ClassificationCriteriaDto confirmedCriteria) {
		this.confirmedCriteria = confirmedCriteria;
	}

	public ClassificationCriteriaDto getConfirmedNoSymptomsCriteria() {
		return confirmedNoSymptomsCriteria;
	}

	public void setConfirmedNoSymptomsCriteria(ClassificationCriteriaDto confirmedNoSymptomsCriteria) {
		this.confirmedNoSymptomsCriteria = confirmedNoSymptomsCriteria;
	}

	public ClassificationCriteriaDto getConfirmedUnknownSymptomsCriteria() {
		return confirmedUnknownSymptomsCriteria;
	}

	public void setConfirmedUnknownSymptomsCriteria(ClassificationCriteriaDto confirmedUnknownSymptomsCriteria) {
		this.confirmedUnknownSymptomsCriteria = confirmedUnknownSymptomsCriteria;
	}

	public ClassificationCriteriaDto getNotACaseCriteria() {
		return notACaseCriteria;
	}

	public void setNotACaseCriteria(ClassificationCriteriaDto notACaseCriteria) {
		this.notACaseCriteria = notACaseCriteria;
	}

	public boolean hasAnyCriteria() {
		return suspectCriteria != null
			|| probableCriteria != null
			|| confirmedCriteria != null
			|| confirmedNoSymptomsCriteria != null
			|| confirmedUnknownSymptomsCriteria != null
			|| notACaseCriteria != null;
	}
}
