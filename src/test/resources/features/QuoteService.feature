@quoteservice
Feature: Quote service Tests
    In order to validate the quote service is up and running fine

  @generateQuote
  Scenario Outline: Generate quote with valid token
    Given User perform successful signup operation with "<name>","<passwd>" with status 200
    When User perform get request "/quote" with valid parameter as generated token
    Then The status code should be 200

    Examples: 
      | name   | passwd  |
      | Tester | abc1234 |

  @generateQuoteValidationError
  Scenario Outline: Validation error when user trying to generate quote without token parameter
    When User perform get request "/quote" without parameter token
    Then The status code should be 422
    And response body should have validation error with parameter name  "<paramname>", message "<msg>" and type "<type>"

    Examples: 
      | paramname | msg            | type                |
      | token     | field required | value_error.missing |

  @generateQuoteUnauthorizedError
  Scenario Outline: Unauthorized error when trying to generate quote with invalid token
    When User perform get request "/quote" with invalid token "<token>"
    Then The status code should be 401
    And response body should have detail "<detail>"

    Examples: 
      | token | detail           |
      | 12345 | token is invalid |
