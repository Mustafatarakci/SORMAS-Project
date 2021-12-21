package de.symeda.sormas.api.event;

import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;

public class EventReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = 2430932452606853497L;

	public EventReferenceDto() {

	}

	public EventReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public EventReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}

	public EventReferenceDto(
		String uuid,
		Disease disease,
		String diseaseDetails,
		EventStatus eventStatus,
		EventInvestigationStatus eventInvestigationStatus,
		Date eventDate) {
		setUuid(uuid);
		setCaption(buildCaption(disease, diseaseDetails, eventStatus, eventInvestigationStatus, eventDate));
	}

	@Override
	public String getCaption() {
		return super.getCaption();
	}

	public static String buildCaption(
		Disease disease,
		String diseaseDetails,
		EventStatus eventStatus,
		EventInvestigationStatus eventInvestigationStatus,
		Date eventDate) {

		String diseaseString = disease != Disease.OTHER ? DataHelper.toStringNullable(disease) : DataHelper.toStringNullable(diseaseDetails);
		String eventStatusString = DataHelper.toStringNullable(eventStatus);
		String eventInvestigationStatusString = DataHelper.toStringNullable(eventInvestigationStatus);
		if (!diseaseString.isEmpty()) {
			eventStatusString = eventStatusString.toLowerCase();
		}

		Language language = I18nProperties.getUserLanguage();
		return diseaseString + " " + eventStatusString + " " + eventInvestigationStatusString + " " + I18nProperties.getString(Strings.on) + " "
			+ DateHelper.formatLocalDate(eventDate, language);
	}
}
