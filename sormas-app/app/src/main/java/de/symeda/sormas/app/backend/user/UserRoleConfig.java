package de.symeda.sormas.app.backend.user;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.table.DatabaseTable;

import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;

@Entity(name = UserRoleConfig.TABLE_NAME)
@DatabaseTable(tableName = UserRoleConfig.TABLE_NAME)
public class UserRoleConfig extends AbstractDomainObject {

	private static final long serialVersionUID = 9053095630718041842L;

	public static final String TABLE_NAME = "userrolesconfig";
	public static final String I18N_PREFIX = "UserRole";

	public static final String USER_ROLE = "userRole";
	public static final String USER_RIGHTS = "userRights";

	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Column(name = "userRights", length = 1024)
	private String userRightsJson;

	// initialized from userRightsJson
	private Set<UserRight> userRights;

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getUserRightsJson() {
		return userRightsJson;
	}

	public void setUserRightsJson(String userRightsJson) {
		this.userRightsJson = userRightsJson;
		userRights = null;
	}

	@Transient // Needed for merge logic
	public Set<UserRight> getUserRights() {
		if (userRights == null) {
			Gson gson = new Gson();
			Type type = new TypeToken<Set<UserRight>>() {
			}.getType();
			userRights = gson.fromJson(userRightsJson, type);
			if (userRights == null) {
				userRights = new HashSet<>();
			}
		}
		return userRights;
	}

	public void setUserRights(Set<UserRight> userRights) {
		this.userRights = userRights;
		Gson gson = new Gson();
		userRightsJson = gson.toJson(userRights);
	}

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}
}
