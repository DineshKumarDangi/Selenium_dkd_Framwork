package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {

	// Method to send the GET request
	public static Response sendGetRequest(String endPoint) {

		return RestAssured.get(endPoint);
	}

	// Method to send the post request
	public static Response sendPostRequest(String endpoint, String payload) {
		return RestAssured.given()
				.header("Content-Type", "applcation/json")
				.body(payload)
				.post();
	}

	// Method to validate response status code
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode() == statusCode;
	}

	// Method to Extract value from JSON response
	public static String getJsonValue(Response response, String value) {

		return response.jsonPath().getString(value);

	}

}
