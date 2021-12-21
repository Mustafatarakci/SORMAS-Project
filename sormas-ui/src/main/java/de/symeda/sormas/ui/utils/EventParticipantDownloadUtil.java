package de.symeda.sormas.ui.utils;

import java.util.Collection;
import java.util.Date;
import java.util.function.Supplier;

import com.vaadin.server.StreamResource;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantCriteria;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.event.EventParticipantExportDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.PersonDto;

public class EventParticipantDownloadUtil {

	public static StreamResource createExtendedEventParticipantExportResource(
		EventParticipantCriteria criteria,
		Supplier<Collection<String>> selectedRows,
		ExportConfigurationDto exportConfiguration) {

		return DownloadUtil.createCsvExportStreamResource(
			EventParticipantExportDto.class,
			null,
			(Integer start, Integer max) -> FacadeProvider.getEventParticipantFacade()
				.getExportList(criteria, selectedRows.get(), start, max, I18nProperties.getUserLanguage(), exportConfiguration),
			EventParticipantDownloadUtil::captionProvider,
			ExportEntityName.EVENT_PARTICIPANTS,
			exportConfiguration);
	}

	public static String getPropertyCaption(String propertyId, String prefixId) {
		if (prefixId != null) {
			return I18nProperties.getPrefixCaption(prefixId, propertyId);
		}
		return I18nProperties.findPrefixCaption(
			propertyId,
			EventParticipantExportDto.I18N_PREFIX,
			EventParticipantDto.I18N_PREFIX,
			EventDto.I18N_PREFIX,
			PersonDto.I18N_PREFIX,
			CaseDataDto.I18N_PREFIX,
			LocationDto.I18N_PREFIX);
	}

	private static String captionProvider(String propertyId, Class<?> type) {
		String caption = getPropertyCaption(propertyId, null);
		if (Date.class.isAssignableFrom(type)) {
			caption += " (" + DateFormatHelper.getDateFormatPattern() + ")";
		}

		return caption;
	}

}
