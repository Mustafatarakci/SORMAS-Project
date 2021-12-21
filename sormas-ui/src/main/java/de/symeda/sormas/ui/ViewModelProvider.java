package de.symeda.sormas.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ViewModelProvider {

	private Map<Class<?>, Object> viewModels = new HashMap<Class<?>, Object>();

	public <M extends Object> M get(Class<M> modelClass) {
		return get(modelClass, null);
	}

	@SuppressWarnings("unchecked")
	public <M extends Object> M get(Class<M> modelClass, M defaultModel) {

		if (!viewModels.containsKey(modelClass)) {
			try {
				if (defaultModel != null) {
					viewModels.put(modelClass, defaultModel);
				} else {
					viewModels.put(modelClass, modelClass.newInstance());
				}
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (M) viewModels.get(modelClass);
	}

	public <M extends Object> void remove(Class<M> modelClass) {

		if (viewModels.containsKey(modelClass)) {
			viewModels.remove(modelClass);
		}
	}

	public <M extends Object> boolean has(Class<M> modelClass) {
		return viewModels.containsKey(modelClass);
	}

	public Collection<Object> getAll() {
		return viewModels.values();
	}
}
