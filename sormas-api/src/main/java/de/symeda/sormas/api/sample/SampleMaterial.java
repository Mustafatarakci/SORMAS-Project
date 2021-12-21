package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.Diseases;

public enum SampleMaterial {

	BLOOD,
	SERA,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	STOOL,
	NASAL_SWAB,
	THROAT_SWAB,
	NP_SWAB,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	RECTAL_SWAB,
	CEREBROSPINAL_FLUID,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	CRUST,
	TISSUE,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	URINE,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	CORNEA_PM,
	SALIVA,
	URINE_PM,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	NUCHAL_SKIN_BIOPSY,
	SPUTUM,
	ENDOTRACHEAL_ASPIRATE,
	BRONCHOALVEOLAR_LAVAGE,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	BRAIN_TISSUE,
	ANTERIOR_NARES_SWAB,
	OP_ASPIRATE,
	NP_ASPIRATE,
	PLEURAL_FLUID,
	OTHER;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static String toString(SampleMaterial value, String details) {

		if (value == null) {
			return "";
		}

		if (value == SampleMaterial.OTHER) {
			return DataHelper.toStringNullable(details);
		}

		return value.toString();
	}
}
