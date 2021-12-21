package de.symeda.sormas.ui.statistics;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.SubMenu;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractSubNavigationView;
import de.symeda.sormas.ui.utils.CssStyles;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class AbstractStatisticsView extends AbstractSubNavigationView<Component> {

	public static final String ROOT_VIEW_NAME = "statistics";

	protected AbstractStatisticsView(String viewName) {
		super(viewName);
	}

	@Override
	public void refreshMenu(SubMenu menu, String params) {
		menu.removeAllViews();
		menu.addView(StatisticsView.VIEW_NAME, I18nProperties.getCaption(Captions.statisticsStatistics), params);
		if (UserProvider.getCurrent().hasUserRight(UserRight.DATABASE_EXPORT_ACCESS)) {
			menu.addView(DatabaseExportView.VIEW_NAME, I18nProperties.getCaption(Captions.statisticsDatabaseExport), params);
		}

		String sormasStatsUrl = FacadeProvider.getConfigFacade().getSormasStatsUrl();
		if (StringUtils.isNotBlank(sormasStatsUrl)) {
			Link sormasStatsLink = new Link(
				I18nProperties.getCaption(Captions.statisticsOpenSormasStats),
				new ExternalResource(sormasStatsUrl));
			sormasStatsLink.addStyleNames(CssStyles.LINK_BUTTON, CssStyles.LINK_BUTTON_PRIMARY);
			this.addHeaderComponent(sormasStatsLink);
		}
	}
}
