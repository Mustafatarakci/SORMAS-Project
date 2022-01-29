/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package org.sormas.e2etests.steps.web.application.contacts;

import static org.sormas.e2etests.pages.application.cases.EpidemiologicalDataCasePage.*;
import static org.sormas.e2etests.pages.application.contacts.ContactDirectoryPage.*;
import static org.sormas.e2etests.pages.application.contacts.CreateNewContactPage.FIRST_NAME_OF_CONTACT_PERSON_INPUT;
import static org.sormas.e2etests.pages.application.contacts.EditContactPage.UUID_INPUT;

import cucumber.api.java8.En;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import org.junit.rules.Timeout;
import org.openqa.selenium.By;
import org.sormas.e2etests.enums.TypeOfActivity;
import org.sormas.e2etests.enums.TypeOfGathering;
import org.sormas.e2etests.enums.YesNoUnknownOptions;
import org.sormas.e2etests.enums.cases.epidemiologicalData.ExposureDetailsRole;
import org.sormas.e2etests.enums.cases.epidemiologicalData.TypeOfActivityExposure;
import org.sormas.e2etests.enums.cases.epidemiologicalData.TypeOfPlace;
import org.sormas.e2etests.helpers.AssertHelpers;
import org.sormas.e2etests.helpers.WebDriverHelpers;
import org.sormas.e2etests.pojo.helpers.ComparisonHelper;
import org.sormas.e2etests.pojo.web.EpidemiologicalData;
import org.sormas.e2etests.pojo.web.epidemiologicalData.Exposure;
import org.sormas.e2etests.services.ContactService;
import org.sormas.e2etests.state.ApiState;
import org.testng.Assert;

public class ContactDirectorySteps implements En {

    protected WebDriverHelpers webDriverHelpers;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    public static Exposure exposureData;
    public static EpidemiologicalData dataSavedFromCheckbox;
    public static EpidemiologicalData specificCaseData;

