@weatherservice
Feature: Weather service Tests
    In order to validate the weather service is up and running fine

  @returnWeather
  Scenario Outline: Weather information with valid token
    Given User perform successful signup operation with "<name>","<passwd>" with status 200
    When User perform get request on "/weather" with valid parameter as generated token
    Then The status code should be 200

    Examples: 
      | name   | passwd  |
      | Tester | abc1234 |

  @weatherInforamtionValidationError
  Scenario Outline: Validation error when user trying to get weather information token without token parameter
    When User perform get request on "/weather" without parameter token
    Then The status code should be 422
    And response body should have validation error with parameter name  "<paramname>", message "<msg>" and type "<type>"

    Examples: 
      | paramname | msg            | type                |
      | token     | field required | value_error.missing |
