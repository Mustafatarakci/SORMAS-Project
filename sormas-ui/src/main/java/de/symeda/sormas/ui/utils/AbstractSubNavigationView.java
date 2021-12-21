package de.symeda.sormas.ui.utils;

import java.util.Optional;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import de.symeda.sormas.ui.SubMenu;

@SuppressWarnings("serial")
public abstract class AbstractSubNavigationView<SC extends Component> extends AbstractView {

	private final SubMenu subNavigationMenu;
	private HorizontalLayout buttonsLayout;
	protected SC subComponent;

	protected AbstractSubNavigationView(String viewName) {
		super(viewName);

		subNavigationMenu = new SubMenu();
		addComponent(subNavigationMenu);
		setExpandRatio(subNavigationMenu, 0);

		createButtonsLayout().ifPresent(l -> {
			buttonsLayout = l;
			addHeaderComponent(l);
		});
	}

	protected Optional<HorizontalLayout> createButtonsLayout() {
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setMargin(false);

		return Optional.of(buttonsLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		String params = event.getParameters();
		refreshMenu(subNavigationMenu, params);
		selectInMenu();
	}

	public abstract void refreshMenu(SubMenu menu, String params);

	protected void setSubComponent(SC newComponent) {

		if (subComponent != null) {
			removeComponent(subComponent);
		}
		subComponent = newComponent;
		if (subComponent != null) {
			// Make sure that the sub component is always the first component below the navigation
			addComponent(subComponent, 2);
			setExpandRatio(subComponent, 1);
		}
	}

	public void selectInMenu() {
		subNavigationMenu.setActiveView(viewName);
	}

	public HorizontalLayout getButtonsLayout() {
		return buttonsLayout;
	}
}
