@UI @Sanity @Persons
Feature: Edit Persons
  Scenario: Edit existent person
   Given I log in with National User
    When I click on the Contacts button from navbar
    And I click on the NEW CONTACT button
    Then I create a new contact
    And I open Contact Person tab
    Then I complete all default empty fields from Contact Person tab
    When I click on new entry button from Contact Information section
    Then I complete all fields from Person Contact Details popup and save
    Then I click on save button from Contact Person tab
    Then I navigate to the last created Person page via URL
    Then I check that previous created person is correctly displayed in Edit Person page
    And While on Person edit page, I will edit all fields with new values
    And I edit all Person primary contact details and save
    Then I click on save button from Edit Person page
    And I check that previous edited person is correctly displayed in Edit Person page

  Scenario: Check Filters on Person page work as expected
    Given API: I create a new person
    Then API: I check that POST call body is "OK"
    And API: I check that POST call status code is 200
    When I log in with National User
    When I click on the Persons button from navbar
    Then I choose random value for Year of birth filter
    And I choose random value for Month of birth filter
    And I choose random value for Day of birth filter
    Then I search after last created person from API by "uuid"
    And I choose present condition field from specific range
    And I choose random value for Region
    And I choose random value for District
    And I choose random value for Community
    Then I apply on the APPLY FILTERS button
    And I change Year of birth filter to "1955"
    Then I apply on the APPLY FILTERS button
    And I check that number of displayed Person results is 0
    Then I choose random value for Year of birth filter
    And I change Month of birth filter to "4"
    Then I apply on the APPLY FILTERS button
    And I check that number of displayed Person results is 0
    And I choose random value for Month of birth filter
    And I change Day of birth filter to "13"
    Then I apply on the APPLY FILTERS button
    And I choose random value for Day of birth filter
    Then I change id,full name,email,phone number, by "uuid"
    And I apply on the APPLY FILTERS button
    And I check that number of displayed Person results is 0
    And I search after last created person from API by "uuid"
    And I change present condition filter to "Unknown"
    And I apply on the APPLY FILTERS button
    And I check that number of displayed Person results is 0
    And I choose present condition field from specific range
    And I change REGION filter to "Berlin"
    And I apply on the APPLY FILTERS button
    And I check that number of displayed Person results is 0
    And I choose random value for Region
    Then I change DISTRICT filter to ""
    And I apply on the APPLY FILTERS button
    And I check that number of displayed Person results is 0
    And I choose random value for District
    Then I change Community filter to "Community2"
    And I apply on the APPLY FILTERS button
    And I check that number of displayed Person results is 0
    And I choose random value for Community
    And I apply on the APPLY FILTERS button
    And I click on the RESET FILTERS button




