package com.assignment.rest.cucumber;

import io.restassured.response.Response;

public class ResponseHolder {
	
	public static Response response;
	public static  int responseCode;
	public static String responseBody;
	
		
	public static int getResponseCode()
	{
		responseCode=response.getStatusCode();
		return responseCode;
	}
	
	public static void setResponse(Response response)
	{
		ResponseHolder.response=response;
	}
	
	public static Response getResponse()
	{
		return response;
	}
	
	public static String getResponseBody()
	{
		responseBody=response.asString();
		return responseBody;
	}

}
