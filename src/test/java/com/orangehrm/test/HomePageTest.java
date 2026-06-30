package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {
	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void pageSetup() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo(String username, String password) {
		//ExtentManager.startTest("Home page verify logo test"); 	-- This has been implemented in TestListener
		ExtentManager.logStep("Navigating to Login page entering username and pasword");
		
		//loginpage.login("Admin", "admin123");
		loginpage.login(username, password);
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homepage.verifyOrangehrmLogo(), "Logo is not visible:");
		ExtentManager.logStep("Validation successfull");
		ExtentManager.logStep("Logged out successfully!"); 
	}

}