    @Inject
    public ContactDirectorySteps(
            WebDriverHelpers webDriverHelpers,
            ApiState apiState,
            AssertHelpers assertHelpers,
            ContactService contactService,
            @Named("ENVIRONMENT_URL") String environmentUrl) {
        this.webDriverHelpers = webDriverHelpers;

        When(
                "I click Apply button for Contacts",
                () -> {
                    webDriverHelpers.clickOnWebElementBySelector(APPLY_FILTERS_BUTTON);
                });

        When(
                "^I navigate to the last created contact via the url$",
                () -> {
                    String LAST_CREATED_CONTACT_URL =
                            environmentUrl
                                    + "/sormas-webdriver/#!contacts/data/"
                                    + apiState.getCreatedContact().getUuid();
                    webDriverHelpers.accessWebSite(LAST_CREATED_CONTACT_URL);
                    webDriverHelpers.waitUntilIdentifiedElementIsVisibleAndClickable(UUID_INPUT);
                });

        When(
                "I click on the NEW CONTACT button",
                () ->
                        webDriverHelpers.clickWhileOtherButtonIsDisplayed(
                                NEW_CONTACT_BUTTON, FIRST_NAME_OF_CONTACT_PERSON_INPUT));

        When(
                "I click on the DETAILED radiobutton from Contact directory",
                () -> {
                    webDriverHelpers.clickOnWebElementBySelector(CONTACT_DIRECTORY_DETAILED_RADIOBUTTON);
                    webDriverHelpers.waitUntilIdentifiedElementIsVisibleAndClickable(
                            By.xpath(String.format(RESULTS_GRID_HEADER, "Sex")), 20);
                    webDriverHelpers.waitUntilANumberOfElementsAreVisibleAndClickable(GRID_HEADERS, 18);
                });

        When(
                "I filter by Contact uuid",
                () -> {
                    String contactUuid = apiState.getCreatedContact().getUuid();
                    By uuidLocator = By.cssSelector(String.format(CONTACT_RESULTS_UUID_LOCATOR, contactUuid));
                    webDriverHelpers.fillAndSubmitInWebElement(
                            CONTACT_DIRECTORY_DETAILED_PAGE_FILTER_INPUT, contactUuid);
                    webDriverHelpers.waitForPageLoadingSpinnerToDisappear(20);
                    webDriverHelpers.waitUntilElementIsVisibleAndClickable(uuidLocator);
                });

        When(
                "^I click on Line Listing button$",
                () -> webDriverHelpers.clickOnWebElementBySelector(LINE_LISTING));

        When(
                "I click on New Entry button in Exposure Details Known",
                () -> {
                    webDriverHelpers.clickOnWebElementBySelector(NEW_ENTRY_EPIDEMIOLOGICAL_DATA);
                    TimeUnit.SECONDS.sleep(4);
                });
        When(
                "I click on the Epidemiological Data button",
                () -> {
                    webDriverHelpers.clickOnWebElementBySelector(EPIDEMIOLOGICAL_DATA_TAB);
                    webDriverHelpers.waitForPageLoadingSpinnerToDisappear(40);
                });
        When(
                "I click on Indoors with {string} option in Exposure Details",
                (String indoorsOption) -> {
                    webDriverHelpers.clickWebElementByText(INDOORS_OPTION, indoorsOption);
                    webDriverHelpers.waitForPageLoadingSpinnerToDisappear(40);
                });

        When(
                "I select a type of activity from combobox in Exposure",
                () -> {
                    loopByActivityTypes();
                    TimeUnit.SECONDS.sleep(2);
                });

        When(
                "I select a type of gathering from combobox in Exposure",
                () -> {
                    loopByGatheringType();
                    TimeUnit.SECONDS.sleep(2);
                });

        When(
                "I select a type of place from combobox in Exposure",
                () -> {
                    loopByTypeOfPlace();
                    TimeUnit.SECONDS.sleep(2);
                });

        When(
                "I select a type of gathering {string} from combobox in Exposure",
                (String option) -> {
                    webDriverHelpers.selectFromCombobox(TYPE_OF_GATHERING_COMBOBOX, option);
                    TimeUnit.SECONDS.sleep(2);
                });

        When(
                "I select a type of activity {string} in Exposure",
                (String option) -> {
                    webDriverHelpers.selectFromCombobox(TYPE_OF_ACTIVITY_COMBOBOX, option);
                    TimeUnit.SECONDS.sleep(2);
                });

        When(
                "I fill a type of activity details in Exposure by {string} TEXT",
                (String typeOfActivityDetails) -> {
                    webDriverHelpers.fillInWebElement(TYPE_OF_ACTIVITY_DETAILS, typeOfActivityDetails);
                    TimeUnit.SECONDS.sleep(2);
                    // webDriverHelpers.clickOnWebElementBySelector(DONE_BUTTON);
                });

        When(
                "I fill a type of gathering details in Exposure by {string} TEXT",
                (String typeOfGatheringDetails) -> {
                    webDriverHelpers.fillInWebElement(TYPE_OF_GATHERING_DETAILS, typeOfGatheringDetails);
                    TimeUnit.SECONDS.sleep(2);
                });

        When(
                "I fill all the data in Exposure for Epidemiological data tab in Contacts",
                () -> {
                    webDriverHelpers.clickWebElementByText(EXPOSURE_DETAILS_KNOWN_OPTIONS, "YES");
                    webDriverHelpers.waitUntilElementIsVisibleAndClickable(EXPOSURE_DETAILS_NEW_ENTRY_BUTTON);
                    webDriverHelpers.clickOnWebElementBySelector(EXPOSURE_DETAILS_NEW_ENTRY_BUTTON);
                    exposureData = contactService.buildGeneratedExposureDataForContact();
                    fillExposure(exposureData);
                });

        When(
                "I am checking all Exposure data is saved and displayed in Contacts",
                () -> {
                    webDriverHelpers.waitUntilElementIsVisibleAndClickable(OPEN_SAVED_EXPOSURE_BUTTON);
                    webDriverHelpers.clickOnWebElementBySelector(OPEN_SAVED_EXPOSURE_BUTTON);
                    webDriverHelpers.waitUntilIdentifiedElementIsPresent(START_OF_EXPOSURE_INPUT);
                    Exposure actualExposureData = collectExposureData();
                    ComparisonHelper.compareEqualEntities(exposureData, actualExposureData);
                });

        When(
                "I click on Residing or working in an area with high risk of transmission of the disease in Contact with ([^\"]*) option",
                (String option) -> {
                    dataSavedFromCheckbox =
                            EpidemiologicalData.builder()
                                    .residingAreaWithRisk(YesNoUnknownOptions.valueOf(option))
                                    .build();
                    webDriverHelpers.clickWebElementByText(RESIDING_OR_WORKING_DETAILS_KNOWN_OPTIONS, option);
                });

        When(
                "I click on Residing or travelling to countries, territories, areas experiencing larger outbreaks of local transmission in Contact with ([^\"]*) option",
                (String option) -> {
                    dataSavedFromCheckbox =
                            dataSavedFromCheckbox.toBuilder()
                                    .largeOutbreaksArea(YesNoUnknownOptions.valueOf(option))
                                    .build();
                    webDriverHelpers.clickWebElementByText(
                            RESIDING_OR_TRAVELING_DETAILS_KNOWN_OPTIONS, option);
                });

        When(
                "I am checking if options in checkbox for Contact are displayed correctly",
                () -> {
                    specificCaseData = collectSpecificData();
                    ComparisonHelper.compareEqualFieldsOfEntities(
                            specificCaseData,
                            dataSavedFromCheckbox,
                            List.of("residingAreaWithRisk", "largeOutbreaksArea"));
                });

        When(
                "I open the last created contact",
                () -> {
                    searchAfterContactByMultipleOptions(apiState.getCreatedContact().getUuid());
                    openContactFromResultsByUUID(apiState.getCreatedContact().getUuid());
                });

        When(
                "I open the first contact from contacts list",
                () -> webDriverHelpers.clickOnWebElementBySelector(FIRST_CONTACT_ID_BUTTON));

        Then(
                "I check that number of displayed contact results is (\\d+)",
                (Integer number) -> {
                    webDriverHelpers.waitUntilIdentifiedElementIsPresent(NEW_CONTACT_BUTTON);
                    assertHelpers.assertWithPoll20Second(
                            () ->
                                    Assert.assertEquals(
                                            webDriverHelpers.getNumberOfElements(CONTACT_GRID_RESULTS_ROWS),
                                            number.intValue(),
                                            "Number of displayed contacts is not correct"));
                });
    }

