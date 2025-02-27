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

package org.sormas.e2etests.steps.documentTemplates;

import static org.sormas.e2etests.pages.application.configuration.ConfigurationTabsPage.CONFIGURATION_DOCUMENT_TEMPLATES_TAB;
import static org.sormas.e2etests.pages.application.configuration.DocumentTemplatesPage.*;

import cucumber.api.java8.En;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import lombok.SneakyThrows;
import org.sormas.e2etests.helpers.WebDriverHelpers;
import org.testng.asserts.SoftAssert;

public class UploadDocumentTemplatesSteps implements En {

  private final WebDriverHelpers webDriverHelpers;
  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
  public static final String userDirPath = System.getProperty("user.dir");

  @SneakyThrows
  @Inject
  public UploadDocumentTemplatesSteps(WebDriverHelpers webDriverHelpers, SoftAssert softly) {
    this.webDriverHelpers = webDriverHelpers;

    When(
        "I navigate to document templates tab",
        () -> webDriverHelpers.clickOnWebElementBySelector(CONFIGURATION_DOCUMENT_TEMPLATES_TAB));

    When(
        "I click on the UPLOAD TEMPLATE button from Document Templates Case",
        () -> webDriverHelpers.clickOnWebElementBySelector(UPLOAD_CASE_TEMPLATE_BUTTON));

    When(
        "I click on the UPLOAD TEMPLATE button from Document Templates Contact",
        () -> webDriverHelpers.clickOnWebElementBySelector(UPLOAD_CONTACT_TEMPLATE_BUTTON));

    When(
        "I click on the UPLOAD TEMPLATE button from Document Templates Event Participant",
        () ->
            webDriverHelpers.clickOnWebElementBySelector(UPLOAD_EVENT_PARTICIPANT_TEMPLATE_BUTTON));

    When(
        "I click on the UPLOAD TEMPLATE button from Document Templates Travel Entry",
        () -> webDriverHelpers.clickOnWebElementBySelector(UPLOAD_TRAVEL_ENTRY_TEMPLATE_BUTTON));

    When(
        "I click on the UPLOAD TEMPLATE button from Document Templates Event",
        () -> webDriverHelpers.clickOnWebElementBySelector(UPLOAD_EVENT_HANDOUT_TEMPLATE_BUTTON));

    When(
        "I pick the case document template file",
        () ->
            webDriverHelpers.sendFile(
                FILE_PICKER, userDirPath + "/uploads/ExampleDocumentTemplateCases.docx"));

    When(
        "I pick the contact document template file",
        () ->
            webDriverHelpers.sendFile(
                FILE_PICKER, userDirPath + "/uploads/ExampleDocumentTemplateContacts.docx"));

    When(
        "I pick the event participant document template file",
        () ->
            webDriverHelpers.sendFile(
                FILE_PICKER,
                userDirPath + "/uploads/ExampleDocumentTemplateEventParticipant.docx"));
    When(
        "I pick the {string} file",
        (String filename) -> {
          webDriverHelpers.waitUntilElementIsVisibleAndClickable(FILE_PICKER);
          webDriverHelpers.sendFile(FILE_PICKER, userDirPath + "/uploads/" + filename);
        });

    When(
        "I pick the travel entry document template file",
        () ->
            webDriverHelpers.sendFile(
                FILE_PICKER, userDirPath + "/uploads/ExampleDocumentTemplateTravelEntry.docx"));

    When(
        "I pick the event document template file",
        () ->
            webDriverHelpers.sendFile(
                FILE_PICKER, userDirPath + "/uploads/ExampleDocumentTemplateEventHandout.html"));

    When(
        "I click on the UPLOAD TEMPLATE button from the popup",
        () -> webDriverHelpers.clickOnWebElementBySelector(UPLOAD_TEMPLATE_POPUP_BUTTON));

    When(
        "I confirm the document template overwrite popup",
        () -> webDriverHelpers.clickOnWebElementBySelector(TEMPLATE_OVERWRITE_CONFIRM_BUTTON));

    Then(
        "I check that an upload success notification appears",
        () -> {
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(UPLOAD_SUCCESS_POPUP);
          webDriverHelpers.clickOnWebElementBySelector(UPLOAD_SUCCESS_POPUP);
        });
    Then(
        "I click to close UPLOAD TEMPLATE popup",
        () -> {
          webDriverHelpers.clickOnWebElementBySelector(CLOSE_POPUP);
        });
  }
}
