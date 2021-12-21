package de.symeda.sormas.ui.dashboard.surveillance;

import com.vaadin.navigator.ViewChangeListener;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.ui.dashboard.AbstractDashboardView;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.DashboardType;
import de.symeda.sormas.ui.dashboard.surveillance.components.SurveillanceFilterLayout;

@SuppressWarnings("serial")
public class SurveillanceDashboardView extends AbstractDashboardView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/surveillance";

	protected DashboardDataProvider dashboardDataProvider;
	protected SurveillanceFilterLayout filterLayout;

	protected SurveillanceOverviewLayout surveillanceOverviewLayout;
	protected SurveillanceDiseaseCarouselLayout diseaseCarouselLayout;

	public SurveillanceDashboardView() {
		super(VIEW_NAME);

		dashboardDataProvider = new DashboardDataProvider();
		if (dashboardDataProvider.getDashboardType() == null) {
			dashboardDataProvider.setDashboardType(DashboardType.SURVEILLANCE);
		}
		if (DashboardType.CONTACTS.equals(dashboardDataProvider.getDashboardType())) {
			dashboardDataProvider.setDisease(FacadeProvider.getDiseaseConfigurationFacade().getDefaultDisease());
		}
		filterLayout = new SurveillanceFilterLayout(this, dashboardDataProvider);
		filterLayout.addDateTypeValueChangeListener(e -> {
			dashboardDataProvider.setNewCaseDateType((NewCaseDateType) e.getProperty().getValue());
		});
		dashboardLayout.addComponent(filterLayout);

		dashboardSwitcher.setValue(DashboardType.SURVEILLANCE);
		dashboardSwitcher.addValueChangeListener(e -> {
			dashboardDataProvider.setDashboardType((DashboardType) e.getProperty().getValue());
			navigateToDashboardView(e);
		});

		dashboardLayout.setSpacing(false);

		//add disease burden and cases
		surveillanceOverviewLayout = new SurveillanceOverviewLayout(dashboardDataProvider);
		dashboardLayout.addComponent(surveillanceOverviewLayout);
		filterLayout.setDateFilterChangeCallback(() -> {
			surveillanceOverviewLayout.updateDifferenceComponentSubHeader();
		});

		//add diseaseCarousel and map
		diseaseCarouselLayout = new SurveillanceDiseaseCarouselLayout(dashboardDataProvider);
		dashboardLayout.addComponent(diseaseCarouselLayout);
		dashboardLayout.setExpandRatio(diseaseCarouselLayout, 1);

		diseaseCarouselLayout.setExpandListener(expanded -> {
			if (expanded) {
				dashboardLayout.removeComponent(surveillanceOverviewLayout);
			} else {
				dashboardLayout.addComponent(surveillanceOverviewLayout, 1);
			}
		});
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		refreshDashboard();
	}

	public void refreshDashboard() {
		dashboardDataProvider.refreshData();

		// Update disease burden
		if (surveillanceOverviewLayout != null)
			surveillanceOverviewLayout.refresh();

		//Update disease carousel
		if (diseaseCarouselLayout != null)
			diseaseCarouselLayout.refresh();
	}
}
