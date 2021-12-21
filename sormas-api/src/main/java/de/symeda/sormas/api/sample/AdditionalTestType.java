package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AdditionalTestType {

	HAEMOGLOBINURIA(SampleMaterial.URINE),
	PROTEINURIA(SampleMaterial.URINE),
	HEMATURIA(SampleMaterial.URINE),
	ARTERIAL_VENOUS_BLOOD_GAS(SampleMaterial.BLOOD),
	ALT_SGPT(SampleMaterial.BLOOD),
	AST_SGOT(SampleMaterial.BLOOD),
	CREATININE(SampleMaterial.BLOOD),
	POTASSIUM(SampleMaterial.BLOOD),
	UREA(SampleMaterial.BLOOD),
	HAEMOGLOBIN(SampleMaterial.BLOOD),
	TOTAL_BILIRUBIN(SampleMaterial.BLOOD),
	CONJ_BILIRUBIN(SampleMaterial.BLOOD),
	WBC_COUNT(SampleMaterial.BLOOD),
	PLATELETS(SampleMaterial.BLOOD),
	PROTHROMBIN_TIME(SampleMaterial.BLOOD);

	private SampleMaterial sampleMaterial;

	AdditionalTestType(SampleMaterial sampleMaterial) {
		this.sampleMaterial = sampleMaterial;
	}

	public SampleMaterial getSampleMaterial() {
		return sampleMaterial;
	}

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public AdditionalTestType[] values(SampleMaterial sampleMaterial) {

		AdditionalTestType[] values = new AdditionalTestType[] {};
		int valuesIndex = 0;
		for (AdditionalTestType type : values()) {
			if (type.getSampleMaterial() == sampleMaterial) {
				values[valuesIndex++] = type;
			}
		}

		return values;
	}
}
