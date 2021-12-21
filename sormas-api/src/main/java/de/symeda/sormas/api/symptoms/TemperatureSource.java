package de.symeda.sormas.api.symptoms;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum TemperatureSource {

	AXILLARY,
	ORAL,
	RECTAL,
	NON_CONTACT;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static String formatTemperatureSource(Float temperature, TemperatureSource temperatureSource) {

		if (temperature == null) {
			return "";
		} else if (temperatureSource == null) {
			return SymptomsHelper.getTemperatureString(temperature);
		} else {
			return SymptomsHelper.getTemperatureString(temperature) + " (" + temperatureSource.toString() + ")";
		}
	}
}
