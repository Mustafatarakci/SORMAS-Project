package de.symeda.sormas.api.user;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class UserRoleTest {

	@Test
	public void testUserRolesCombinationValidity() {

		assertValidRolesCombination(UserRole.ADMIN, UserRole.NATIONAL_USER);

		assertValidRolesCombination(
			UserRole.ADMIN,
			UserRole.NATIONAL_USER,
			UserRole.LAB_USER,
			UserRole.REST_USER,
			UserRole.REST_EXTERNAL_VISITS_USER,
			UserRole.IMPORT_USER);

		assertValidRolesCombination(
			UserRole.NATIONAL_USER,
			UserRole.NATIONAL_OBSERVER,
			UserRole.NATIONAL_CLINICIAN,
			UserRole.POE_NATIONAL_USER,
			UserRole.REST_EXTERNAL_VISITS_USER);

		assertValidRolesCombination(
			UserRole.NATIONAL_USER,
			UserRole.LAB_USER);

		assertValidRolesCombination(
			UserRole.ADMIN,
			UserRole.LAB_USER);

		assertInvalidRolesCombination(
			UserRole.LAB_USER,
			UserRole.EXTERNAL_LAB_USER);

		assertInvalidRolesCombination(
			UserRole.NATIONAL_USER,
			UserRole.EXTERNAL_LAB_USER);

		assertValidRolesCombination(
			UserRole.SURVEILLANCE_SUPERVISOR,
			UserRole.CASE_SUPERVISOR,
			UserRole.CONTACT_SUPERVISOR,
			UserRole.EVENT_OFFICER,
			UserRole.STATE_OBSERVER,
			UserRole.POE_SUPERVISOR);

		assertValidRolesCombination(
			UserRole.SURVEILLANCE_OFFICER,
			UserRole.CASE_OFFICER,
			UserRole.CONTACT_OFFICER,
			UserRole.DISTRICT_OBSERVER);
		
		assertValidRolesCombination(UserRole.HOSPITAL_INFORMANT);

		assertValidRolesCombination(UserRole.COMMUNITY_INFORMANT, UserRole.IMPORT_USER);

		assertInvalidRolesCombination(UserRole.COMMUNITY_INFORMANT, UserRole.HOSPITAL_INFORMANT);
		assertInvalidRolesCombination(UserRole.ADMIN, UserRole.NATIONAL_USER, UserRole.SURVEILLANCE_SUPERVISOR);
		assertInvalidRolesCombination(UserRole.NATIONAL_USER, UserRole.EVENT_OFFICER);
	}

	@Test
	public void testUserRolesJurisdiction(){

		assertJurisdictionForRoles(JurisdictionLevel.NONE, UserRole.ADMIN, UserRole.REST_USER);
		assertJurisdictionForRoles(JurisdictionLevel.NATION, UserRole.ADMIN, UserRole.REST_USER, UserRole.NATIONAL_CLINICIAN);
		assertJurisdictionForRoles(JurisdictionLevel.DISTRICT, UserRole.ADMIN, UserRole.REST_USER, UserRole.DISTRICT_OBSERVER);
		assertJurisdictionForRoles(JurisdictionLevel.NATION, UserRole.NATIONAL_USER, UserRole.LAB_USER);
		assertJurisdictionForRoles(JurisdictionLevel.REGION, UserRole.CASE_SUPERVISOR);
		assertJurisdictionForRoles(JurisdictionLevel.LABORATORY, UserRole.LAB_USER);
		assertJurisdictionForRoles(JurisdictionLevel.LABORATORY, UserRole.ADMIN, UserRole.LAB_USER);
	}


	private void assertJurisdictionForRoles(final JurisdictionLevel jurisdictionLevel, final UserRole... userRoles) {
		Assert.assertEquals(jurisdictionLevel, UserRole.getJurisdictionLevel(Arrays.asList(userRoles)));
	}

	private void assertValidRolesCombination(final UserRole... userRoles) {
		isValidRolesCombination(true, userRoles);
	}

	private void assertInvalidRolesCombination(final UserRole... userRoles) {
		isValidRolesCombination(false, userRoles);
	}

	private void isValidRolesCombination(final Boolean isValid, final UserRole... userRoles) {
		try {
			UserRole.validate(Arrays.asList(userRoles));
		} catch (UserRole.UserRoleValidationException e) {
			if (isValid) {
				Assert.fail(e.getMessage());
			}
		}
	}
}
