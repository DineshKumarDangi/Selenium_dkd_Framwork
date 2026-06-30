package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	private ActionDriver actionDriver;
// Define locators using By class
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
/*
	// initialize the ActionDriver object by passing WebDriver instance
	public LoginPage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
	} */
	
	public LoginPage (WebDriver driver){
		this.actionDriver = BaseClass.getActionDriver();
		
	}
	
// Method to perform login
	public void login(String userName, String password) {
		actionDriver.enterText(userNameField, userName);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
// Method to check if error is displayed

	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}

// Method to get the text from error message
	public String getErrorMessage() {
		return actionDriver.getText(errorMessage);
	}

// verify if error message is correct or not
	public boolean VerifyErrorMessage(String expectedError) {

		return actionDriver.compareText(errorMessage, expectedError);
	}

}