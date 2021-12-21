package de.symeda.sormas.ui.dashboard;

import static de.symeda.sormas.ui.UiUtil.permitted;

import com.vaadin.navigator.Navigator;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.dashboard.campaigns.CampaignDashboardView;
import de.symeda.sormas.ui.dashboard.contacts.ContactsDashboardView;
import de.symeda.sormas.ui.dashboard.surveillance.SurveillanceDashboardView;

public class DashboardController {

	public DashboardController() {

	}

	public void registerViews(Navigator navigator) {
		if (permitted(FeatureType.CASE_SURVEILANCE, UserRight.DASHBOARD_SURVEILLANCE_ACCESS)) {
			navigator.addView(SurveillanceDashboardView.VIEW_NAME, SurveillanceDashboardView.class);
		}
		if (permitted(FeatureType.CONTACT_TRACING, UserRight.DASHBOARD_CONTACT_ACCESS)) {
			navigator.addView(ContactsDashboardView.VIEW_NAME, ContactsDashboardView.class);
		}
		if (permitted(FeatureType.CAMPAIGNS, UserRight.DASHBOARD_CAMPAIGNS_ACCESS)) {
			navigator.addView(CampaignDashboardView.VIEW_NAME, CampaignDashboardView.class);
		}
	}
}
