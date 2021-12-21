package de.symeda.sormas.ui.dashboard.surveillance.components.disease.burden;

import java.util.List;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.shared.ui.grid.HeightMode;

import de.symeda.sormas.api.disease.DiseaseBurdenDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.dashboard.DiseaseBurdenGrid;
import de.symeda.sormas.ui.utils.CssStyles;

public class DiseaseBurdenComponent extends VerticalLayout {

	private static final long serialVersionUID = 6582975657305031105L;

	private DiseaseBurdenGrid grid;

	public DiseaseBurdenComponent() {

		Label title = new Label(I18nProperties.getCaption(Captions.dashboardDiseaseBurdenInfo));
		CssStyles.style(title, CssStyles.H2, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_NONE);

		grid = new DiseaseBurdenGrid();
		grid.setHeightMode(HeightMode.ROW);
		grid.setWidth(100, Unit.PERCENTAGE);

		// layout
		setWidth(100, Unit.PERCENTAGE);

		addComponent(title);
		addComponent(grid);
		setMargin(new MarginInfo(true, true, false, true));
		setSpacing(false);
		setExpandRatio(grid, 1);
	}

	public void refresh(List<DiseaseBurdenDto> diseasesBurden) {
		grid.reload(diseasesBurden);
		grid.setHeightByRows(diseasesBurden.size());
	}
}
