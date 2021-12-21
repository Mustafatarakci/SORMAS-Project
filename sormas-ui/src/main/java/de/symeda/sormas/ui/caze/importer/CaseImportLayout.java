package de.symeda.sormas.ui.caze.importer;

import java.io.IOException;

import com.opencsv.exceptions.CsvValidationException;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.importexport.ImportFacade;
import de.symeda.sormas.api.importexport.ValueSeparator;
import de.symeda.sormas.ui.importer.AbstractImportLayout;
import de.symeda.sormas.ui.importer.ImportReceiver;

@SuppressWarnings("serial")
public class CaseImportLayout extends AbstractImportLayout {

	public CaseImportLayout() {

		super();

		ImportFacade importFacade = FacadeProvider.getImportFacade();

		addDownloadResourcesComponent(1, new ClassResource("/SORMAS_Import_Guide.pdf"));
		addDownloadImportTemplateComponent(2, importFacade.getCaseImportTemplateFilePath(), importFacade.getCaseImportTemplateFileName());
		addImportCsvComponent(3, new ImportReceiver("_case_import_", file -> {
			resetDownloadErrorReportButton();

			try {
				CaseImporter importer = new CaseImporter(file, true, currentUser, (ValueSeparator) separator.getValue());
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
