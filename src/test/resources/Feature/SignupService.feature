@signup
Feature: Signup service Tests
    New user Account creation and token generation

  @sanityTest
  Scenario Outline: Sanity Test
    When User perform post request with "/signup?name=test&passwd=jk2222"
    Then The status code should be 200

  @tokenCreation
  Scenario Outline: SignUp new  user
    When User perform signup post request with parameters "<name>","<passwd>"
    Then The status code should be 200
    And response body should have key "<key>" and message "<message>"

    Examples: 
      | name   | passwd  | key   | message                                                                          |
      | Tester | abc1234 | token | Please save it. This is visible only once. If you forget please regenerate token |

  @tokenRenewal
  Scenario Outline: Renew Token for the user
    Given User perform successful signup operation with "<name>","<passwd>" with status 200
    When User perform patch request "/renew" with parameter "name" and value "<name>"
    Then The status code should be 200
    And response body should have key "<key>" and message "<message>"

    Examples: 
      | name          | passwd | key   | message                                    |
      | Renew_User222 | rn2334 | token | Please save it. This is visible only once. |

  @tokenValidation
  Scenario Outline: Validate provided Token is valid or not
    Given User perform successful signup operation with "<name>","<passwd>" with status 200
    When User perform get request "/validate" with parameter as generated token
    Then The status code should be 200
    And response body should have response text "<message>"

    Examples: 
      | name              | passwd   | message |
      | Test_Another_User | asdja222 | true    |

  @userInformation
  Scenario Outline: Get user information for valid token
    Given User perform successful signup operation with "<name>","<passwd>" with status 200
    When User perform get request "/user" with parameter as generated token
    Then The status code should be 200
    And response body should have response text "<message>"

    Examples: 
      | name      | passwd | message   |
      | Test_User | testk1222 | Test_User |
