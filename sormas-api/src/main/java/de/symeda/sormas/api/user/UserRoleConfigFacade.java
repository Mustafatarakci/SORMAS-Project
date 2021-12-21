package de.symeda.sormas.api.user;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;
import javax.validation.Valid;

@Remote
public interface UserRoleConfigFacade {

	List<UserRoleConfigDto> getAllAfter(Date date);

	List<UserRoleConfigDto> getAll();

	List<String> getAllUuids();

	List<String> getDeletedUuids(Date date);

	UserRoleConfigDto getByUuid(String uuid);

	UserRoleConfigDto saveUserRoleConfig(@Valid UserRoleConfigDto dto);

	void deleteUserRoleConfig(UserRoleConfigDto dto);

	/**
	 * Will fallback to default user rights for each role that has no configuration defined
	 */
	Set<UserRight> getEffectiveUserRights(UserRole... userRoles);

	Set<UserRole> getEnabledUserRoles();

	Map<UserRole, Set<UserRight>> getAllAsMap();
}
