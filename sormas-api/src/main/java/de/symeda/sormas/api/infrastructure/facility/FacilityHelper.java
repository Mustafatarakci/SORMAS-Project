package de.symeda.sormas.api.infrastructure.facility;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DataHelper;

public final class FacilityHelper {

	private FacilityHelper() {
		// Hide Utility Class Constructor
	}

	public static String buildFacilityString(String facilityUuid, String facilityName, String facilityDetails) {

		StringBuilder result = new StringBuilder();
		result.append(buildFacilityString(facilityUuid, facilityName));

		if (!DataHelper.isNullOrEmpty(facilityDetails)) {
			if (result.length() > 0) {
				result.append(" - ");
			}
			result.append(facilityDetails);
		}
		return result.toString();
	}

	public static String buildFacilityString(String facilityUuid, String facilityName) {

		if (facilityUuid != null) {
			if (facilityUuid.equals(FacilityDto.OTHER_FACILITY_UUID)) {
				return I18nProperties.getPrefixCaption(FacilityDto.I18N_PREFIX, FacilityDto.OTHER_FACILITY);
			}
			if (facilityUuid.equals(FacilityDto.NONE_FACILITY_UUID)) {
				return I18nProperties.getPrefixCaption(FacilityDto.I18N_PREFIX, FacilityDto.NO_FACILITY);
			}
		}

		StringBuilder caption = new StringBuilder();
		if (!DataHelper.isNullOrEmpty(facilityName)) {
			caption.append(facilityName);
		}

		return caption.toString();
	}

	public static boolean isOtherOrNoneHealthFacility(String facilityUuid) {
		return FacilityDto.OTHER_FACILITY_UUID.equals(facilityUuid) || FacilityDto.NONE_FACILITY_UUID.equals(facilityUuid);
	}
}
