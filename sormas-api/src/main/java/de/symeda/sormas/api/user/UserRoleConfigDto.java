package de.symeda.sormas.api.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.utils.DataHelper;

public class UserRoleConfigDto extends EntityDto {

	private static final long serialVersionUID = -547459523041494446L;

	public static final String I18N_PREFIX = "UserRole";

	public static final String USER_ROLE = "userRole";
	public static final String USER_RIGHTS = "userRights";

	private UserRole userRole;
	private Set<UserRight> userRights;

	public static UserRoleConfigDto build(UserRole userRole, UserRight... userRights) {

		UserRoleConfigDto dto = new UserRoleConfigDto();
		dto.setUuid(DataHelper.createUuid());
		dto.setUserRole(userRole);
		Set<UserRight> userRightsSet = new HashSet<UserRight>();
		userRightsSet.addAll(Arrays.asList(userRights));
		dto.setUserRights(userRightsSet);
		return dto;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Set<UserRight> getUserRights() {
		return userRights;
	}

	public void setUserRights(Set<UserRight> userRights) {
		this.userRights = userRights;
	}
}
