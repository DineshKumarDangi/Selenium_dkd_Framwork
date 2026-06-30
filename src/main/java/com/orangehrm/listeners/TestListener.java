package com.orangehrm.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class TestListener implements ITestListener, IAnnotationTransformer {

	/*@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
	*/
	// Triggers when test start
	@Override
	public void onTestStart(ITestResult result) {
		//
		System.out.println("onTestStart inside TestListener ");
		String testName = result.getMethod().getMethodName();
		// Start logging in Extent Report
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test started" + testName);

	}

	// Triggers when a test success
	@Override
	public void onTestSuccess(ITestResult result) {
		
		String testName = result.getMethod().getMethodName();
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")){
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test passed successfully!","Test End:"+testName+" - ✔ Test Passed ");
			// Start logging in Extent Report
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test passed successfully!","Test End:"+testName+" - ✔ Test Passed ");
		}
		else {
			ExtentManager.logStepValidationForAPI("Test End: "+ testName +" - ✔ Test Passed ");
		}			
	}
	
	// Triggers when a Test Failed
	@Override
	public void onTestFailure(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
		ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!","Test End:"+testName+" - ❌ Test Failed ");
	}
		else {
		ExtentManager.logFailureAPI("Test End:"+testName+" - ❌ Test Failed ");
		}
	}

	// Triggers when test skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test skipped" + testName);
	}

	// Triggers when a suit Starts
	@Override
	public void onStart(ITestContext context) {
		// Initialize the extent report
		ExtentManager.getReporter();
	}

	// Triggers when suit ends
	@Override
	public void onFinish(ITestContext context) {
		//
		System.out.println("onFinish() method executed ----->   ");
		ExtentManager.endTest();
	}

}
