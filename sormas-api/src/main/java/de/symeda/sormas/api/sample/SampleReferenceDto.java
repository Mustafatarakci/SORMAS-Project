package de.symeda.sormas.api.sample;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;

public class SampleReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -6975445672442728938L;

	public SampleReferenceDto() {

	}

	public SampleReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public SampleReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}

	public SampleReferenceDto(String uuid, SampleMaterial sampleMaterial, String caseUuid, String contactUuid, String eventParticipantUuid) {
		setUuid(uuid);
		setCaption(buildCaption(sampleMaterial, caseUuid, contactUuid, eventParticipantUuid));
	}

	public static String buildCaption(SampleMaterial sampleMaterial, String caseUuid, String contactUuid, String eventParticipantUuid) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DataHelper.toStringNullable(sampleMaterial));
		if (stringBuilder.length() > 0) {
			stringBuilder.append(" ");
		}
		stringBuilder.append(I18nProperties.getString(Strings.entitySample));
		if (caseUuid != null) {
			stringBuilder.append(StringUtils.wrap(I18nProperties.getString(Strings.forCase), " ")).append(DataHelper.getShortUuid(caseUuid));
		}
		if (contactUuid != null) {
			stringBuilder.append(StringUtils.wrap(I18nProperties.getString(Strings.forContact), " ")).append(DataHelper.getShortUuid(contactUuid));
		}
		if (eventParticipantUuid != null) {
			stringBuilder.append(StringUtils.wrap(I18nProperties.getString(Strings.forEventParticipant), " "))
				.append(DataHelper.getShortUuid(eventParticipantUuid));
		}
		return stringBuilder.toString();
	}
}
