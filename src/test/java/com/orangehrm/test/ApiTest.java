package com.orangehrm.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {
	@Test //(retryAnalyzer = RetryAnalyzer.class)
	public void verifyGetUserAPI() {
		SoftAssert softAssert = new SoftAssert();

		// Step 1: Define API End point
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " + endPoint);
		
		// Step 2: Send GET request
		ExtentManager.logStep("Sending get request to the API ");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step 3: Validate Status code
		ExtentManager.logStep("Validating API Response status code ");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as expected");
		if (isStatusCodeValid == true) {
			ExtentManager.logStepValidationForAPI("Sttaus code Validation Passed! ");
		} else {
			ExtentManager.logFailureAPI("Status Code validation Failed! ");
		}
		
		// Step 4: Validate user name
		ExtentManager.logStep("Validating responsebody for username");
		String username =ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(username);
		softAssert.assertTrue(isUserNameValid,"Username is not valid");
		if(isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username Validation Passed! ");
		}
		else {
			ExtentManager.logFailureAPI("Username Validation Failed!");
		}
		// Step 5: Validate email
		ExtentManager.logStep("Validating responsebody for username");
		String email =ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(email);
		softAssert.assertTrue(isEmailValid,"email is not valid");
		if(isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email Validation Passed! ");
		}
		else {
			ExtentManager.logFailureAPI("Email Validation Failed!");
		}
		softAssert.assertAll();
	}
}
