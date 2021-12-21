package de.symeda.sormas.ui.statistics;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;

public enum StatisticsVisualizationElementType {

	ROWS,
	COLUMNS;

	public String toString(StatisticsVisualizationType visualizationType) {
		if (visualizationType == StatisticsVisualizationType.CHART) {
			return I18nProperties.getEnumCaption(this, "Chart");
		} else {
			return I18nProperties.getEnumCaption(this);
		}
	}

	public String getEmptySelectionString(StatisticsVisualizationType visualizationType) {
		switch (this) {
		case ROWS:
			if (visualizationType == StatisticsVisualizationType.CHART) {
				return I18nProperties.getCaption(Captions.statisticsDontGroupSeries);
			} else {
				return I18nProperties.getCaption(Captions.statisticsDontGroupRows);
			}
		case COLUMNS:
			if (visualizationType == StatisticsVisualizationType.CHART) {
				return I18nProperties.getCaption(Captions.statisticsDontGroupX);
			} else {
				return I18nProperties.getCaption(Captions.statisticsDontGroupColumns);
			}
		default:
			return null;
		}
	}
}
