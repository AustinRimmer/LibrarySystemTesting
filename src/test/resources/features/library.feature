Feature: Library operations
  As a user
  I want to perform all library operations
  So that I can use the library

  Scenario: A1 Acceptance Test 1
    Given I have an initialized library
    When "Alice" logs in
    And "Alice" borrows "The Great Gatsby"
    And "Alice" logs out
    And "Charlie" logs in
    Then "The Great Gatsby" is unavailable

    When "Alice" returns "The Great Gatsby"
    And "Alice" logs out
    And "Charlie" logs in
    Then "The Great Gatsby" is available