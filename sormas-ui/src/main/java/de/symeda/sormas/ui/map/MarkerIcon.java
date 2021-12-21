package de.symeda.sormas.ui.map;

public enum MarkerIcon {

	// make sure to update mapIcons in leaflet-connector.js when editing this
	// clustering always falls back to the lowest icon index

	CASE_CONFIRMED,
	CASE_SUSPECT,
	CASE_PROBABLE,
	CASE_UNCLASSIFIED,
	FACILITY_CONFIRMED,
	FACILITY_SUSPECT,
	FACILITY_PROBABLE,
	FACILITY_UNCLASSIFIED,
	CONTACT_LONG_OVERDUE,
	CONTACT_OVERDUE,
	CONTACT_OK,
	EVENT_OUTBREAK,
	EVENT_RUMOR;

	/**
	 * E.g. "contact long-overdue"
	 */
	private final String cssClasses;

	MarkerIcon() {
		cssClasses = this.name().toLowerCase().replaceFirst("_", " ").replaceAll("_", "-");
	}

	public String getHtmlElement(String size) {
		return "<div class='marker " + cssClasses + "' style='width:" + size + "; height:" + size + "'></div>";
	}
}
