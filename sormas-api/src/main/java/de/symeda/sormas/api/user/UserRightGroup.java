package de.symeda.sormas.api.user;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum UserRightGroup {

	CASE_INVESTIGATION,
	CONTACT_TRACING,
	SAMPLE_COLLECTION,
	EVENT_MANAGEMENT,
	TASK_MANAGEMENT,
	REPORTING,
	USER_MANAGEMENT,
	SYSTEM_CONFIGURATION,
	MISCELLANEOUS;

//	private List<UserRight> userRights;
//	
//	public List<UserRight> getUserRights() {
//		if (userRights == null) {
//			userRights = new ArrayList<>();
//			for (UserRight userRight : UserRight.values()) {
//				if (userRight.getUserGroup() == this) {
//					userRights.add(userRight);
//				}
//			}
//		}
//		return userRights;
//	}

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
