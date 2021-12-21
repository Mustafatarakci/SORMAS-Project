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
import javax.persistence.UniqueConstraint;

import de.symeda.auditlog.api.Audited;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.backend.common.AbstractDomainObject;

@Entity(name = UserRoleConfig.TABLE_NAME)
@Audited
public class UserRoleConfig extends AbstractDomainObject {

	private static final long serialVersionUID = 9053095630718041842L;

	public static final String TABLE_NAME = "userrolesconfig";

	public static final String USER_ROLE = "userRole";
	public static final String USER_RIGHTS = "userRights";

	private UserRole userRole;
	private Set<UserRight> userRights;
	private boolean enabled = true;

	@Enumerated(EnumType.STRING)
	@Column(name = USER_ROLE, unique = true, nullable = false)
	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	@ElementCollection(fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "userroles_userrights",
		joinColumns = @JoinColumn(name = "userrole_id", referencedColumnName = UserRoleConfig.ID, nullable = false),
		uniqueConstraints = @UniqueConstraint(columnNames = {
			"userrole_id",
			"userright" }))
	@Column(name = "userright", nullable = false)
	public Set<UserRight> getUserRights() {
		return userRights;
	}

	public void setUserRights(Set<UserRight> userRights) {
		this.userRights = userRights;
	}

	@Column(nullable = false)
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
