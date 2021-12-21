package de.symeda.sormas.api.action;

import java.io.Serializable;

import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class ActionCriteria extends BaseCriteria implements Serializable {

	private static final long serialVersionUID = -9174165215694877624L;

	private ActionStatus actionStatus;
	private EventReferenceDto event;

	public ActionStatus getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(ActionStatus actionStatus) {
		this.actionStatus = actionStatus;
	}

	public ActionCriteria actionStatus(ActionStatus actionStatus) {
		setActionStatus(actionStatus);
		return this;
	}

	public ActionCriteria event(EventReferenceDto event) {
		this.event = event;
		return this;
	}

	@IgnoreForUrl
	public EventReferenceDto getEvent() {
		return event;
	}

	public boolean hasContextCriteria() {
		return getEvent() != null;
	}
}
