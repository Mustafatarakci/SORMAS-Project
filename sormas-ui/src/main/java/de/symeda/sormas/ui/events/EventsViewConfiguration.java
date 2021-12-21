package de.symeda.sormas.ui.events;

import de.symeda.sormas.ui.utils.ViewConfiguration;

public class EventsViewConfiguration extends ViewConfiguration {

	private EventsViewType viewType;

	public EventsViewType getViewType() {
		return viewType;
	}

	public void setViewType(EventsViewType viewType) {
		this.viewType = viewType;
	}
}
