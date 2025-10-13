@belly @basic
Feature: Belly Management
  As a hungry person
  I want to manage my belly state
  So that I know when I need to eat

  Background:
    Given I start with an empty belly

  @happy
  Scenario: Happy with many cukes
    Given I have 60 cukes in my belly
    When I check my mood
    Then I should be "HAPPY"
    And my belly should not growl

  @hungry
  Scenario: Hungry after waiting
    Given I have 25 cukes in my belly
    When I wait 2 hours
    Then my belly should growl
    And I should have less than 10 cukes left

  @satisfied
  Scenario: Satisfied with moderate eating
    Given I have 35 cukes in my belly
    When I wait 1 hour
    Then I should be satisfied

  @soda @digestion
  Scenario Outline: Digestion with soda
    Given I have <initial> cukes in my belly
    And I drink a soda
    When I wait <hours> hours
    Then I should have exactly <remaining> cukes left
    And the digestion should be faster

    Examples:
      | initial | hours | remaining |
      | 50      | 2     | 20        |
      | 30      | 1     | 15        |
      | 40      | 3     | 0         |

  @content
  Scenario Outline: Different cucumber amounts
    Given I have <cukes> cucumber(s) in my stomach
    When I check my mood
    Then I should be "<mood>"

    Examples:
      | cukes | mood     |
      | 5     | HUNGRY   |
      | 25    | CONTENT  |
      | 45    | CONTENT  |
      | 65    | HAPPY    |

  @belly @basic @complex
  Scenario: Complex eating pattern
    Given I start with an empty belly
    Given I have 10 cukes in my belly
    And I have 15 more cukes in my belly
    When I wait 1 hour
    And I drink a soda
    And I wait 1 more hour
    Then I should have exactly 0 cukes left
    And my belly should growl