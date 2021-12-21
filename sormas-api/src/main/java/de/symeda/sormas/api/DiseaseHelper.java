package de.symeda.sormas.api;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.caze.PlagueType;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.utils.DataHelper;

public final class DiseaseHelper {

	private DiseaseHelper() {
		// Hide Utility Class Constructor
	}

	/**
	 * Checks whether the given symptoms match the clinical criteria of one of the three Plague types.
	 */
	public static PlagueType getPlagueTypeForSymptoms(SymptomsDto symptoms) {
		if (symptoms.getFever() == SymptomState.YES) {
			if (symptoms.getPainfulLymphadenitis() == SymptomState.YES) {
				return PlagueType.BUBONIC;
			} else if (symptoms.getCough() == SymptomState.YES
				|| symptoms.getChestPain() == SymptomState.YES
				|| symptoms.getCoughingBlood() == SymptomState.YES) {
				return PlagueType.PNEUMONIC;
			} else if (symptoms.getChillsSweats() == SymptomState.YES) {
				return PlagueType.SEPTICAEMIC;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String toString(Disease disease, String diseaseDetails) {
		if (disease == null) {
			return "";
		}

		return disease != Disease.OTHER ? disease.toShortString() : DataHelper.toStringNullable(diseaseDetails);
	}

	public static String toString(Disease disease, String diseaseDetails, DiseaseVariant diseaseVariant) {
		return String.format("%s %s", toString(disease, diseaseDetails), variantInBrackets(diseaseVariant));
	}

	/**
	 * @return The disease variant caption in parenthesis or an empty string if the disease variant is null
	 */
	public static String variantInBrackets(DiseaseVariant diseaseVariant) {
		return diseaseVariant == null ? StringUtils.EMPTY : String.format("(%s)", diseaseVariant.getCaption());
	}
}
