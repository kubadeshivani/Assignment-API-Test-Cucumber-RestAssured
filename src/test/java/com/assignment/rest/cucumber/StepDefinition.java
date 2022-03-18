package com.assignment.rest.cucumber;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.assignment.rest.cucumber.model.HttpValidationError;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class StepDefinition {

	private static final String SIGN_UP_BASE_URL = "http://127.0.0.1:8001/api/v1/signupsrv";
	private static final String QUOTE_BASE_URL = "http://127.0.0.1:8080/api/v1/quotesrv";
	private static final String WEATHER_BASE_URL = "http://127.0.0.1:8080/api/v1/weathersrv";

	private static final RequestSpecification signUpRequestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON)
			.setBaseUri(SIGN_UP_BASE_URL).build();

	private static final RequestSpecification quoteRequestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON)
			.setBaseUri(QUOTE_BASE_URL).build();

	private static final RequestSpecification weatherRequestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON)
			.setBaseUri(WEATHER_BASE_URL).build();

	private static final ResponseSpecification responseSpec_Success = new ResponseSpecBuilder()
			.expectContentType(ContentType.JSON).expectStatusCode(200).build();

	private static final ResponseSpecification responseSpec_UnAuthorized = new ResponseSpecBuilder()
			.expectContentType(ContentType.JSON).expectStatusCode(401).build();

	private static final ResponseSpecification responseSpec_Validation_Error = new ResponseSpecBuilder()
			.expectContentType(ContentType.JSON).expectStatusCode(422).build();

	private static final ResponseSpecification responseSpec_Duplicate_Error = new ResponseSpecBuilder()
			.expectContentType(ContentType.JSON).expectStatusCode(409).build();

	private static final ResponseSpecification responseSpec_Not_Found = new ResponseSpecBuilder().expectContentType(ContentType.JSON)
			.expectStatusCode(404).build();

	private Response response;


	@When("User perform post request with {string}")
	public void user_perform_signup_request(String url) {
		response = given().spec(signUpRequestSpec).when().post(url).then().spec(responseSpec_Success).extract()
				.response();
	}

	@Then("The status code should be {int}")
	public void the_status_code_should_be(Integer status) {
		assertTrue(response.getStatusCode() == status);
	}

	@When("User perform signup post request with parameters {string},{string}")
	public void user_perform_signup_post_request_with_parameters(String name, String password) {
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("name", name);
		paramMap.put("passwd", password);
		response = given().spec(signUpRequestSpec).queryParams(paramMap).post("/signup").then()
				.spec(responseSpec_Success).extract().response();
	}

	@Then("response body should have key {string} and message {string}")
	public void in_response_body_is(String key, String message) {
		String res = response.getBody().as(String.class);
		assertNotNull(res);
		assertTrue(res.contains(key));
		assertTrue(res.contains(message));
	}

	@When("User perform patch request {string} with parameter name {string} and value {string}")
	public void user_perform_patch_request_with_parameter_and_value(String uri, String paramName, String paramValue) {
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			Map<String, String> paramMap = new LinkedHashMap<String, String>();
			paramMap.put(paramName, paramValue);
			response = given().spec(signUpRequestSpec).queryParams(paramMap).patch(uri).then()
					.spec(responseSpec_Success).extract().response();
		}
	}

	@When("User perform patch request {string} with existing username parameter name {string} and value {string}")
	public void user_perform_patch_request_with_existing_username_parameter_name_and_value(String uri, String paramName,
			String paramValue) {
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			Map<String, String> paramMap = new LinkedHashMap<String, String>();
			paramMap.put(paramName, paramValue);
			response = given().spec(signUpRequestSpec).queryParams(paramMap).patch(uri).then()
					.spec(responseSpec_Duplicate_Error).extract().response();
		}

	}

	@Given("User perform successful signup operation with {string},{string} with status {int}")
	public void user_perform_successful_signup_operation_with_with_status(String name, String password,
			Integer status) {
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("name", name);
		paramMap.put("passwd", password);
		response = given().spec(signUpRequestSpec).queryParams(paramMap).post("/signup").then()
				.spec(responseSpec_Success).extract().response();

	}

	@When("User perform get request {string} with parameter as generated token")
	public void user_perform_get_request_with_parameter_as_generated_token(String uri) {
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			Map<String, String> paramMap = new LinkedHashMap<String, String>();
			paramMap.put("token", parseToken(response.getBody().as(String.class)));
			response = given().spec(signUpRequestSpec).queryParams(paramMap).get(uri).then().spec(responseSpec_Success)
					.extract().response();
		}
	}

	@Then("response body should have response text {string}")
	public void response_body_should_have_response_text(String text) {
		String res = response.getBody().as(String.class);
		assertNotNull(res);
		assertEquals(text.trim(), res.trim());
	}
	
	@When("User perform post request {string} with only parameter {string}")
	public void user_perform_post_request_with_only_parameter(String uri, String name) {
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("name", name);

		response = given().spec(signUpRequestSpec).queryParams(paramMap).post(uri).then()
				.spec(responseSpec_Validation_Error).extract().response();
	}

	@When("User perform patch request {string} without name parameter")
	public void user_perform_post_request_without_name_parameter(String uri) {
		response = given().spec(signUpRequestSpec).patch(uri).then().spec(responseSpec_Validation_Error).extract()
				.response();
	}

	@When("User perform get request {string} without token parameter")
	public void user_perform_get_request_without_token_parameter(String uri) {
		response = given().spec(signUpRequestSpec).get(uri).then().spec(responseSpec_Validation_Error).extract()
				.response();
	}

	@When("User perform patch request {string} with name {string}")
	public void user_perform_patch_request_with_name(String uri, String name) {
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("name", name);
		response = given().spec(signUpRequestSpec).queryParams(paramMap).patch(uri).then().spec(responseSpec_Not_Found)
				.extract().response();
	}

	@Then("The status code should not be {int}")
	public void the_status_code_should_not_be(Integer status) {
		assertTrue(response.getStatusCode() != status);
	}

	@Then("response body should have validation error with parameter name  {string}, message {string} and type {string}")
	public void response_body_should_have_validation_error_with_parameter_name_message_and_type(String paramName,
			String message, String type) {
		HttpValidationError error = response.as(HttpValidationError.class);
		assertNotNull(error);
		assertEquals(paramName, error.getDetail().get(0).getLoc().get(1));
		assertEquals(message, error.getDetail().get(0).getMsg());
		assertEquals(type, error.getDetail().get(0).getType());
	}

	@Then("response body should have detail {string}")
	public void response_body_should_have_detail(String detail) {
		assertEquals(detail, response.jsonPath().get("detail").toString());
	}

	@When("User perform get request {string} with name {string}")
	public void user_perform_get_request_with_name(String uri, String token) {
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("token", token);

		response = given().spec(signUpRequestSpec).queryParams(paramMap).get(uri).then().spec(responseSpec_UnAuthorized)
				.extract().response();
	}

	@When("User perform get request {string} with valid parameter as generated token")
	public void user_perform_get_request_with_valid_parameter_as_generated_token(String uri) {
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			Map<String, String> paramMap = new LinkedHashMap<String, String>();
			paramMap.put("token", parseToken(response.getBody().as(String.class)));
			response = given().spec(quoteRequestSpec).queryParams(paramMap).get(uri).then().spec(responseSpec_Success)
					.extract().response();
		}
	}

	@When("User perform get request {string} without parameter token")
	public void user_perform_get_request_without_parameter_token(String uri) {
		response = given().spec(quoteRequestSpec).get(uri).then().spec(responseSpec_Validation_Error).extract()
				.response();
	}

	@When("User perform get request {string} with invalid token {string}")
	public void user_perform_get_request_with_invalid_token(String uri, String token) {
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("token", token);

		response = given().spec(quoteRequestSpec).queryParams(paramMap).get(uri).then().spec(responseSpec_UnAuthorized)
				.extract().response();
	}

	@When("User perform get request on {string} with valid parameter as generated token")
	public void user_perform_get_request_on_with_valid_parameter_as_generated_token(String uri) {
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			Map<String, String> paramMap = new LinkedHashMap<String, String>();
			paramMap.put("token", parseToken(response.getBody().as(String.class)));
			response = given().spec(weatherRequestSpec).queryParams(paramMap).get(uri).then().spec(responseSpec_Success)
					.extract().response();
		}
	}

	@When("User perform get request on {string} without parameter token")
	public void user_perform_get_request_on_without_parameter_token(String uri) {
		response = given().spec(weatherRequestSpec).get(uri).then().spec(responseSpec_Validation_Error).extract()
				.response();
	}

	@When("User perform get request on {string} with invalid token {string}")
	public void user_perform_get_request_on_with_invalid_token(String uri, String token) {
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("token", token);

		response = given().spec(weatherRequestSpec).queryParams(paramMap).get(uri).then()
				.spec(responseSpec_UnAuthorized).extract().response();
	}

	private String parseToken(String response) {
		return StringUtils.remove(StringUtils.split(response)[1], "\\n");
	}
}
