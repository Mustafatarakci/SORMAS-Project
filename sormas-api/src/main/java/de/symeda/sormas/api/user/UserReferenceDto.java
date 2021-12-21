package de.symeda.sormas.api.user;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.PersonalData;
import de.symeda.sormas.api.utils.SensitiveData;

public class UserReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -8558187171374254398L;

	@PersonalData(mandatoryField = true)
	@SensitiveData(mandatoryField = true)
	private String firstName;
	@PersonalData(mandatoryField = true)
	@SensitiveData(mandatoryField = true)
	private String lastName;

	public UserReferenceDto() {
	}

	public UserReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public UserReferenceDto(String uuid, String firstName, String lastName, Set<UserRole> userRoles) {
		setUuid(uuid);
		setCaption(buildCaption(firstName, lastName, userRoles));
		this.firstName = firstName;
		this.lastName = lastName;
	}

	protected UserReferenceDto(String uuid, String firstName, String lastName, String caption) {
		this.firstName = firstName;
		this.lastName = lastName;
		setUuid(uuid);
		setCaption(caption);
	}

	public static String buildCaption(String firstName, String lastName, Set<UserRole> userRoles) {

		StringBuilder result = new StringBuilder();
		result.append(DataHelper.toStringNullable(firstName)).append(" ").append(DataHelper.toStringNullable(lastName).toUpperCase());
		boolean first = true;
		if (userRoles != null) {
			for (UserRole userRole : userRoles) {
				if (first) {
					result.append(" - ");
					first = false;
				} else {
					result.append(", ");
				}
				result.append(userRole.toString());
			}
		}
		return result.toString();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@JsonIgnore
	public String getShortCaption() {
		return buildCaption(firstName, lastName, null);
	}
}
