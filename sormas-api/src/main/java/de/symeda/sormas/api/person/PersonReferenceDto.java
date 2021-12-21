package de.symeda.sormas.api.person;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.utils.PersonalData;

public class PersonReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -8558187171374254398L;

	@PersonalData
	private String firstName;

	@PersonalData
	private String lastName;

	public PersonReferenceDto() {

	}

	public PersonReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public PersonReferenceDto(String uuid, String firstName, String lastName) {

		setUuid(uuid);

		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String getCaption() {
		return PersonDto.buildCaption(firstName, lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
