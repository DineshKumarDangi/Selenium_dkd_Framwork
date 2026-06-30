package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize extent report
	public static synchronized ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation test report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding system information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java version", System.getProperty("java.version"));
			extent.setSystemInfo("User name", System.getProperty("user.name"));
		}
		return extent;
	}

	// Start the test (Create test and attach it with thread local test)
	public static synchronized ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// End the test
	public static synchronized void endTest() {
		getReporter().flush();
	}

	// Get Current Thread's test
	public static ExtentTest getTest() {
		return test.get();
	}

// Method to get the name of the current test
	public static synchronized String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No test is  currently active for this thread";
		}
	}

// Log step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	// Log a step validation with ScreenShot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
		getTest().pass(logMessage);
		// ScreenShot method
		attachScreenshot(driver, screenShotMessage);
	}
	
	// Log a step validation for API
	public static void logStepValidationForAPI(String logMessage) {
			getTest().pass(logMessage);			
		}
		
	
	// Log a Failure
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
		String colorMessage= String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		// ScreenShot method
		attachScreenshot(driver, screenShotMessage);
	}
	
	// Log a Failure for API
		public static void logFailureAPI( String logMessage) {
			String colorMessage= String.format("<span style='color:red;'>%s</span>", logMessage);
			getTest().fail(colorMessage);
		}

	// log a skip
	public static void logSkip(String logMessage) {
		String colorMessage= String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
		// ScreenShot method
	}

// Take screenshot with date and time in file
	public static synchronized String takeScreenShot(WebDriver driver, String screenShotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		// Formate date and time for file name
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		// Saving the screen to a file
		String destPath = System.getProperty("user.dir") + "/src/test/resources/screenShots/"
				+ screenShotName + "_" + timeStamp + ".png";
		File FinalPath = new File(destPath);
		try {
			FileUtils.copyFile(src, FinalPath);
		} catch (IOException e) {

		}
		// convert screenshot to Base64 for embedding in the report
		String base64Format = convertToBase64(src);
		return base64Format;

	}
	// convert screenshot to base64 format

	public static String convertToBase64(File screenshotFile) {
		String base64Format = "";
		// Read the file content into byte array
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(screenshotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Convert the byte array to Base64 String
		return base64Format;
	}

// Attach screenshot to report using base64
	public static synchronized void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenshotBase64 = takeScreenShot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attache screenshot:" + message);
			e.printStackTrace();
		}

	}

	// Register WebDriver for current thread
	public static void registerDriver(WebDriver driver) {
		//driverMap.put(Thread.currentThread().getId(), driver); // depricated 
		driverMap.put(Thread.currentThread().getId(),driver);
	}

}
