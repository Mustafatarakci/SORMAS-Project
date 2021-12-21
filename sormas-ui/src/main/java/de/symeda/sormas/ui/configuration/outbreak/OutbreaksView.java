package de.symeda.sormas.ui.configuration.outbreak;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.utils.CssStyles;

public class OutbreaksView extends AbstractConfigurationView {

	private static final long serialVersionUID = -6589135368637794263L;

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/outbreaks";

	private OutbreakOverviewGrid grid;
	private VerticalLayout contentLayout;

	public OutbreaksView() {
		super(VIEW_NAME);

		Label infoTextLabel = new Label(I18nProperties.getString(Strings.infoDefineOutbreaks));
		CssStyles.style(infoTextLabel, CssStyles.LABEL_MEDIUM);

		grid = new OutbreakOverviewGrid();

		contentLayout = new VerticalLayout();
		contentLayout.addComponent(infoTextLabel);
		contentLayout.addComponent(grid);
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		contentLayout.setSizeFull();
		contentLayout.setStyleName("crud-main-layout");
		contentLayout.setExpandRatio(grid, 1);

		addComponent(contentLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		grid.reload();
		super.enter(event);
	}
}
