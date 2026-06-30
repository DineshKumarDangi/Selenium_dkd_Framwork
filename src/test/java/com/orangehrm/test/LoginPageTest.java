package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void pageSetup() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}
	//@Test

	//	public void verifyValidLoginTest() { //String username, String password
	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
		public void verifyValidLoginTest(String username, String password) {
		// ExtentManager.startTest("Valid login Test"); -- This has been implemented in
		// TestListener
		System.out.println("Running TestMethod1 on thread" + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login page entering username and pasword");
	//	 loginpage.login(prop.getProperty("username"), prop.getProperty("password")); // read from config file
		 //loginpage.login("Admin", "admin123");
		loginpage.login(username, password);
		ExtentManager.logStep("Verify admin tab is visible or not");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin tab should be visible after successful login");
		ExtentManager.logStep("Validation successfull");
		homepage.logout();
		ExtentManager.logStep("Logged out successfully!");
		staticWait(2);
	}
	@Test(enabled =false)
	//@Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class)
	public void invalidLoginTest() { //String username, String password
		// ExtentManager.startTest("In-Valid login Test"); -- This has been implemented
		// in TestListener
	//	System.out.println("Running TestMethod1 on thread" + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login page entering username and pasword");
		//loginpage.login(username, password);
		 loginpage.login("Admin1", "admin123");
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginpage.VerifyErrorMessage(expectedErrorMessage), "Test failed : Invalid error message");
		ExtentManager.logStep("Validation successfull");
		ExtentManager.logStep("Logged out successfully!");
	}

}
