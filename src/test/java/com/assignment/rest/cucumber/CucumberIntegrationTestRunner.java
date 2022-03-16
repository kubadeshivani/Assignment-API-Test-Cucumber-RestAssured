package com.assignment.rest.cucumber;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
				features = "src/test/resources/Feature",
				glue = "com.assignment.rest.cucumber",
				
				plugin = { "pretty", "json:target/cucumber-reports/Cucumber.json",
						"junit:target/cucumber-reports/Cucumber.xml",
						"html:target/cucumber-reports/Cucumber.html",
						"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
				monochrome = true
				)
public class CucumberIntegrationTestRunner {
	
	
		
}
