package de.symeda.sormas.backend.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import de.symeda.sormas.api.user.UserHelper;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.api.utils.PasswordHelper;
import de.symeda.sormas.backend.user.User;

public final class MockDataGenerator {

	private MockDataGenerator() {
		// Hide Utility Class Constructor
	}

	public static User createUser(UserRole userRole, String firstName, String lastName, String password) {
		Set<UserRole> userRoles = userRole != null ? Collections.singleton(userRole) : null;
		return createUser(userRoles, firstName, lastName, password);
	}

	public static User createUser(Set<UserRole> userRoles, String firstName, String lastName, String password) {

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		if (CollectionUtils.isNotEmpty(userRoles)) {
			user.setUserRoles(new HashSet<>(userRoles));
		}
		user.updateJurisdictionLevel();
		user.setUserName(UserHelper.getSuggestedUsername(user.getFirstName(), user.getLastName()));
		user.setSeed(PasswordHelper.createPass(16));
		user.setPassword(PasswordHelper.encodePassword(password, user.getSeed()));
		return user;
	}
}
