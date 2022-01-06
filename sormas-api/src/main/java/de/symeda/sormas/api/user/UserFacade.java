package de.symeda.sormas.api.user;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.task.TaskContextIndex;
import de.symeda.sormas.api.travelentry.TravelEntryReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface UserFacade {

	UserDto getByUuid(String uuid);

	UserDto saveUser(@Valid UserDto dto);

	boolean isLoginUnique(String uuid, String userName);

	String resetPassword(String uuid);

	List<UserDto> getAllAfter(Date date);

	UserDto getByUserName(String userName);

	List<UserReferenceDto> getUsersByRegionAndRoles(RegionReferenceDto regionRef, UserRole... assignableRoles);

	List<UserReferenceDto> getUsersByRegionsAndRoles(List<RegionReferenceDto> regionRefs, UserRole... assignableRoles);

	List<UserReferenceDto> getUsersWithSuperiorJurisdiction(UserDto user);

	List<UserDto> getIndexList(UserCriteria userCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	Page<UserDto> getIndexPage(UserCriteria userCriteria, int offset, int size, List<SortProperty> sortProperties);

	long count(UserCriteria userCriteria);

	/**
	 * @param district
	 * @param includeSupervisors
	 *            independent from the district
	 * @param userRoles
	 *            roles of the users by district
	 * @return
	 */
	List<UserReferenceDto> getUserRefsByDistrict(DistrictReferenceDto district, boolean includeSupervisors, UserRole... userRoles);

	List<UserReferenceDto> getUserRefsByDistricts(List<DistrictReferenceDto> districts, boolean includeSupervisors, UserRole... userRoles);

	List<UserReferenceDto> getAllUserRefs(boolean includeInactive);

	List<UserDto> getUsersByAssociatedOfficer(UserReferenceDto associatedOfficer, UserRole... userRoles);

	List<String> getAllUuids();

	List<UserDto> getByUuids(List<String> uuids);

	UserDto getCurrentUser();

	UserReferenceDto getCurrentUserAsReference();

	Set<UserRole> getValidLoginRoles(String userName, String password);

	void removeUserAsSurveillanceAndContactOfficer(String userUuid);

	UserSyncResult syncUser(String userUuid);

	List<UserDto> getUsersWithDefaultPassword();

	void enableUsers(List<String> userUuids);

	void disableUsers(List<String> userUuids);

	List<UserReferenceDto> getUsersHavingCaseInJurisdiction(CaseReferenceDto caseReferenceDto);

	List<UserReferenceDto> getUsersHavingContactInJurisdiction(ContactReferenceDto contactReferenceDto);

	List<UserReferenceDto> getUsersHavingEventInJurisdiction(EventReferenceDto event);

	List<UserReferenceDto> getUsersHavingTravelEntryInJurisdiction(TravelEntryReferenceDto travelEntryReferenceDto);

	List<UserReferenceWithTaskNumbersDto> getAssignableUsersWithTaskNumbers(@NotNull TaskContextIndex taskContextIndex);

	List<UserReferenceDto> getUsersByRegionAndRight(RegionReferenceDto region, UserRight userRight);
}
