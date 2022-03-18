# Assignment-API-Test-Cucumber-RestAssured
Task 1 : Rest API Test Automation using Cucumber and Rest Assured Framework

This automation suite is designed to test below 3 services : 

- Signup service -- Service for user to create his account
- Quote service -- Quotation service.
- Weather service -- Weather service.

There are 4 feature files covering 19 scenarios of 3 services.

* src/test/resources/features/SignUpService.feature
* src/test/resources/features/SignUpService_Error.feature
* src/test/resources/features/QuoteService.feature
* src/test/resources/features/WeatherService.feature
 
Required Setup :

* Java - Java 1.8 - Programming Language
* Maven- (Version 3.8.1)- Dependency Management
* Cucumber - (version 7.2.3 )- BDD Framework
* Rest-Assured - (version 4.5.1) -API Calls
* Junit 4 - (version 4.12) - Test Framework

In order to execute the automation suite navigate to the Project directory within a Terminal/CMD window and run the command:

'mvn clean test'

* 19 Scenarios will be executed with 3 failures for open defects 

Report file can be found : 

'target/cucumber-reports/Cucumber.html' 1 of the 12 test scenarios

* Important Note : Tested with Windows 10 platform and should also work on other platforms.
