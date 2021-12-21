package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.Diseases;

public enum PathogenTestType {

	ANTIBODY_DETECTION,
	ANTIGEN_DETECTION,
	RAPID_TEST,
	CULTURE,
	HISTOPATHOLOGY,
	ISOLATION,
	IGM_SERUM_ANTIBODY,
	IGG_SERUM_ANTIBODY,
	IGA_SERUM_ANTIBODY,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	INCUBATION_TIME,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	INDIRECT_FLUORESCENT_ANTIBODY,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	DIRECT_FLUORESCENT_ANTIBODY,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	MICROSCOPY,
	NEUTRALIZING_ANTIBODIES,
	PCR_RT_PCR,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	GRAM_STAIN,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	LATEX_AGGLUTINATION,
	CQ_VALUE_DETECTION,
	SEQUENCING,
	DNA_MICROARRAY,
	OTHER;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static String toString(PathogenTestType value, String details) {
		if (value == null) {
			return "";
		}

		if (value == PathogenTestType.OTHER) {
			return DataHelper.toStringNullable(details);
		}

		return value.toString();
	}
}
