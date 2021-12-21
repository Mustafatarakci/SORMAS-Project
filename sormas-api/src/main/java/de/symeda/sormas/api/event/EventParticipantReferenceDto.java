package de.symeda.sormas.api.event;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.PersonalData;

public class EventParticipantReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -8725734604520880084L;

	@PersonalData
	private String firstName;
	@PersonalData
	private String lastName;

	public EventParticipantReferenceDto() {

	}

	public EventParticipantReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public EventParticipantReferenceDto(String uuid, String firstName, String lastName) {
		super(uuid);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String getCaption() {
		return buildCaption(getUuid(), firstName, lastName);
	}

	public static String buildCaption(String uuid, String firstName, String lastName) {

		String personName = PersonDto.buildCaption(firstName, lastName);
		String shortUuid = DataHelper.getShortUuid(uuid);

		if (personName.trim().length() > 0) {
			return personName + " (" + shortUuid + ")";
		}

		return shortUuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
