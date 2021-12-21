package de.symeda.sormas.api.therapy;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum TreatmentType {

	DRUG_INTAKE,
	ORAL_REHYDRATION_SALTS,
	BLOOD_TRANSFUSION,
	RENAL_REPLACEMENT_THERAPY,
	IV_FLUID_THERAPY,
	OXYGEN_THERAPY,
	INVASIVE_MECHANICAL_VENTILATION,
	VASOPRESSORS_INOTROPES,
	OTHER;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static String buildCaption(TreatmentType treatmentType, String treatmentDetails, TypeOfDrug typeOfDrug) {

		StringBuilder captionBuilder = new StringBuilder();
		captionBuilder.append(treatmentType.toString());
		if (!StringUtils.isEmpty(treatmentDetails)) {
			captionBuilder.append(" - ").append(treatmentDetails);
		}
		if (typeOfDrug != null && typeOfDrug != TypeOfDrug.OTHER) {
			captionBuilder.append(" - ").append(typeOfDrug.toString());
		}
		return captionBuilder.toString();
	}
}
