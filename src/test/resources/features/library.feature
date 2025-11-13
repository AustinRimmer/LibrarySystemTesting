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


    When "Charlie" logs out
    And "Alice" logs in
    And "Alice" returns "The Great Gatsby"
    And "Alice" logs out
    And "Charlie" logs in
    Then "The Great Gatsby" is available

  Scenario: Multiple holds queue processing
    Given I have an initialized library
    When "Alice" borrows "The Great Gatsby"
    And "Alice" logs out
    And "Charlie" logs in
    And "Charlie" borrows "The Great Gatsby"
    And "Charlie" logs out
    Then "The Great Gatsby" is unavailable
    And "Charlie" is first in hold queue of "The Great Gatsby"

    When "Alice" logs in
    And "Alice" returns "The Great Gatsby"
    And "Alice" logs out
    And "Bob" logs in
    And "Bob" borrows "The Great Gatsby"
    And "Bob" logs out

    Then "Bob" has 0 books borrowed
    And "Bob" has 1 held books

    When "Charlie" logs in
    Then "Charlie" is notified that a held book "The Great Gatsby" is available
    And "Charlie" borrows "The Great Gatsby"
    Then "Charlie" has 1 books borrowed

  Scenario: Borrowing limit and hold interactions
    Given I have an initialized library

    When "Bob" logs in
    And "Bob" borrows "1984"
    And "Bob" borrows "Pride and Prejudice"
    And "Bob" borrows "Wuthering Heights"
    And "Bob" borrows "The Great Gatsby"
    And "Bob" borrows "The Hobbit"
    Then "Bob" has 3 books borrowed
    And "Bob" has 2 held books

    When "Bob" logs out
    And "Charlie" logs in
    And "Charlie" borrows "1984"
    And "Charlie" logs out
    And "Bob" logs in
    And "Bob" returns "1984"
    Then "Bob" has 2 books borrowed

    When "Charlie" logs in
    Then "Charlie" is notified that a held book "1984" is available

  Scenario: no borrowed books returns
    Given I have an initialized library

    When "Charlie" logs in
    Then "Charlie" has 0 books borrowed
    And all books are available

    When "Charlie" returns "The Great Gatsby"
    Then "Charlie" has 0 books borrowed

    When "Alice" logs in
    Then "Alice" has 0 books borrowed
    And all books are available

    When "Alice" returns "The Great Gatsby"
    Then "Alice" has 0 books borrowed

    When "Bob" logs in
    Then "Bob" has 0 books borrowed
    And all books are available

    When "Bob" returns "The Great Gatsby"
    Then "Bob" has 0 books borrowed
