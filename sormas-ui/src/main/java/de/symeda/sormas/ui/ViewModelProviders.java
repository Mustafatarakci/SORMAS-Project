package de.symeda.sormas.ui;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.vaadin.navigator.View;
import com.vaadin.ui.UI;

public class ViewModelProviders {

	private Map<Class<?>, ViewModelProvider> viewModelProviders = new HashMap<Class<?>, ViewModelProvider>();

	public static <V extends View> ViewModelProvider of(Class<V> viewClass) {

		ViewModelProviders providers = getCurrent();
		if (!providers.viewModelProviders.containsKey(viewClass)) {
			providers.viewModelProviders.put(viewClass, new ViewModelProvider());
		}
		return providers.viewModelProviders.get(viewClass);
	}

	/**
	 * Gets the provider for the current UI belongs.
	 *
	 * @see UI#getCurrent()
	 *
	 * @return the current instance if available, otherwise <code>null</code>
	 */
	@NotNull
	private static ViewModelProviders getCurrent() {

		UI currentUI = UI.getCurrent();
		if (currentUI instanceof HasViewModelProviders) {
			return ((HasViewModelProviders) currentUI).getViewModelProviders();
		}
		throw new IllegalStateException("UI.getCurrent did not an instance that implements HasViewModelProviders");
	}

	public interface HasViewModelProviders {

		@NotNull
		ViewModelProviders getViewModelProviders();
	}
}
