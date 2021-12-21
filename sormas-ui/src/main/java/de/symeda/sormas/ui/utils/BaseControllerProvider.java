package de.symeda.sormas.ui.utils;

import java.io.Closeable;

/**
 * Sets the app id on the server side if it has not yet been set in this request.
 * I would have liked to use CDI instead of ThreadLocal, but the ServiceLocator works static.
 * The BaseControllerProvider is held in the session!
 * So it is shared within the session between all instances of the application (tabs);
 * F5 does not reload it.
 */
public class BaseControllerProvider {

	private static ThreadLocal<BaseControllerProvider> controllerProviderThreadLocal = new ThreadLocal<>();

	protected static BaseControllerProvider get() {
		return controllerProviderThreadLocal.get();
	}

	/**
	 * Must be called if a new request was started by Vaadin.
	 * Could be controlled via the Vaadin session, for example.
	 */
	public static Closeable requestStart(BaseControllerProvider controllerProvider) {
		controllerProviderThreadLocal.set(controllerProvider);
		controllerProvider.onRequestStart();
		return BaseControllerProvider::requestEnd;
	}

	protected void onRequestStart() {
		//NOOP
	}

	public static void requestEnd() {
		controllerProviderThreadLocal.remove();
	}
}
