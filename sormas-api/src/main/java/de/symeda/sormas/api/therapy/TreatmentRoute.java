package de.symeda.sormas.api.therapy;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum TreatmentRoute {

	ORAL,
	IV,
	RECTAL,
	TOPICAL,
	OTHER;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static String buildCaption(TreatmentRoute treatmentRoute, String routeDetails) {

		if (treatmentRoute != null && (treatmentRoute != TreatmentRoute.OTHER || StringUtils.isEmpty(routeDetails))) {
			return treatmentRoute.toString();
		} else {
			return routeDetails;
		}
	}
}
