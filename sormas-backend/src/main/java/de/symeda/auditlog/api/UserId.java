package de.symeda.auditlog.api;

import java.io.Serializable;

/**
 * Describes the currently logged in user.
 * 
 * @author Oliver Milke
 * @since 12.11.2015
 */
public final class UserId implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;

	public UserId(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
