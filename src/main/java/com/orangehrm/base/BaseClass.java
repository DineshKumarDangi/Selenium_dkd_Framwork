package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;

//import io.github.bonigarcia.wdm.WebDriverManager; //add pom dependency for this

public class BaseClass {
	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	//public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	public static final Logger logger = LogManager.getLogger(BaseClass.class);
	
	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties"
			);

		prop.load(fis);
		logger.info("config.properties file loaded");

		// Start the extent report
		// ExtentManager.getReporter(); -- This has been implemented in TestListener
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("Settingup webdriver for : " + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		logger.info("WebDriver Initialized and Browser maximized");
		logger.trace("This is a trace message");
		logger.debug("This is a debug message");
		logger.warn("This is a warng message");
		logger.error("This is a Error message");
		logger.fatal("This is a fatal message");
		

		/*
		 * // initialize the actionDriver only once if(actionDriver==null) {
		 * actionDriver= new ActionDriver(driver);
		 * System.out.println("Actiondriver instance is created"+
		 * Thread.currentThread().getId());
		 * 
		 * }
		 */
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread : " + Thread.currentThread().getId()); //.getId()  threadId()
	}

	/*
	 * initialize the WebDriver based on browser defined in config.properties file
	 */

	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");
		switch (browser.toLowerCase()) {
		case "chrome":

			// Create ChromeOptions
			/*ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--headless"); // Run Chrome in headless mode
			chromeOptions.addArguments("--disable-gpu"); // Disable GPU for headless mode
			//options.addArguments("--window-size=1920,1080"); // Set window size
			chromeOptions.addArguments("--disable-notifications"); // Disable browser notifications
			chromeOptions.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
			chromeOptions.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
*/
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--headless=new"); // modern headless mode
		//	chromeOptions.addArguments("--window-size=1920,1080"); // recommended
			chromeOptions.addArguments("--disable-notifications");
			chromeOptions.addArguments("--no-sandbox");
			chromeOptions.addArguments("--disable-dev-shm-usage");

			// driver = new ChromeDriver();
			driver.set(new ChromeDriver(chromeOptions)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created.");
			// break;
			/*
			 * WebDriverManager.chromedriver().setup();
			 *  driver = new ChromeDriver();
			 * driver.get(url);
			 */
			break;

		case "firefox":
			// Create FirefoxOptions
			FirefoxOptions ffOptions = new FirefoxOptions();
			ffOptions.addArguments("--headless"); // Run Firefox in headless mode
			ffOptions.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			ffOptions.addArguments("--width=1920"); // Set browser width
			ffOptions.addArguments("--height=1080"); // Set browser height
			ffOptions.addArguments("--disable-notifications"); // Disable browser notifications
			ffOptions.addArguments("--no-sandbox"); // Needed for CI/CD environments
			ffOptions.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(ffOptions)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created.");
			break;
		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments("--headless"); // Run Edge in headless mode
			edgeOptions.addArguments("--disable-gpu"); // Disable GPU acceleration
			edgeOptions.addArguments("--window-size=1920,1080"); // Set window size
			edgeOptions.addArguments("--disable-notifications"); // Disable pop-up notifications
			edgeOptions.addArguments("--no-sandbox"); // Needed for CI/CD
			edgeOptions.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver(edgeOptions)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created.");
			break;
		default:
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}
	}

	/*
	 * configure browser settings such as implicit wait , maximize the browser and
	 * navigate to the url
	 */
	private void configureBrowser() {
		// implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		// maximize the browser
		getDriver().manage().window().maximize();

		// navigate to URL
		try {
			getDriver().get(prop.getProperty("url")); // new changes as per thread
		} catch (Exception e) {
			System.out.println("Failed to navigate to browser: " + e.getMessage());
		}

	}

	@AfterMethod
	public synchronized void tearDown() {
		staticWait(2);
		if (driver.get() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver: " + e.getMessage());
			}
			logger.info("Webdriver instance is closed."); // new changes as per thread
			driver.remove();
			actionDriver.remove();
			// ExtentManager.endTest(); //-- This has been implemented in TestListener
//			driver = null ;
//			actionDriver=null;
		}
	}

	// static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	/*
	 * // Driver Getter method public WebDriver getDriver() { return this.driver; }
	 */
	// Driver Setter method

	// Getter method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("Webdriver not initialized.");
			throw new IllegalStateException("Webdriver not initialized.");
		}
		return driver.get();
	}
	/* 
	// check ? - this method is not been used
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}
	*/

	// getter method for ActionDriver
	public synchronized static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver not initialized.");
			throw new IllegalStateException("ActionDriver not initialized.");
		}
		return actionDriver.get();
	}

}
