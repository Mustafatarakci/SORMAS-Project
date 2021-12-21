package de.symeda.sormas.ui.events.eventparticipantimporter;

import java.io.IOException;

import com.opencsv.exceptions.CsvValidationException;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.importexport.ImportFacade;
import de.symeda.sormas.api.importexport.ValueSeparator;
import de.symeda.sormas.ui.importer.AbstractImportLayout;
import de.symeda.sormas.ui.importer.ImportReceiver;

@SuppressWarnings("serial")
public class EventParticipantImportLayout extends AbstractImportLayout {

	public EventParticipantImportLayout(EventReferenceDto event) {

		super();

		ImportFacade importFacade = FacadeProvider.getImportFacade();

		addDownloadResourcesComponent(1, new ClassResource("/SORMAS_Import_Guide.pdf"));
		addDownloadImportTemplateComponent(
			2,
			importFacade.getEventParticipantImportTemplateFilePath(),
			importFacade.getEventParticipantImportTemplateFileName());
		addImportCsvComponent(3, new ImportReceiver("_eventparticipant_import_", file -> {
			resetDownloadErrorReportButton();

			try {
				EventParticipantImporter importer =
					new EventParticipantImporter(file, true, currentUser, event, (ValueSeparator) separator.getValue());
				importer.startImport(this::extendDownloadErrorReportButton, currentUI, true);
			} catch (IOException | CsvValidationException e) {
				new Notification(
					I18nProperties.getString(Strings.headingImportFailed),
					I18nProperties.getString(Strings.messageImportFailed),
					Type.ERROR_MESSAGE,
					false).show(Page.getCurrent());
			}
		}));
		addDownloadErrorReportComponent(4);
	}
}
