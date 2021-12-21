package de.symeda.sormas.ui.dashboard.surveillance;

import com.vaadin.ui.CustomLayout;

import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.surveillance.components.statistics.CaseStatisticsComponent;
import de.symeda.sormas.ui.dashboard.surveillance.components.statistics.EventStatisticsComponent;
import de.symeda.sormas.ui.dashboard.surveillance.components.statistics.TestResultsStatisticsComponent;
import de.symeda.sormas.ui.dashboard.surveillance.components.statistics.summary.DiseaseSummaryComponent;
import de.symeda.sormas.ui.utils.LayoutUtil;

public class DiseaseStatisticsComponent extends CustomLayout {

	private static final long serialVersionUID = 6582975657305031105L;

	private final DashboardDataProvider dashboardDataProvider;

	private final CaseStatisticsComponent caseStatisticsComponent;
	private final DiseaseSummaryComponent diseaseSummaryComponent;
	private final EventStatisticsComponent eventStatisticsComponent;
	private final TestResultsStatisticsComponent testResultsStatisticsComponent;

	private static final String CASE_LOC = "case";
	private static final String OUTBREAK_LOC = "outbreak";
	private static final String EVENT_LOC = "event";
	private static final String SAMPLE_LOC = "sample";

	public DiseaseStatisticsComponent(DashboardDataProvider dashboardDataProvider) {
		this.dashboardDataProvider = dashboardDataProvider;

		setWidth(100, Unit.PERCENTAGE);

		setTemplateContents(
			LayoutUtil.fluidRow(
				LayoutUtil.fluidColumn(6, 0, 12, 0, LayoutUtil.fluidRowLocs(CASE_LOC, OUTBREAK_LOC)),
				LayoutUtil.fluidColumn(6, 0, 12, 0, LayoutUtil.fluidRowLocs(EVENT_LOC, SAMPLE_LOC))));

		caseStatisticsComponent = new CaseStatisticsComponent();
		diseaseSummaryComponent = new DiseaseSummaryComponent();
		eventStatisticsComponent = new EventStatisticsComponent();
		testResultsStatisticsComponent = new TestResultsStatisticsComponent();

		addComponent(caseStatisticsComponent, CASE_LOC);
		addComponent(diseaseSummaryComponent, OUTBREAK_LOC);
		addComponent(eventStatisticsComponent, EVENT_LOC);
		addComponent(testResultsStatisticsComponent, SAMPLE_LOC);
	}

	public void refresh() {
		caseStatisticsComponent.update(dashboardDataProvider.getCasesCountByClassification());
		diseaseSummaryComponent.update(dashboardDataProvider);
		eventStatisticsComponent.update(dashboardDataProvider.getEventCountByStatus());
		testResultsStatisticsComponent.update(dashboardDataProvider.getTestResultCountByResultType());
	}
}
