/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.sormas.e2etests.pages.application.cases;

import org.openqa.selenium.By;

public class EditCasePage {
  public static final By CALCULATE_CASE_CLASSIFICATION_BUTTON =
      By.id("caseClassificationCalculationButton");
  public static final By FOLLOW_UP_TAB = By.cssSelector("[id='tab-cases-visits'] a");
  public static final By SYMPTOMS_TAB = By.cssSelector("[id='tab-cases-symptoms']");
  public static final By THERAPY_TAB = By.cssSelector("[id='tab-cases-therapy']");
  public static final By HOSPITALIZATION_TAB = By.cssSelector("[id='tab-cases-hospitalization']");
  public static final By BACK_TO_CASES_LIST_BUTTON = By.id("tab-cases");
  public static final By REGION_INPUT = By.cssSelector("#responsibleRegion input");
  public static final By DISTRICT_INPUT = By.cssSelector("#responsibleDistrict input");
  public static final By COMMUNITY_INPUT = By.cssSelector("#responsibleCommunity input");
  public static final By PLACE_OF_STAY_SELECTED_VALUE =
      By.cssSelector("#facilityOrHome input[checked] + label");
  public static final By PLACE_DESCRIPTION_INPUT = By.cssSelector("#healthFacilityDetails");
  public static final By EXTERNAL_ID_INPUT = By.id("externalID");
  public static final By UUID_INPUT = By.id("uuid");
  public static final By DISEASE_INPUT = By.cssSelector("#disease input");
  public static final By DISEASE_VARIANT_INPUT = By.cssSelector("#diseaseVariant input");
  public static final By USER_INFORMATION =
      By.cssSelector(".v-slot-view-header .v-slot-primary div");
  public static final By CASE_TAB = By.cssSelector("div#tab-cases-data");
  public static final By CASE_PERSON_TAB = By.cssSelector("div#tab-cases-person");
  public static final By NEW_TASK_BUTTON = By.cssSelector("div#taskNewTask");
  public static final By EDIT_TASK_BUTTON = By.cssSelector("div[id*='edit-task']");
  public static final By NEW_SAMPLE_BUTTON = By.cssSelector("[id='New sample']");
  public static final By NEW_SAMPLE_BUTTON_DE = By.cssSelector("[id='Neue Probe']");
  public static final By EDIT_SAMPLE_BUTTON =
      By.cssSelector(
          "[location='samples'] [class='v-button v-widget link v-button-link compact v-button-compact']");
  public static final By REPORT_DATE_INPUT = By.cssSelector("#reportDate input");
  public static final By CASE_CLASSIFICATION_COMBOBOX = By.cssSelector("#caseClassification div");
  public static final By CASE_CLASSIFICATION_SPAN =
      By.cssSelector("#caseClassification span input:checked+label");
  public static final By CASE_CLASSIFICATION_INPUT =
      By.cssSelector("#caseClassification input:checked+label");
  public static final By CLINICAL_CONFIRMATION_COMBOBOX =
      By.cssSelector("#clinicalConfirmation div");
  public static final By EPIDEMIOLOGICAL_CONFIRMATION_COMBOBOX =
      By.cssSelector("#epidemiologicalConfirmation div");
  public static final By EPID_NUMBER_INPUT = By.cssSelector("#epidNumber input");
  public static final By LABORATORY_DIAGNOSTIC_CONFIRMATION_COMBOBOX =
      By.cssSelector("#laboratoryDiagnosticConfirmation div");
  public static final By INVESTIGATION_STATUS_OPTIONS =
      By.cssSelector("#investigationStatus label");
  public static final By INVESTIGATED_DATE_FIELD = By.cssSelector("#investigatedDate");
  public static final By EXTERNAL_TOKEN_INPUT = By.id("externalToken");
  public static final By DISEASE_COMBOBOX = By.cssSelector("#disease div");
  public static final By REINFECTION_OPTIONS = By.cssSelector("#reInfection label");
  public static final By OUTCOME_OF_CASE_OPTIONS = By.cssSelector("#outcome label");
  public static final By DATE_OF_OUTCOME = By.cssSelector("#outcomeDate");
  public static final By DATE_OF_OUTCOME_INPUT = By.cssSelector("#outcomeDate input");
  public static final By SEQUELAE_OPTIONS = By.cssSelector("#sequelae label");
  public static final By SEQUELAE_DETAILS = By.cssSelector("#sequelaeDetails");
  public static final By PREGNANCY_OPTIONS = By.cssSelector("#pregnant label");
  public static final By TRIMESTER_OPTIONS = By.cssSelector("#trimester label");
  public static final By PLACE_OF_STAY_CHECKBOX_LABEL =
      By.xpath("//*[@id='differentPlaceOfStayJurisdiction']/label");
  public static final By PLACE_OF_STAY_CHECKBOX_INPUT =
      By.xpath("//*[@id='differentPlaceOfStayJurisdiction']/input");
  public static final By PLACE_OF_STAY_REGION_COMBOBOX = By.cssSelector("#region div");
  public static final By PLACE_OF_STAY_DISTRICT_COMBOBOX = By.cssSelector("#district div");
  public static final By REPORTING_DISTRICT_COMBOBOX = By.cssSelector("#reportingDistrict div");
  public static final By CASE_IDENTIFICATION_SOURCE_COMBOBOX =
      By.cssSelector("#caseIdentificationSource div");
  public static final By PLACE_OF_STAY_OPTIONS =
      By.cssSelector("[location='facilityOrHomeLoc'] label");
  public static final By FACILITY_CATEGORY_COMBOBOX = By.cssSelector("#typeGroup div");
  public static final By FACILITY_TYPE_COMBOBOX = By.cssSelector("#facilityType div");
  public static final By FACILITY_HEALTH_COMBOBOX = By.cssSelector("#healthFacility div");
  public static final By FACILITY_HEALTH_INPUT = By.cssSelector("#healthFacility input");
  public static final By FACILITY_ACTIVITY_COMBOBOX =
      By.cssSelector("[id='typeOfPlace'] [class='v-filterselect-button']");
  public static final By REGION_COMBOBOX = By.cssSelector("#responsibleRegion div");
  public static final By DISTRICT_COMBOBOX = By.cssSelector("#responsibleDistrict div");
  public static final By COMMUNITY_COMBOBOX = By.cssSelector("#responsibleCommunity div");
  public static final By RESPONSIBLE_REGION_COMBOBOX = By.cssSelector("#responsibleRegion div");
  public static final By RESPONSIBLE_DISTRICT_COMBOBOX = By.cssSelector("#responsibleDistrict div");
  public static final By COMMUNITY_COMBOBOX_BY_PLACE_OF_STAY = By.cssSelector("#community div");
  public static final By RESPONSIBLE_COMMUNITY_COMBOBOX =
      By.cssSelector("#responsibleCommunity div");
  public static final By PROHIBITION_TO_WORK_OPTIONS = By.cssSelector("#prohibitionToWork label");
  public static final By HOME_BASED_QUARANTINE_POSSIBLE_OPTIONS =
      By.cssSelector("#quarantineHomePossible label");
  public static final By QUARANTINE_COMBOBOX = By.cssSelector("#quarantine div");
  public static final By QUARANTINE_DATE_FROM = By.cssSelector("#quarantineFrom");
  public static final By QUARANTINE_DATE_FROM_INPUT = By.cssSelector("#quarantineFrom input");
  public static final By QUARANTINE_DATE_TO = By.cssSelector("#quarantineTo");
  public static final By QUARANTINE_DATE_TO_INPUT = By.cssSelector("#quarantineTo input");
  public static final By QUARANTINE_CHANGE_COMMENT = By.cssSelector("#quarantineChangeComment");
  public static final By QUARANTINE_POPUP_MESSAGE =
      By.xpath("//div[@class='v-label v-widget v-has-width']");
  public static final By QUARANTINE_POPUP_SAVE_BUTTON =
      By.cssSelector(".popupContent #actionConfirm");
  public static final By QUARANTINE_POPUP_DISCARD_BUTTON =
      By.cssSelector(".popupContent #actionCancel");
  public static final By DISCARD_BUTTON_POPUP = By.cssSelector(".popupContent #discard");
  public static final By QUARANTINE_ORDERED_VERBALLY_CHECKBOX_LABEL =
      By.xpath("//*[@id='quarantineOrderedVerbally']/label");
  public static final By QUARANTINE_ORDERED_VERBALLY_CHECKBOX_INPUT =
      By.xpath("//*[@id='quarantineOrderedVerbally']/input");
  public static final By QUARANTINE_ORDERED_BY_DOCUMENT_CHECKBOX_LABEL =
      By.xpath("//*[@id='quarantineOrderedOfficialDocument']/label");
  public static final By QUARANTINE_ORDERED_BY_DOCUMENT_CHECKBOX_INPUT =
      By.xpath("//*[@id='quarantineOrderedOfficialDocument']/input");
  public static final By OFFICIAL_QUARANTINE_ORDER_SENT_CHECKBOX_LABEL =
      By.xpath("//*[@id='quarantineOfficialOrderSent']/label");
  public static final By OFFICIAL_QUARANTINE_ORDER_SENT_CHECKBOX_INPUT =
      By.xpath("//*[@id='quarantineOfficialOrderSent']/input");
  public static final By QUARANTINE_ORDERED_BY_DOCUMENT_DATE =
      By.cssSelector("#quarantineOrderedOfficialDocumentDate");
  public static final By DATE_OF_THE_VERBAL_ORDER =
      By.cssSelector("#quarantineOrderedVerballyDate");
  public static final By DATE_OFFICIAL_QUARANTINE_ORDER_WAS_SENT =
      By.cssSelector("#quarantineOfficialOrderSentDate");
  public static final By QUARANTINE_TYPE_DETAILS = By.cssSelector("#quarantineTypeDetails");
  public static final By REPORT_GPS_LATITUDE_INPUT = By.cssSelector("input#reportLat");
  public static final By REPORT_GPS_LONGITUDE_INPUT = By.cssSelector("input#reportLon");
  public static final By REPORT_GPS_ACCURACY_IN_M_INPUT =
      By.cssSelector("input#reportLatLonAccuracy");
  public static final By BLOOD_ORGAN_TISSUE_DONATION_IN_THE_LAST_6_MONTHS_OPTIONS =
      By.cssSelector("#bloodOrganOrTissueDonated label");
  public static final By VACCINATION_STATUS_FOR_THIS_DISEASE_COMBOBOX =
      By.cssSelector("#vaccinationStatus div");
  public static final By RESPONSIBLE_SURVEILLANCE_OFFICER_COMBOBOX =
      By.cssSelector("#surveillanceOfficer div");
  public static final By DATE_RECEIVED_AT_DISTRICT_LEVEL_INPUT =
      By.cssSelector("#districtLevelDate input");
  public static final By DATE_RECEIVED_AT_REGION_LEVEL_INPUT =
      By.cssSelector("#regionLevelDate input");
  public static final By DATE_RECEIVED_AT_NATIONAL_LEVEL_INPUT =
      By.cssSelector("#nationalLevelDate input");
  public static final By GENERAL_COMMENT_TEXTAREA = By.cssSelector("textarea#additionalDetails");
  public static final By SAVE_BUTTON = By.id("commit");
  public static final By ACTION_CANCEL = By.cssSelector(".popupContent #actionCancel");
  public static final By DELETE_BUTTON = By.id("delete");
  public static final By DELETE_POPUP_YES_BUTTON = By.cssSelector(".popupContent #actionConfirm");
  public static final By CASE_SAVED_POPUP = By.cssSelector(".v-Notification-caption");
  public static final By EXTRA_COMMENT_INPUT =
      By.cssSelector(".popupContent [class='v-textfield v-widget v-has-width']");
  public static final By CREATE_DOCUMENT_BUTTON = By.cssSelector("[id='Create']");
  public static final By CREATE_QUARANTINE_ORDER_BUTTON =
      By.cssSelector(".popupContent [id='Create']");
  public static final By EXTRA_COMMENT_TEXTAREA =
      By.cssSelector(".popupContent [class='v-textfield v-widget v-has-width']");
  public static final By QUARANTINE_ORDER_COMBOBOX =
      By.cssSelector(".popupContent div[role='combobox'] div");
  public static final By CLINICAL_COURSE_TAB = By.id("tab-cases-clinicalcourse");
  public static final By NEW_TRAVEL_ENTRY_BUTTON_DE = By.id("Neue Einreise");
  public static final By PICK_OR_CREATE_PERSON_TITLE = By.xpath("//div[@class='v-window-header']");
  public static final By SAVE_POPUP_CONTENT = By.cssSelector(".popupContent #commit");
  public static final By BACK_TO_THE_CASES_LIST_BUTTON =
      By.xpath("//div[@class='v-link v-widget v-caption v-link-v-caption']/a[@href='#!cases']");
  public static final By EDIT_TRAVEL_ENTRY_FROM_CASE_BUTTON =
      By.xpath("//div[@class='v-slot v-slot-s-list']//div[@role='button']");
  public static final By REFERENCE_DEFINITION_TEXT =
      By.cssSelector("#caseReferenceDefinition input");
  public static final By BACK_TO_CASES_BUTTON = By.id("tab-cases");
  public static final By CASE_DATA_TITLE = By.cssSelector("[location='caseDataHeadingLoc']");
  public static final By EDIT_CASE_EPIDEMIOLOGICAL_DATA = By.cssSelector("#tab-cases-epidata");
  public static final By PICK_OR_CREATE_PERSON_POPUP_HEADER =
      By.xpath("//*[contains(text(),'Pick or create person')]");
  public static final By PICK_OR_CREATE_CASE_POPUP_HEADER =
      By.xpath("//*[contains(text(),'Pick or create case')]");
  public static final By CREATE_NEW_PERSON_CHECKBOX =
      By.xpath("//label[text()='Create a new person']");
  public static final By SELECT_MATCHING_PERSON_CHECKBOX =
      By.xpath("//label[text()='Select a matching person']");
  public static final By CREATE_NEW_CASE_CHECKBOX = By.xpath("//label[text()='Create a new case']");
  public static final By CURRENT_HOSPITALIZATION_POPUP =
      By.xpath("//*[contains(text(),'Current hospitalization')]");
  public static final By SAVE_AND_OPEN_HOSPITALIZATION_BUTTON =
      By.cssSelector(".popupContent #actionConfirm");
  public static final By PREVIOUS_COVID_INFECTION_IS_KNOWN_DE_LABEL =
      By.xpath(
          "//label[text()='Genomsequenz des Virus von vorausgehender SARS-CoV-2-Infektion ist bekannt']");
  public static final By CURRENT_COVID_INFECTION_IS_KNOWN_DE_LABEL =
      By.xpath(
          "//label[text()='Genomsequenz des Virus der aktuellen SARS-CoV-2-Infektion ist bekannt']");
  public static final By PREVIOUS_AND_CURRENT_COVID_INFECTION_IS_KNOWN_DE_LABEL =
      By.xpath(
          "//label[text()='Genomsequenzen der Viren von vorausgehender und aktueller SARS-CoV-2-Infektion stimmen nicht \u00FCberein']");
  public static final By PERSON_HAS_OVERCOME_ACUTE_RESPIRATORY_DE_LABEL =
      By.xpath(
          "//label[text()='Person hat nach einer best\u00E4tigten SARS-CoV-2-Infektion die akute respiratorische Erkrankung \u00FCberwunden']");
  public static final By PERSON_HAD_AN_ASYMPTOMATIC_COVID_INFECTION_DE_LABEL =
      By.xpath("//label[text()='Person hatte eine asymptomatische SARS-CoV-2-Infektion']");
  public static final By COVID_GENOME_COPY_NUMBER_DE_LABEL =
      By.xpath(
          "//label[text()='Anzahl der SARS-CoV-2-Genomkopien im Rahmen des aktuellen PCR-Nachweises >=10^6/ml oder Ct-Wert < 30']");
  public static final By INDIVIDUAL_TESTED_POSITIVE_FOR_COVID_BY_PCR_DE_LABEL =
      By.xpath(
          "//label[text()='Person wurde mittels PCR positiv auf SARS-CoV-2 getestet, aber Anzahl der SARS-CoV-2-Genomkopien im Rahmen des aktuellen PCR-Nachweises <10^6/ml oder Ct-Wert >= 30 oder beide Angaben nicht bekannt']");
  public static final By PERSON_TESTED_CONCLUSIVELY_NEGATIVE_BY_PRC_LABEL =
      By.xpath(
          "//label[text()='Person wurde nach der vorausgehenden SARS-CoV-2-Infektion mittels PCR abschlie\u00DFend mindestens einmal negativ getestet']");
  public static final By THE_LAST_POSITIVE_PCR_DETECTION_WAS_MORE_THAN_3_MONTHS_AGO_DE_LABEL =
      By.xpath(
          "//label[text()='Der letzte positive PCR-Nachweis der vorausgehenden Infektion ist l\u00E4nger als 3 Monate zur\u00FCckliegend']");
  public static final By CREATE_A_NEW_CASE_FOR_THE_SAME_PERSON_DE_CHECKBOX =
      By.xpath("//*[text()='Neuen Fall erstellen']");
  public static final By REINFECTION_EYE_ICON = By.xpath("//span[@class='v-icon v-icon-eye']");
  public static final By TOOLTIP_EYE_ICON_HOVER = By.xpath("//div[@class='v-tooltip-text']");
  public static final By REINFECTION_STATUS_LABEL = By.cssSelector("#reinfectionStatus input");
  public static final By CREATE_DOCUMENT_TEMPLATES = By.id("Create");
  public static final By CREATE_DOCUMENT_TEMPLATES_DE = By.id("Erstellen");
  public static final By CREATE_DOCUMENT_TEMPLATES_POPUP_DE =
      By.cssSelector(".v-window #Erstellen");
  public static final By UPLOAD_DOCUMENT_CHECKBOX =
      By.xpath("//label[text()='Also upload the generated document to this entity']");
  public static final By POPUPS_INPUTS = By.cssSelector(".popupContent input");
  public static final By VACCINATION_STATUS_INPUT = By.cssSelector("#vaccinationStatus input");
  public static final By GENERATED_DOCUMENT_NAME =
      By.xpath(
          "//div[text()='Documents']/../parent::div/../../following-sibling::div//div[@class='v-label v-widget caption-truncated v-label-caption-truncated v-label-undef-w']");
  public static final By ARCHIVE_CASE_BUTTON = By.id("archiveDearchive");
  public static final By CONFIRM_ACTION = By.id("actionConfirm");
  public static final By ARCHIVE_RELATED_CONTACTS_CHECKBOX =
      By.cssSelector(".popupContent span[class='v-checkbox v-widget']");
  public static final By INFRASTRUCTURE_DATA_POPUP =
      By.xpath("//*[contains(text(),'Infrastructure data has changed')]");
  public static final By ACTION_CONFIRM = By.cssSelector("[id='actionConfirm']");
  public static final By SAMPLES_CARD_DATE_OF_COLLECTED_SAMPLE =
      By.xpath("(//div[@location='samples']//div[@class='v-label v-widget v-label-undef-w'])[1]");
  public static final By SAMPLES_CARD_LABORATORY =
      By.xpath("(//div[@location='samples']//div[@class='v-label v-widget v-label-undef-w'])[2]");
  public static final By SAMPLES_CARD_NUMBER_OF_TESTS =
      By.xpath("(//div[@location='samples']//div[@class='v-label v-widget v-label-undef-w'])[3]");
  public static final By SAMPLES_CARD_DATE_AND_TIME_OF_RESULT =
      By.xpath("(//div[@location='samples']//div[@class='v-label v-widget v-label-undef-w'])[4]");
  public static final By SAMPLES_CARD_TEST_TYPE =
      By.xpath("(//div[@location='samples']//div[@class='v-label v-widget v-label-undef-w'])[5]");
  public static final By POINT_OF_ENTRY_TEXT = By.cssSelector("#pointOfEntry input");
  public static final By POINT_OF_ENTRY_DETAILS = By.cssSelector("#pointOfEntryDetails");
  public static final By REFER_CASE_FROM_POINT_OF_ENTRY =
      By.cssSelector("#caseReferFromPointOfEntry");
  public static final By REFER_CASE_FROM_POINT_OF_ENTRY_POPUP_DE =
      By.xpath(
          "//div[contains(@class,'v-window-header') and text()='Fall vom Einreiseort weiterleiten']");
  public static final By REFER_CASE_FROM_POINT_OF_ENTRY_REGION =
      By.cssSelector(".popupContent #region div[role='button'");
  public static final By REFER_CASE_FROM_POINT_OF_ENTRY_DISTRICT =
      By.cssSelector(".popupContent #district div[role='button'");
  public static final By REFER_CASE_FROM_POINT_OF_ENTRY_SAVE_BUTTON =
      By.cssSelector(".popupContent #commit");
  public static final By CASE_ORIGIN = By.cssSelector("#caseOrigin input");

  public static By getCaseIDPathByIndex(int index) {
    return By.xpath(String.format("//table/tbody/tr[%s]/td[1]/a", index));
  }
}