    private void searchAfterContactByMultipleOptions(String idPhoneNameEmail) {
        webDriverHelpers.waitUntilElementIsVisibleAndClickable(APPLY_FILTERS_BUTTON);
        webDriverHelpers.fillInWebElement(MULTIPLE_OPTIONS_SEARCH_INPUT, idPhoneNameEmail);
        webDriverHelpers.clickOnWebElementBySelector(APPLY_FILTERS_BUTTON);
    }

    private void openContactFromResultsByUUID(String uuid) {
        By uuidLocator = By.cssSelector(String.format(CONTACT_RESULTS_UUID_LOCATOR, uuid));
        webDriverHelpers.clickOnWebElementBySelector((uuidLocator));
        webDriverHelpers.waitUntilIdentifiedElementIsPresent(UUID_INPUT);
    }

    private void fillExposure(Exposure exposureData) {
        webDriverHelpers.waitForPageLoaded();
        webDriverHelpers.fillInWebElement(
                START_OF_EXPOSURE_INPUT, formatter.format(exposureData.getStartOfExposure()));
        webDriverHelpers.fillInWebElement(
                END_OF_EXPOSURE_INPUT, formatter.format(exposureData.getEndOfExposure()));
        webDriverHelpers.fillInWebElement(
                EXPOSURE_DESCRIPTION_INPUT, exposureData.getExposureDescription());
        webDriverHelpers.selectFromCombobox(
                TYPE_OF_ACTIVITY_COMBOBOX, exposureData.getTypeOfActivity().getActivity());
        webDriverHelpers.selectFromCombobox(
                EXPOSURE_DETAILS_ROLE_COMBOBOX, exposureData.getExposureDetailsRole().getRole());
        webDriverHelpers.clickWebElementByText(
                RISK_AREA_OPTIONS, exposureData.getRiskArea().toString());
        webDriverHelpers.clickWebElementByText(INDOORS_OPTIONS, exposureData.getIndoors().toString());
        webDriverHelpers.clickWebElementByText(OUTDOORS_OPTIONS, exposureData.getOutdoors().toString());
        webDriverHelpers.clickWebElementByText(
                WEARING_MASK_OPTIONS, exposureData.getWearingMask().toString());
        webDriverHelpers.clickWebElementByText(
                WEARING_PPE_OPTIONS, exposureData.getWearingPpe().toString());
        webDriverHelpers.clickWebElementByText(
                OTHER_PROTECTIVE_MEASURES_OPTIONS, exposureData.getOtherProtectiveMeasures().toString());
        webDriverHelpers.clickWebElementByText(
                SHORT_DISTANCE_OPTIONS, exposureData.getShortDistance().toString());
        webDriverHelpers.clickWebElementByText(
                LONG_FACE_TO_FACE_CONTACT_OPTIONS, exposureData.getLongFaceToFaceContact().toString());
        webDriverHelpers.clickWebElementByText(
                PERCUTANEOUS_OPTIONS, exposureData.getPercutaneous().toString());
        webDriverHelpers.clickWebElementByText(
                CONTACT_TO_BODY_FLUIDS_OPTONS, exposureData.getContactToBodyFluids().toString());
        webDriverHelpers.clickWebElementByText(
                HANDLING_SAMPLES_OPTIONS, exposureData.getHandlingSamples().toString());
        webDriverHelpers.selectFromCombobox(
                TYPE_OF_PLACE_COMBOBOX, exposureData.getTypeOfPlace().getPlace());
        webDriverHelpers.selectFromCombobox(CONTINENT_COMBOBOX, exposureData.getContinent());
        webDriverHelpers.selectFromCombobox(SUBCONTINENT_COMBOBOX, exposureData.getSubcontinent());
        webDriverHelpers.selectFromCombobox(COUNTRY_COMBOBOX, exposureData.getCountry());
        webDriverHelpers.clickOnWebElementBySelector(DONE_BUTTON);
    }

