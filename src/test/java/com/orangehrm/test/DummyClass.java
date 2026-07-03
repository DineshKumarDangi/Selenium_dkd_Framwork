package com.orangehrm.test;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;


public class DummyClass extends BaseClass {
	@Test
	public void dummyTest() {
		// Test 
		//ExtentManager.startTest("dummy Test1");		-- This has been implemented in TestListener
		String pageTitle = getDriver().getTitle();
		System.out.println("page title is: "+ pageTitle);
		ExtentManager.logStep("Verify the title: ");
		Assert.assertEquals("OrangeHRM", pageTitle,"page title is not matching");
		
		System.out.println("Test passed - title is matching");
	//	ExtentManager.logSkip("This case is skipped");
		throw new SkipException("Skipping the test as part of testing ");
	}
}