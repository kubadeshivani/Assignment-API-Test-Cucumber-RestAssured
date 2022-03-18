@signuperror
Feature: Negetive Test : To Verify the signUp service is not running fine
    In order to validate the signup service is fetching error when trying to create new user Account and token generation

  @tokenCreationWithSameUserName
  Scenario Outline: SignUp new  user with same data
    Given User perform signup post request with parameters "<name>","<passwd>"
    When User perform signup post request with parameters "<name>","<passwd>"
    Then The status code should not be 200

    Examples: 
      | name   | passwd  |
      | Tester | abc1234 |

  @signupValidationError
  Scenario Outline: Validation error when signup new user with only name
    When User perform post request "/signup" with only parameter "<name>"
    Then The status code should be 422
    And response body should have validation error with parameter name  "<paramname>", message "<msg>" and type "<type>"

    Examples: 
      | name   | paramname | msg            | type                |
      | Tester | passwd    | field required | value_error.missing |

  @renewValidationError
  Scenario Outline: Validation error when user renew token without name parameter
    When User perform patch request "/renew" without name parameter
    Then The status code should be 422
    And response body should have validation error with parameter name  "<paramname>", message "<msg>" and type "<type>"

    Examples: 
      | paramname | msg            | type                |
      | name      | field required | value_error.missing |

  @tokenValidationError
  Scenario Outline: Validation error when user trying to validate token without token parameter
    When User perform get request "/validate" without token parameter
    Then The status code should be 422
    And response body should have validation error with parameter name  "<paramname>", message "<msg>" and type "<type>"

    Examples: 
      | paramname | msg            | type                |
      | token     | field required | value_error.missing |

  @UserInformationValidationError
  Scenario Outline: Validation error when user trying to get user information without token parameter
    When User perform get request "/user" without token parameter
    Then The status code should be 422
    And response body should have validation error with parameter name  "<paramname>", message "<msg>" and type "<type>"

    Examples: 
      | paramname | msg            | type                |
      | token     | field required | value_error.missing |

  @renewNotFoundError
  Scenario Outline: User not found error when trying to renew token for the user
    When User perform patch request "/renew" with name "<name>"
    Then The status code should be 404
    And response body should have detail "<detail>"

    Examples: 
      | name | detail         |
      | xyz  | User Not Found |

  @UserInformationUnauthorizedError
  Scenario Outline: Unauthorized error when trying to get user information for invalid token
    When User perform get request "/user" with name "<token>"
    Then The status code should be 401
    And response body should have response text "<message>"

    Examples: 
      | token | message       |
      |  1234 | Invalid Token |

  @renewDuplicateRecordsError
  Scenario Outline: Duplicate records found when trying to renew token for the user having more records with same name
    Given User perform successful signup operation with "<name>","<passwd>" with status 200
    When User perform successful signup operation with "<name>","<passwd>" with status 200
    And User perform patch request "/renew" with existing username parameter name "name" and value "<name>"
    Then The status code should be 409
    And response body should have detail "<detail>"

    Examples: 
      | name   | password | detail                  |
      | Tester | abc1234  | Duplicate Records found |
