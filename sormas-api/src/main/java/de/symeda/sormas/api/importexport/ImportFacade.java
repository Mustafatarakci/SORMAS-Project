package de.symeda.sormas.api.importexport;

import java.io.IOException;
import java.net.URI;

import javax.ejb.Remote;

@Remote
public interface ImportFacade {

	String ACTIVE_DISEASES_PLACEHOLDER = "${activeDiseases}";

	/**
	 * Creates a .csv file with one row containing all relevant column names of the case entity
	 * and its sub-entities and returns the path to the .csv file that can then be used to offer
	 * it as a download.
	 */
	void generateCaseImportTemplateFile() throws IOException;

	void generateEventImportTemplateFile() throws IOException;

	void generateEventParticipantImportTemplateFile() throws IOException;

	void generateCampaignFormImportTemplateFile(String campaignFormUuid) throws IOException;

	void generateCaseContactImportTemplateFile() throws IOException;

	void generateCaseLineListingImportTemplateFile() throws IOException;

	void generatePointOfEntryImportTemplateFile() throws IOException;

	void generatePopulationDataImportTemplateFile() throws IOException;

	void generateAreaImportTemplateFile() throws IOException;

	void generateContinentImportTemplateFile() throws IOException;

	void generateSubcontinentImportTemplateFile() throws IOException;

	void generateCountryImportTemplateFile() throws IOException;

	void generateRegionImportTemplateFile() throws IOException;

	void generateDistrictImportTemplateFile() throws IOException;

	void generateCommunityImportTemplateFile() throws IOException;

	void generateFacilityImportTemplateFile() throws IOException;

	void generateContactImportTemplateFile() throws IOException;

	String getCaseImportTemplateFileName();

	String getCaseImportTemplateFilePath();

	String getEventImportTemplateFileName();

	String getEventImportTemplateFilePath();

	String getEventParticipantImportTemplateFileName();

	String getEventParticipantImportTemplateFilePath();

	String getCampaignFormImportTemplateFilePath();

	String getPointOfEntryImportTemplateFileName();

	String getPointOfEntryImportTemplateFilePath();

	String getPopulationDataImportTemplateFileName();

	String getPopulationDataImportTemplateFilePath();

	String getCaseLineListingImportTemplateFileName();

	String getCaseLineListingImportTemplateFilePath();

	String getAreaImportTemplateFileName();

	String getAreaImportTemplateFilePath();

	String getContinentImportTemplateFileName();

	String getContinentImportTemplateFilePath();

	String getSubcontinentImportTemplateFileName();

	String getSubcontinentImportTemplateFilePath();

	String getCountryImportTemplateFileName();

	String getCountryImportTemplateFilePath();

	URI getAllCountriesImportFilePath();

	URI getAllSubcontinentsImportFilePath();

	URI getAllContinentsImportFilePath();

	String getRegionImportTemplateFileName();

	String getRegionImportTemplateFilePath();

	String getDistrictImportTemplateFileName();

	String getDistrictImportTemplateFilePath();

	String getCommunityImportTemplateFileName();

	String getCommunityImportTemplateFilePath();

	String getFacilityImportTemplateFileName();

	String getFacilityImportTemplateFilePath();

	String getCaseContactImportTemplateFileName();

	String getCaseContactImportTemplateFilePath();

	String getContactImportTemplateFileName();

	String getContactImportTemplateFilePath();

	String getImportTemplateContent(String templateFilePath) throws IOException;
}
