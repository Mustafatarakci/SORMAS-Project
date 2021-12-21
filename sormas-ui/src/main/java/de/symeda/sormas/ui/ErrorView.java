package de.symeda.sormas.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;

/**
 * View shown when trying to navigate to a view that does not exist using
 * {@link com.vaadin.navigator.Navigator}.
 * 
 * 
 */
@SuppressWarnings("serial")
public class ErrorView extends VerticalLayout implements View {

	private Label explanation;

	public ErrorView() {

		setMargin(true);
		setSpacing(true);

		Label header = new Label(I18nProperties.getString(Strings.headingViewNotFound));
		header.addStyleName(ValoTheme.LABEL_H1);
		addComponent(header);
		addComponent(explanation = new Label());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		explanation.setValue(String.format(I18nProperties.getString(Strings.errorViewNotFound), event.getViewName()));
	}
}
