Feature: Automated User Acceptance Tests for the Sentiment Analysis Application

  Background:
    Given Open https://fhvweatherapp.herokuapp.com/

  Scenario: Test login and logout
    Given Login with user 'user@test.com'
    When I press logout
    Then I see the login page

  Scenario: Test real location
    Given Login with user 'user@test.com'
    When Check the weather 'Dornbirn'
    Then The Location should be Dornbirn
    And I press logout

  Scenario: Test unknown location
    Given Login with user 'user@test.com'
    When Check the weather 'asdf1234'
    Then The Weather should be City not found
    And I press logout

  Scenario: User interaction with history (video)
    Given Login with user 'user@test.com'
    When Check the weather 'Dornbirn'
    When Check the weather 'Bregenz'
    And Navigate to history
    Then The 1. row shows the history item with text 'Dornbirn' has weatherinformations
    And The 2. row shows the history item with text 'Bregenz' has weatherinformations
    And I press logout