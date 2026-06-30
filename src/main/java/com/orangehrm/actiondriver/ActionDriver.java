package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver Instance created:");
	}

//Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element: " + elementDescription);
			logger.info("Clicked an element -->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to click element :" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element: ",
					elementDescription + "_unable to click");
			logger.error("Unable to click element: " + e.getMessage());
		}

	}

	// Method to enter text in input field -- void code duplication - fix the
	// multiple calling method
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			// driver.findElement(by).clear();
			// driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on: " + getElementDescription(by) + "--> " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to enter the value: " + e.getMessage());
		}
	}

// method to get text from input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get the text: " + e.getMessage());
			return "";
		}
	}

	// method to compare two text
	public boolean compareText(By by, String expectedText) {
		
		waitForElementToBeVisible(by);
		String actualText = driver.findElement(by).getText();
		if (expectedText.equals(actualText)) {
			applyBorder(by, "green");
			logger.info("Texts are matching : " + actualText + " equals " + expectedText);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "compare text ",
					" Text verified successfully " + actualText + " equals " + expectedText);
			return true;
		} else {
			applyBorder(by, "red");
			logger.error("Texts are not Matching : " + actualText + " not equals " + expectedText);
			ExtentManager.logFailure(BaseClass.getDriver(), "Text comparision Failed! ",
					" Text comparision Failed! " + actualText + " NOT equals " + expectedText);
			return false;
		}
	}

	// Method to check if element is displayed.
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed: " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed" + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed",
					"Element is displayed" + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is NOT displayed",
					"Element is not displayed" + getElementDescription(by));
			return false;
		}
	}

	// wait for page load
	public void waitForPageLoad(int timeOutInSec) {
		
		// configure timeout explicitly
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).pollingEvery(Duration.ofMillis(500)) // optional: set polling interval
				.until((webDriver) -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within: " + timeOutInSec + " seconds" + e.getMessage());
		}
	}

	// scroll to element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to locate element: " + e.getMessage());
		}
	}

// wait for element to be click-able
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable: " + e.getMessage());
		}

	}

	// wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible: " + e.getMessage());
		}
	}

	// Method to get description of element using By locator
	public String getElementDescription(By locator) {
		// Check for null driver or locator for null pointer exception
		if (driver == null)
			return "driver is null";
		if (locator == null)
			return "locator is null";
		// find the element using locator

		WebElement element = driver.findElement(locator);

		// get element Attributes
		String text = element.getText();
		String name = element.getDomAttribute("name");
		String id = element.getDomAttribute("id");
		String className = element.getDomAttribute("class");
		String placeHolder = element.getDomAttribute("placeholder");

		// return the description based on element attribute
		try {
			if (isNotEmpty(name))
				return "Element with name : " + name;
			else if (isNotEmpty(id))
				return "Element with id : " + id;
			else if (isNotEmpty(text))
				return "Element with text : " + text;
			else if (isNotEmpty(className))
				return "Element with class : " + truncate(text, 50);
			else if (isNotEmpty(placeHolder))
				return "Element with placeholder : " + placeHolder;

		} catch (Exception e) {
			logger.error("unable to describe the element " + e.getMessage());
		}

		return "unable to describe the element";
	}

	// Utility method to check String is not NULL or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}
	
	
	// Utility method to truncate long string
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength)
			return value;

		return value.substring(0, maxLength) + "...";

	}

// Utility method to border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			// apply the border
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);

			logger.info("Appllied the border with color " + color + " to element " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element: " + getElementDescription(by));
		}

	}
}