    private Exposure collectExposureData() {
        return Exposure.builder()
                .startOfExposure(
                        LocalDate.parse(
                                webDriverHelpers.getValueFromWebElement(START_OF_EXPOSURE_INPUT), formatter))
                .endOfExposure(
                        LocalDate.parse(
                                webDriverHelpers.getValueFromWebElement(END_OF_EXPOSURE_INPUT), formatter))
                .exposureDescription(webDriverHelpers.getValueFromWebElement(EXPOSURE_DESCRIPTION_INPUT))
                .typeOfActivity(
                        TypeOfActivityExposure.fromString(
                                webDriverHelpers.getValueFromCombobox(TYPE_OF_ACTIVITY_COMBOBOX)))
                .exposureDetailsRole(
                        ExposureDetailsRole.fromString(
                                webDriverHelpers.getValueFromCombobox(EXPOSURE_DETAILS_ROLE_COMBOBOX)))
                .riskArea(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(RISK_AREA_OPTIONS)))
                .indoors(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(INDOORS_OPTIONS)))
                .outdoors(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(OUTDOORS_OPTIONS)))
                .wearingMask(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(WEARING_MASK_OPTIONS)))
                .wearingPpe(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(WEARING_PPE_OPTIONS)))
                .otherProtectiveMeasures(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(
                                        OTHER_PROTECTIVE_MEASURES_OPTIONS)))
                .shortDistance(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(SHORT_DISTANCE_OPTIONS)))
                .longFaceToFaceContact(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(
                                        LONG_FACE_TO_FACE_CONTACT_OPTIONS)))
                .percutaneous(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(PERCUTANEOUS_OPTIONS)))
                .contactToBodyFluids(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(
                                        CONTACT_TO_BODY_FLUIDS_OPTONS)))
                .handlingSamples(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(
                                        HANDLING_SAMPLES_OPTIONS)))
                .typeOfPlace(
                        TypeOfPlace.fromString(webDriverHelpers.getValueFromCombobox(TYPE_OF_PLACE_COMBOBOX)))
                .continent(webDriverHelpers.getValueFromCombobox(CONTINENT_COMBOBOX))
                .subcontinent(webDriverHelpers.getValueFromCombobox(SUBCONTINENT_COMBOBOX))
                .country(webDriverHelpers.getValueFromCombobox(COUNTRY_COMBOBOX))
                .build();
    }

    private EpidemiologicalData collectSpecificData() {
        return EpidemiologicalData.builder()
                .residingAreaWithRisk(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(
                                        RESIDING_OR_WORKING_DETAILS_KNOWN_OPTIONS)))
                .largeOutbreaksArea(
                        YesNoUnknownOptions.valueOf(
                                webDriverHelpers.getCheckedOptionFromHorizontalOptionGroup(
                                        RESIDING_OR_TRAVELING_DETAILS_KNOWN_OPTIONS)))
                .build();
    }

    private void loopByActivityTypes() {
        for (TypeOfActivity value : TypeOfActivity.values())
            if (value != TypeOfActivity.valueOf("GATHERING")
                    && value != TypeOfActivity.valueOf("OTHER")) {
                webDriverHelpers.selectFromCombobox(TYPE_OF_ACTIVITY_COMBOBOX, value.toString());
            }
    }

    private void loopByGatheringType() {
        for (TypeOfGathering value : TypeOfGathering.values()) {
            if (value != TypeOfGathering.valueOf("OTHER")) {
                webDriverHelpers.selectFromCombobox(TYPE_OF_GATHERING_COMBOBOX, value.toString());
            }
        }
    }

    private void loopByTypeOfPlace() {
        for (TypeOfPlace value : TypeOfPlace.values()) {
            webDriverHelpers.selectFromCombobox(TYPE_OF_PLACE_COMBOBOX, value.toString());
        }
    }
}
