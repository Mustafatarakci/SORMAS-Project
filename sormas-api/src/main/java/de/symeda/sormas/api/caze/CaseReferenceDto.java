package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.PersonalData;

public class CaseReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = 5007131477733638086L;

	@PersonalData
	private String firstName;
	@PersonalData
	private String lastName;

	public CaseReferenceDto() {

	}

	public CaseReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public CaseReferenceDto(String uuid, String firstName, String lastName) {

		setUuid(uuid);

		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String getCaption() {
		return buildCaption(getUuid(), firstName, lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public static String buildCaption(String uuid, String firstName, String lastName) {

		String personName = PersonDto.buildCaption(firstName, lastName);
		String shortUuid = DataHelper.getShortUuid(uuid);

		if (personName.trim().length() > 0) {
			return personName + " (" + shortUuid + ")";
		}

		return shortUuid;
	}
}
