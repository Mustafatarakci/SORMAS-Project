package de.symeda.sormas.ui;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import com.vaadin.ui.UI;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.user.UserRole;

public class UserProvider {

	private UserDto user;
	private UserReferenceDto userReference;
	private Set<UserRight> userRights;

	public UserDto getUser() {

		if (user == null) {
			user = FacadeProvider.getUserFacade().getCurrentUser();
		}
		return user;
	}

	public Set<UserRight> getUserRights() {

		if (userRights == null) {
			userRights = FacadeProvider.getUserRoleConfigFacade().getEffectiveUserRights(getUser().getUserRoles().toArray(new UserRole[] {}));
		}
		return userRights;
	}

	public Set<UserRole> getUserRoles() {
		return getUser().getUserRoles();
	}

	public boolean hasUserRole(UserRole userRole) {
		return getUser().getUserRoles().contains(userRole);
	}

	/**
	 * Checks if the User possesses any of the specified userRoles
	 */
	public boolean hasAnyUserRole(UserRole... userRoles) {
		Set<UserRole> currentUserRoles = getUser().getUserRoles();
		return Arrays.stream(userRoles).anyMatch(currentUserRoles::contains);
	}

	public boolean hasUserRight(UserRight userRight) {
		return getUserRights().contains(userRight);
	}

	public boolean hasAllUserRights(UserRight... userRights) {
		return getUserRights().containsAll(Arrays.asList(userRights));
	}

	public boolean hasNationalJurisdictionLevel() {
		return UserRole.getJurisdictionLevel(getCurrent().getUserRoles()) == JurisdictionLevel.NATION;
	}

	public boolean hasRegion(RegionReferenceDto regionReference) {
		RegionReferenceDto userRegionReference = getCurrent().getUser().getRegion();
		return Objects.equals(userRegionReference, regionReference);
	}

	public UserReferenceDto getUserReference() {

		if (userReference == null) {
			userReference = getUser().toReference();
		}
		return userReference;
	}

	public String getUuid() {
		return getUser().getUuid();
	}

	public String getUserName() {
		return getUser().getName();
	}

	public boolean isCurrentUser(UserDto user) {
		return getUser().equals(user);
	}

	/**
	 * Gets the user to which the current UI belongs. This is automatically defined
	 * when processing requests to the server. In other cases, (e.g. from background
	 * threads), the current UI is not automatically defined.
	 *
	 * @see UI#getCurrent()
	 *
	 * @return the current user instance if available, otherwise <code>null</code>
	 */
	public static UserProvider getCurrent() {

		UI currentUI = UI.getCurrent();
		if (currentUI instanceof HasUserProvider) {
			return ((HasUserProvider) currentUI).getUserProvider();
		}
		return null;
	}

	public interface HasUserProvider {

		UserProvider getUserProvider();
	}
}
