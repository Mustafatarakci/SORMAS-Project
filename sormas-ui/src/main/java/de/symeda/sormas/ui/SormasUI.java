package de.symeda.sormas.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.ui.UserProvider.HasUserProvider;
import de.symeda.sormas.ui.ViewModelProviders.HasViewModelProviders;
import de.symeda.sormas.ui.utils.SormasDefaultConverterFactory;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@SuppressWarnings("serial")
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("sormas")
@Widgetset("de.symeda.sormas.SormasWidgetset")
public class SormasUI extends UI implements HasUserProvider, HasViewModelProviders {

	private final UserProvider userProvider = new UserProvider();
	private final ViewModelProviders viewModelProviders = new ViewModelProviders();

	@Override
	public void init(VaadinRequest vaadinRequest) {

		setErrorHandler(SormasErrorHandler.get());
		setLocale(vaadinRequest.getLocale());

		Responsive.makeResponsive(this);

		VaadinSession.getCurrent().setConverterFactory(new SormasDefaultConverterFactory());

		getPage().setTitle(FacadeProvider.getConfigFacade().getSormasInstanceName());

		initMainScreen();
	}

	protected void initMainScreen() {
		addStyleName(ValoTheme.UI_WITH_MENU);
		setContent(new MainScreen(SormasUI.this));
	}

	public static SormasUI get() {
		return (SormasUI) UI.getCurrent();
	}

	@Override
	public UserProvider getUserProvider() {
		return userProvider;
	}

	@Override
	public ViewModelProviders getViewModelProviders() {
		return viewModelProviders;
	}

	@WebServlet(urlPatterns = {
		"/*" }, name = "SormasUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SormasUI.class, productionMode = false)
	@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
	public static class SormasUIServlet extends VaadinServlet {

	}

	public static void refreshView() {
		get().getNavigator().navigateTo(get().getNavigator().getState());
	}
}
