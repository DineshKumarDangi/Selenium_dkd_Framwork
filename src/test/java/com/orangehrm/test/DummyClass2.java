package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {
	@Test
	public void dummyTest() {
		//ExtentManager.startTest("dummy Test2"); 	-- This has been implemented in TestListener
		String pageTitle = getDriver().getTitle();
		System.out.println("page title is: "+ pageTitle);
		ExtentManager.logStep("Verify the title: ");
		Assert.assertEquals("OrangeHRM", pageTitle,"page title is Not matching");
		System.out.println("test passed - title is Matching");
		ExtentManager.logStep("Validation successfull: ");
	}
}