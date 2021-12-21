package de.symeda.sormas.ui.statistics;

import com.vaadin.navigator.Navigator;

import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.UserProvider;

public class StatisticsController {

	public StatisticsController() {

	}

	public void registerViews(Navigator navigator) {
		navigator.addView(StatisticsView.VIEW_NAME, StatisticsView.class);
		if (UserProvider.getCurrent().hasUserRight(UserRight.DATABASE_EXPORT_ACCESS)) {
			navigator.addView(DatabaseExportView.VIEW_NAME, DatabaseExportView.class);
		}
	}
}
