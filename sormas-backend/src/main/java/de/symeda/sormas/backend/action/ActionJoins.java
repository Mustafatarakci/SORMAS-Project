package de.symeda.sormas.backend.action;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import de.symeda.sormas.backend.event.Event;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.util.AbstractDomainObjectJoins;

public class ActionJoins<T> extends AbstractDomainObjectJoins<T, Action> {

	private Join<Action, Event> event;
	private Join<Action, User> creator;
	private Join<Action, User> lastModifiedBy;

	public ActionJoins(From<T, Action> root) {
		super(root);
	}

	public Join<Action, Event> getEvent(JoinType joinType) {
		return getOrCreate(event, Action.EVENT, joinType, this::setEvent);
	}

	private void setEvent(Join<Action, Event> event) {
		this.event = event;
	}

	public Join<Action, User> getCreator() {
		return getOrCreate(creator, Action.CREATOR_USER, JoinType.LEFT, this::setCreator);
	}

	private void setCreator(Join<Action, User> creator) {
		this.creator = creator;
	}

	public Join<Action, User> getLastModifiedBy() {
		return getOrCreate(creator, Action.LAST_MODIFIED_BY, JoinType.LEFT, this::setLastModifiedBy);
	}

	private void setLastModifiedBy(Join<Action, User> lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
