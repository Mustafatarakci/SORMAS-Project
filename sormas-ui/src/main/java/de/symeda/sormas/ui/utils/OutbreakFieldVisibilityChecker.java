package de.symeda.sormas.ui.utils;

import de.symeda.sormas.api.utils.Outbreaks;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;

public class OutbreakFieldVisibilityChecker implements FieldVisibilityCheckers.FieldNameBaseChecker {

	private final ViewMode viewMode;

	public OutbreakFieldVisibilityChecker(ViewMode viewMode) {
		this.viewMode = viewMode;
	}

	@Override
	public boolean isVisible(Class<?> parentType, String propertyId) {
		return viewMode != ViewMode.SIMPLE || Outbreaks.OutbreaksConfiguration.isDefined(parentType, propertyId);
	}
}
