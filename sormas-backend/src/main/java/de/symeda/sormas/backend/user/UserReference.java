package de.symeda.sormas.backend.user;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Immutable;

import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.backend.common.AbstractDomainObject;

/**
 * This is a <strong>slim read-only copy</strong> of {@link User} to load user data without instantiating large JPA entity trees.<br />
 * The main reason this is not done by DTO projection is because the collections like {@code userRoles} cannot be queried there.
 */
@Entity
@Immutable
@Table(name = "users")
public class UserReference extends AbstractDomainObject {

	private static final long serialVersionUID = 9025694116880610101L;

	private boolean active;
	private String firstName;
	private String lastName;
	private Set<UserRole> userRoles;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = User.TABLE_NAME_USERROLES,
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = User.ID, nullable = false),
		uniqueConstraints = @UniqueConstraint(columnNames = {
			"user_id",
			"userrole" }))
	@Column(name = "userrole", nullable = false)
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	@Transient
	public String getName() {
		return firstName + " " + lastName;
	}
}
