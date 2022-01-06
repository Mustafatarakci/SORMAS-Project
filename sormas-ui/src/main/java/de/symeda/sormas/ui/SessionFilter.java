package de.symeda.sormas.ui;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserFacade;
import de.symeda.sormas.ui.utils.BaseControllerProvider;

@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class SessionFilter implements Filter {

	@EJB
	private SessionFilterBean sessionFilterBean;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession();

		final HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("X-Content-Type-Options", "nosniff");
		res.addHeader("X-Frame-Options", "SAMEORIGIN");
		res.addHeader("Referrer-Policy", "same-origin");

		ControllerProvider controllerProvider =
			Optional.of(session).map(s -> (ControllerProvider) s.getAttribute("controllerProvider")).orElseGet(() -> {
				ControllerProvider cp = new ControllerProvider();
				session.setAttribute("controllerProvider", cp);
				return cp;
			});

		try {
			sessionFilterBean.doFilter((req, resp) -> {
				Language userLanguage =
					Optional.of(FacadeProvider.getUserFacade()).map(UserFacade::getCurrentUser).map(UserDto::getLanguage).orElse(null);
				I18nProperties.setUserLanguage(userLanguage);
				FacadeProvider.getI18nFacade().setUserLanguage(userLanguage);

				try (Closeable bc = BaseControllerProvider.requestStart(controllerProvider)) {
					chain.doFilter(req, response);
				}
			}, request, response);
		} finally {
			I18nProperties.removeUserLanguage();
			FacadeProvider.getI18nFacade().removeUserLanguage();
		}
	}
}
