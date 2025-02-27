@UI @Sanity @Contact @ContactImportExport
Feature: Contact import and export tests

  @issue=SORDEV-10043 @env_main
  Scenario: Contact basic export test
    When API: I create a new person
    Then API: I check that POST call body is "OK"
    And API: I check that POST call status code is 200
    Then API: I create a new contact
    Then API: I check that POST call body is "OK"
    And API: I check that POST call status code is 200
    Given I log in as a Admin User
    And I click on the Contacts button from navbar
    Then I filter by Contact uuid
    And I click on the Export contact button
    Then I click on the Basic Contact Export button
    Then I check if downloaded data generated by basic contact option is correct

  @issue=SORDEV-10046 @env_main
  Scenario: Contact custom export test
    When API: I create a new person
    Then API: I check that POST call body is "OK"
    And API: I check that POST call status code is 200
    Then API: I create a new contact
    Then API: I check that POST call body is "OK"
    And API: I check that POST call status code is 200
    Given I log in as a Admin User
    And I click on the Contacts button from navbar
    Then I filter by Contact uuid
    And I click on the Export contact button
    Then I click on the Custom Contact Export button
    And I click on the New Export Configuration button in Custom Contact Export popup
    Then I fill Configuration Name field with "Test Configuration" in Custom Contact Export popup
    And I select specific data to export in Export Configuration for Custom Contact Export
    When I download created custom contact export file
    And I delete created custom contact export file
    Then I check if downloaded data generated by custom contact option is correct
