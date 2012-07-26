Feature: Calculator - Plus
In order to avoid silly mistakes
Cashiers must be able to calculate a fraction

@stupid
Scenario: Integers
  Given I have entered 4 into the calculator
  And I have entered 3 into the calculator
  When I press plus
  Then the stored result should be 7