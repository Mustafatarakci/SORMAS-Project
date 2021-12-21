package de.symeda.sormas.api.utils.fieldvisibility.checkers;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.utils.Diseases;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;

public class DiseaseFieldVisibilityChecker implements FieldVisibilityCheckers.FieldNameBaseChecker {

	private final Disease disease;

	public DiseaseFieldVisibilityChecker(Disease disease) {
		this.disease = disease;
	}

	@Override
	public boolean isVisible(Class<?> parentType, String propertyId) {
		return Diseases.DiseasesConfiguration.isDefinedOrMissing(parentType, propertyId, disease);
	}
}
