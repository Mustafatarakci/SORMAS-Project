package de.symeda.sormas.api.user;

import java.io.Serializable;

import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class UserRoleCriteria extends BaseCriteria implements Serializable {

	private Boolean enabled;
	private UserRight userRight;
	private String freeText;

	public UserRoleCriteria enabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public UserRoleCriteria userRight(UserRight userRight) {
		this.userRight = userRight;
		return this;
	}

	public UserRight getUserRight() {
		return userRight;
	}

	@IgnoreForUrl
	public String getFreeText() {
		return freeText;
	}
}
