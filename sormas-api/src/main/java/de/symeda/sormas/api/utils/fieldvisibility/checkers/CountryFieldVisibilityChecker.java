package de.symeda.sormas.api.utils.fieldvisibility.checkers;

import java.lang.reflect.AccessibleObject;

import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.utils.HideForCountries;
import de.symeda.sormas.api.utils.HideForCountriesExcept;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;

public class CountryFieldVisibilityChecker implements FieldVisibilityCheckers.FieldBasedChecker {

	private final String countryLocale;

	public CountryFieldVisibilityChecker(String countryLocale) {
		this.countryLocale = countryLocale;
	}

	@Override
	public boolean isVisible(AccessibleObject accessibleObject) {
		if (accessibleObject.isAnnotationPresent(HideForCountries.class)) {
			String[] countries = accessibleObject.getAnnotation(HideForCountries.class).countries();
			return !CountryHelper.isInCountries(countryLocale, countries);
		}

		if (accessibleObject.isAnnotationPresent(HideForCountriesExcept.class)) {
			String[] countries = accessibleObject.getAnnotation(HideForCountriesExcept.class).countries();
			return CountryHelper.isInCountries(countryLocale, countries);
		}

		return true;
	}
}
