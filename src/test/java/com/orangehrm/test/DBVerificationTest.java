package com.orangehrm.test;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass{
	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void pageSetup() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

	
	@Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String empId ,String empName ) {
		SoftAssert softAssert = getSoftAssert();
		
		 ExtentManager.logStep("Logging with admin credentials");
		 loginpage.login(prop.getProperty("username"), prop.getProperty("password"));
		 ExtentManager.logStep("Click on PIM tab");
		 homepage.clickonPIMTab();
		 ExtentManager.logStep("Search for employee");
		 homepage.employeeSearch(empName);
		 ExtentManager.logStep("Get the employeename from database");
		 
		 String employee_id=empId;
		 
		 // Fetch the data into a map
		 Map<String, String> employeedetails = DBConnection.getEmployeeDetails(employee_id);
		 String  emplFirstName = employeedetails.get("firstName");
		 String  emplMiddleName =  employeedetails.get("middleName");
		 String  emplLastName = employeedetails.get("lastName");
		 
		 String emplFirstaAndMiddleName=(emplFirstName +" "+emplMiddleName).trim();
		 // verification for first and middle name
		 ExtentManager.logStep("Verify the employee first and middle name: ");
		 softAssert.assertTrue(homepage.verifyEmployeeFirstAndMiddleName(emplFirstaAndMiddleName),"First and Middle name not Matching");
		 // Verification for last name
		 ExtentManager.logStep("Verify the employee last name: ");
		 softAssert.assertTrue(homepage.verifyEmployeeLastName(emplLastName),"Employee last name not matching");
		 ExtentManager.logStep("DB validation Completed");	
		 softAssert.assertAll();
	}

}
