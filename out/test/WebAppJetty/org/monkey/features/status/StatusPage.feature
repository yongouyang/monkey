@ui
Feature: Status Page
  The Status Page have a link such that when clicked, it
  will bring us the latest status of the system

Scenario: Page Title
  Given I am on the status page
  Then The page title should be Status Page

Scenario: Check Status
  Given I am on the status page
  And I click the link with id checkStatus
  Then I should be on the ping page