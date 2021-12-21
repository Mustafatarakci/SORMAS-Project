package de.symeda.sormas.api.task;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;

public enum TaskContext {

	CASE(FeatureType.TASK_GENERATION_CASE_SURVEILLANCE, "cases", Strings.notificationTaskAssociatedCaseLink),
	CONTACT(FeatureType.TASK_GENERATION_CONTACT_TRACING, "contacts", Strings.notificationTaskAssociatedContactLink),
	EVENT(FeatureType.TASK_GENERATION_EVENT_SURVEILLANCE, "events", Strings.notificationTaskAssociatedEventLink),
	GENERAL(FeatureType.TASK_GENERATION_GENERAL, null, null),
	TRAVEL_ENTRY(FeatureType.TRAVEL_ENTRIES, "travelEntries", Strings.notificationTaskAssociatedTravelEntryLink);

	private final FeatureType featureType;
	private final String urlPattern;
	private final String associatedEntityLinkMessage;

	TaskContext(FeatureType featureType, String urlPattern, String associatedEntityLinkMessage) {
		this.featureType = featureType;
		this.urlPattern = urlPattern;
		this.associatedEntityLinkMessage = associatedEntityLinkMessage;
	}

	public FeatureType getFeatureType() {
		return featureType;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public String getAssociatedEntityLinkMessage() {
		return associatedEntityLinkMessage;
	}

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
