package de.symeda.sormas.ui.dashboard;

import static de.symeda.sormas.ui.UiUtil.permitted;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.dashboard.campaigns.CampaignDashboardView;
import de.symeda.sormas.ui.dashboard.contacts.ContactsDashboardView;
import de.symeda.sormas.ui.dashboard.surveillance.SurveillanceDashboardView;
import de.symeda.sormas.ui.utils.AbstractView;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public abstract class AbstractDashboardView extends AbstractView {

	public static final String ROOT_VIEW_NAME = "dashboard";

	protected VerticalLayout dashboardLayout;
	protected OptionGroup dashboardSwitcher = new OptionGroup();

	@SuppressWarnings("deprecation")
	protected AbstractDashboardView(String viewName) {

		super(viewName);

		addStyleName(DashboardCssStyles.DASHBOARD_SCREEN);

		CssStyles.style(dashboardSwitcher, CssStyles.FORCE_CAPTION, ValoTheme.OPTIONGROUP_HORIZONTAL, CssStyles.OPTIONGROUP_HORIZONTAL_PRIMARY);
		if (permitted(FeatureType.CASE_SURVEILANCE, UserRight.DASHBOARD_SURVEILLANCE_ACCESS)) {
			dashboardSwitcher.addItem(DashboardType.SURVEILLANCE);
			dashboardSwitcher.setItemCaption(DashboardType.SURVEILLANCE, I18nProperties.getEnumCaption(DashboardType.SURVEILLANCE));
		}
		if (permitted(FeatureType.CONTACT_TRACING, UserRight.DASHBOARD_CONTACT_ACCESS)) {
			dashboardSwitcher.addItem(DashboardType.CONTACTS);
			dashboardSwitcher.setItemCaption(DashboardType.CONTACTS, I18nProperties.getEnumCaption(DashboardType.CONTACTS));
		}
		if (permitted(FeatureType.CAMPAIGNS, UserRight.DASHBOARD_CAMPAIGNS_ACCESS)) {
			dashboardSwitcher.addItem(DashboardType.CAMPAIGNS);
			dashboardSwitcher.setItemCaption(DashboardType.CAMPAIGNS, I18nProperties.getEnumCaption(DashboardType.CAMPAIGNS));
		}
		addHeaderComponent(dashboardSwitcher);

		// Hide the dashboard switcher if only one dashboard is accessible to the user
		if (dashboardSwitcher.size() <= 1) {
			dashboardSwitcher.setVisible(false);
		}

		// Dashboard layout
		dashboardLayout = new VerticalLayout();
		dashboardLayout.setMargin(false);
		dashboardLayout.setSpacing(false);
		dashboardLayout.setSizeFull();
		dashboardLayout.setStyleName("crud-main-layout");

		addComponent(dashboardLayout);
		setExpandRatio(dashboardLayout, 1);
	}

	protected void navigateToDashboardView(Property.ValueChangeEvent e) {
		if (DashboardType.SURVEILLANCE.equals(e.getProperty().getValue())) {
			SormasUI.get().getNavigator().navigateTo(SurveillanceDashboardView.VIEW_NAME);
		} else if (DashboardType.CONTACTS.equals(e.getProperty().getValue())) {
			SormasUI.get().getNavigator().navigateTo(ContactsDashboardView.VIEW_NAME);
		} else {
			SormasUI.get().getNavigator().navigateTo(CampaignDashboardView.VIEW_NAME);
		}
	}

	public abstract void refreshDashboard();
}
