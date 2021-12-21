package de.symeda.sormas.ui.dashboard.surveillance.components.epicurve;

import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.dashboard.DashboardCriteria;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.diagram.AbstractEpiCurveComponent;
import de.symeda.sormas.ui.dashboard.surveillance.components.epicurve.builders.SurveillanceEpiCurveBuilder;
import de.symeda.sormas.ui.dashboard.surveillance.components.epicurve.builders.SurveillanceEpiCurveBuilders;
import de.symeda.sormas.ui.utils.CssStyles;

public class SurveillanceEpiCurveComponent extends AbstractEpiCurveComponent {

	private static final long serialVersionUID = 6582975657305031105L;

	private SurveillanceEpiCurveMode epiCurveSurveillanceMode;

	public SurveillanceEpiCurveComponent(DashboardDataProvider dashboardDataProvider) {
		super(dashboardDataProvider);
	}

	@Override
	protected OptionGroup createEpiCurveModeSelector() {
		if (epiCurveSurveillanceMode == null) {
			epiCurveSurveillanceMode = SurveillanceEpiCurveMode.CASE_STATUS;
		}

		OptionGroup epiCurveModeOptionGroup = new OptionGroup();
		CssStyles.style(epiCurveModeOptionGroup, ValoTheme.OPTIONGROUP_HORIZONTAL, CssStyles.OPTIONGROUP_HORIZONTAL_SUBTLE);
		epiCurveModeOptionGroup.addItems((Object[]) SurveillanceEpiCurveMode.values());
		epiCurveModeOptionGroup.setValue(epiCurveSurveillanceMode);
		epiCurveModeOptionGroup.select(epiCurveSurveillanceMode);
		epiCurveModeOptionGroup.addValueChangeListener(e -> {
			epiCurveSurveillanceMode = (SurveillanceEpiCurveMode) e.getProperty().getValue();
			clearAndFillEpiCurveChart();
		});
		return epiCurveModeOptionGroup;
	}

	@Override
	public void clearAndFillEpiCurveChart() {
		SurveillanceEpiCurveBuilder surveillanceEpiCurveBuilder =
			SurveillanceEpiCurveBuilders.getEpiCurveBuilder(epiCurveSurveillanceMode, epiCurveGrouping);
		DashboardCriteria dashboardCriteria = new DashboardCriteria().disease(dashboardDataProvider.getDisease())
			.region(dashboardDataProvider.getRegion())
			.district(dashboardDataProvider.getDistrict())
			.newCaseDateType(dashboardDataProvider.getNewCaseDateType());
		epiCurveChart.setHcjs(surveillanceEpiCurveBuilder.buildFrom(buildListOfFilteredDates(), dashboardCriteria));
	}
}
