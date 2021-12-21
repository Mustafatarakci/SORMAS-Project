package de.symeda.sormas.api.event;

import java.util.Arrays;
import java.util.List;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.HideForCountriesExcept;

public enum TypeOfPlace {

	FACILITY,
	@HideForCountriesExcept(countries = {})
	FACILITY_23_IFSG,
	@HideForCountriesExcept(countries = {})
	COMMUNITY_FACILITY,
	@HideForCountriesExcept(countries = {})
	FACILITY_36_IFSG,
	FESTIVITIES,
	HOME,
	MEANS_OF_TRANSPORT,
	PUBLIC_PLACE,
	SCATTERED,
	UNKNOWN,
	OTHER;

	public static final List<TypeOfPlace> FOR_CASES = Arrays.asList(FACILITY, HOME);
	public static final List<TypeOfPlace> FOR_ACTIVITY_AS_CASE_GERMANY =
		Arrays.asList(FACILITY_23_IFSG, COMMUNITY_FACILITY, FACILITY_36_IFSG, UNKNOWN, OTHER);
	private static final List<TypeOfPlace> FACILITY_TYPES = Arrays.asList(FACILITY, FACILITY_23_IFSG, COMMUNITY_FACILITY, FACILITY_36_IFSG);

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static boolean isFacilityType(Object typeOfPlace) {
		return FACILITY_TYPES.contains(typeOfPlace);
	}
}
