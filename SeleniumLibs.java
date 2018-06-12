package com.example.demo;

import java.util.*;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;

import com.google.common.base.Function;
import com.thoughtworks.selenium.Selenium;

/**
 * This library is a wrapper for Selenium webdriver
 * 
 * @author ashish
 * 
 */
public class SeleniumLibs {

	public static WebDriver driver;
	public static FirefoxProfile profile;
	public static WebDriverWait wait;
	private static DesiredCapabilities capabilities;
	private static String FIREFOX_PATH = System.getProperty("FIREFOX_BINARY");

	/**
	 * Method to start the selenium using webdriver
	 */
	public static void startSelenium(String serverUrl) {
		System.out.println("FF Path is :" +FIREFOX_PATH);
		if (false) {
			capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("firefox_binary", FIREFOX_PATH);
			driver = new FirefoxDriver(capabilities);
		} else {
			driver = new FirefoxDriver();
		}
		wait = new WebDriverWait(driver, 90);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().maximize();
		driver.get(serverUrl);
	}

	/**
	 * Method to start the selenium using webdriver
	 */
	public static void startSSLSelenium(String serverUrl) {
		profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		if (!FIREFOX_PATH.equals("")) {
			File binaryFile = new File(FIREFOX_PATH);
			FirefoxBinary firefoxBinary = new FirefoxBinary(binaryFile);
			driver = new FirefoxDriver(firefoxBinary, profile);
		} else {
			driver = new FirefoxDriver(profile);
		}
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 90);
		driver.get(serverUrl);
	}

	/**
	 * Method to start the selenium using webdriver
	 */
	public static void startSelenium(WebDriver myDriver, String serverUrl) {
		myDriver.get(serverUrl);
		((JavascriptExecutor) myDriver)
		.executeScript("window.moveTo(0, 0); window.resizeTo(screen.width, screen.height);");
	}

	/**
	 * Method to stop selenium
	 */
	public static void stopSelenium() {
		driver.quit();
		driver = null;
	}

	/**
	 * Methos to stop Selenium using webdriver
	 */
	public static void stopSelenium(WebDriver myDriver) {
		myDriver.quit();
		myDriver = null;
	}

	/**
	 * finds the element and return particular webElement elements
	 * 
	 * @param locator
	 */
	public static WebElement findElement(By locator) {
		return driver.findElement(locator);

	}

	/**
	 * finds the element and return true if it exists
	 * 
	 * @param locator
	 */
	public static boolean findElements(String locator) {
		if (driver.findElements(By.xpath(locator)).size() > 0) {
			return true;
		} else
			return false;
	}

	/**
	 * finds the element and return true if it exists
	 * 
	 * @param locator
	 */
	public static boolean findElements(WebDriver myDriver, String locator) {
		if (myDriver.findElements(By.xpath(locator)).size() > 0) {
			return true;
		} else
			return false;
	}

	/**
	 * This method is used to get the webdriver instance from Selenium 1.0
	 * 
	 * @return
	 */
	/*
	 * cwesley 5/12/14 - Commented out unused method public static WebDriver
	 * getDriverInstance() { WebDriver driverInstance =
	 * ((WebDriverBackedSelenium) selenium) .getWrappedDriver(); return
	 * driverInstance; }
	 */

	/**
	 * This method is used to get the firefox web driver instance.
	 * 
	 * @return
	 */
	public static WebDriver getFirefoxDriverInstance() {
		WebDriver firefoxDriverInstance;
		if (!FIREFOX_PATH.equals("")) {
			capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("firefox_binary", FIREFOX_PATH);
			firefoxDriverInstance = new FirefoxDriver(capabilities);
		} else {
			firefoxDriverInstance = new FirefoxDriver();
		}
		return firefoxDriverInstance;
	}

	/**
	 * Wait until element is visible
	 * 
	 * @param locator
	 * @param time
	 */
	public static void waitForElement(By locator, int time) {
		waitForElement(driver, locator, time);
	}

	/**
	 * Wait until element is visible
	 * 
	 * @param myDriver
	 *            - a <code>WebDriver</code> instance for the second browser
	 * @param locator
	 * @param time
	 */
	public static void waitForElement(WebDriver myDriver, By locator, int time) {
		int i = 1;
		long end = System.currentTimeMillis() + time;

		while (System.currentTimeMillis() < end) {
			Boolean exists = myDriver.findElements(locator).size() != 0;
			if (exists) {
				WebElement resultsDiv = myDriver.findElement(locator);
				if (resultsDiv.isDisplayed()) {
					break;
				}
			}
		}
	}

	/**
	 * This method waits until the element in located in the page
	 * 
	 * @param locator
	 * @return
	 */
	static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
		return new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(locator);
			}
		};
	}

	/**
	 * Wait until the element is present
	 * 
	 * @param locator
	 */
	public static void waitForElementPresent(final By locator) {
		wait.until(presenceOfElementLocated(locator));
	}

	/**
	 * Wait until element is present, if string for the locator is passed along
	 * with its type (xpath, link, class etc) then By element is constructed
	 * 
	 * @param locator
	 * @param locatorType
	 */
	public static void waitForElementPresent(String locator, String locatorType) {
		wait.until(presenceOfElementLocated(getLocatorBy(locator, locatorType)));
	}

	/**
	 * Returns boolean and checks if element is visible
	 * 
	 * @param myDriver
	 * @param locator
	 * @return
	 */
	public static boolean isElementVisible(WebDriver myDriver, By locator) {
		WebElement resultsDiv = myDriver.findElement(locator);
		return resultsDiv.isDisplayed();
	}

	/**
	 * Returns boolean and checks if element is visible
	 * 
	 * @param locator
	 * @return
	 */
	public static boolean isElementVisible(By locator) {
		return isElementVisible(driver, locator);
	}

	/**
	 * Returns the By locator if string for the locator is passed along with its
	 * type (xpath, link, class etc) then By element is constructed
	 * 
	 * @param locator
	 * @param locatorType
	 * @return
	 */
	public static By getLocatorBy(String locator, String locatorType) {
		By by = null;
		if (locatorType.equals("xpath")) {
			by = By.xpath(locator);
			return by;
		} else if (locatorType.equals("name")) {
			by = By.name(locator);
			return by;
		} else if (locatorType.equals("id")) {
			by = By.id(locator);
			return by;
		}
		return by;
	}

	/**
	 * Method to sleep for specified time
	 * 
	 * @param i
	 */
	public static void waitSeconds(int i) {
		try {

			int sleepTime = i * 1000;
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e.toString(), e);
		}
	}

	/**
	 * Checks if element is visible. If the specified identifier has multiple
	 * elements then it checks at-least one element is present
	 * 
	 * @param xpathLocator
	 * @param timeoutInSec
	 * @return
	 * @throws Exception
	 */
	public static boolean isElementPresent(String xpathLocator, int timeoutInSec) throws Exception {
		return isElementPresent(driver, xpathLocator, timeoutInSec);
	}

	/**
	 * Checks if element is visible in a second browser. If the specified
	 * identifier has multiple elements then it checks atleast one element is
	 * present
	 * 
	 * @param myDriver
	 *            the <code>WebDriver</code> object representing the second
	 *            browser.
	 * @param xpathLocator
	 * @param timeoutInSec
	 * @return true if element is found in the resulting page, false otherwise
	 * @throws Exception
	 */
	public static boolean isElementPresent(WebDriver myDriver, String xpathLocator, int timeoutInSec) throws Exception {
		Boolean exists = myDriver.findElements(By.xpath(xpathLocator)).size() != 0;
		if (exists) {
			return exists;
		} else {
			myDriver.manage().timeouts().implicitlyWait(timeoutInSec, TimeUnit.SECONDS);
			exists = myDriver.findElements(By.xpath(xpathLocator)).size() != 0;
			return exists;
		}
	}

	/**
	 * Wait until the busy icon is attached to the dom
	 * 
	 * @param myDriver
	 * @throws Exception
	 */
	public static void waitUntilBusy(WebDriver myDriver) throws Exception {
		long end = System.currentTimeMillis() + 60000;
		WebElement element = myDriver.findElement(By.xpath("//"));
		while (System.currentTimeMillis() < end) {
			try {
				element.getAttribute("title");
			} catch (StaleElementReferenceException e) {
				break;
			}
		}
	}

	/**
	 * Wait until the busy icon is attached to the dom
	 * 
	 * @throws Exception
	 */
	public static void waitUntilBusy() throws Exception {
		waitUntilBusy(driver);
	}

	/**
	 * Perform mouse hover action
	 * 
	 * @param xpath
	 * @throws Exception
	 */
	public static void mouseHover(WebDriver myDriver, String xpath) throws Exception {

		WebElement myElement = myDriver.findElement(By.xpath(xpath));
		Actions builder = new Actions(myDriver);
		builder.moveToElement(myElement).build().perform();
	}

	/**
	 * Perform mouse hover action
	 * 
	 * @param xpath
	 * @throws Exception
	 */
	public static void mouseHover(String xpath) throws Exception {
		mouseHover(driver, xpath);
	}

	/**
	 * Method to perform double click
	 * 
	 * @param myDriver
	 * @param bylocator
	 * @throws Exception
	 */
	public static void doubleClick(WebDriver myDriver, By bylocator) throws Exception {
		Actions action = new Actions(myDriver);
		action.doubleClick(myDriver.findElement(bylocator));
		action.perform();
		waitUntilBusy();
	}

	/**
	 * Method to perform double click
	 * 
	 * @param bylocator
	 * @throws Exception
	 */
	public static void doubleClick(By bylocator) throws Exception {
		doubleClick(driver, bylocator);
	}

	/**
	 * Click the locator
	 * 
	 * @param bylocator
	 * @throws Exception
	 */
	public static void click(By bylocator) throws Exception {
		click(driver, bylocator);
	}

	/**
	 * Click the locator
	 * 
	 * @param myDriver
	 *            a <code>WebDriver</code> instance for a second browser
	 * @param bylocator
	 *            an Xpath for the location to be clicked
	 * @throws Exception
	 */
	public static void click(WebDriver myDriver, By bylocator) throws Exception {
		myDriver.findElement(bylocator).click();
	}

	/**
	 * Click the locator and wait until element is busy
	 * 
	 * @param myDriver
	 * @param bylocator
	 * @throws Exception
	 */
	public static void clickWaitUntilBusy(WebDriver myDriver, By bylocator) throws Exception {
		myDriver.findElement(bylocator).click();
		waitUntilBusy(myDriver);
	}

	/**
	 * Click the locator and wait until element is busy
	 * 
	 * @param bylocator
	 * @throws Exception
	 */
	public static void clickWaitUntilBusy(By bylocator) throws Exception {
		driver.findElement(bylocator).click();
		waitUntilBusy();
	}

	/**
	 * Click the element and wait until element is present
	 * 
	 * @param myDriver
	 * @param bylocator
	 * @param element
	 * @throws Exception
	 */
	public static void clickWaitForElementToAppear(WebDriver myDriver, By bylocator, By element) throws Exception {
		myDriver.findElement(bylocator).click();
		waitForElementPresent(element);
	}

	/**
	 * Click the element and wait until element is present
	 * 
	 * @param bylocator
	 * @param element
	 * @throws Exception
	 */
	public static void clickWaitForElementToAppear(By bylocator, By element) throws Exception {
		clickWaitForElementToAppear(driver, bylocator, element);
	}

	/**
	 * Method to perform click at the center
	 * 
	 * @param bylocator
	 * @throws Exception
	 */
	public static void clickAtMiddleOfElement(By bylocator) throws Exception {
		Actions action = new Actions(driver);
		action.click(driver.findElement(bylocator));
		action.release();
		waitUntilBusy();
	}

	/**
	 * Method to take screenshot
	 * 
	 * @return
	 */
	public static File takeScreenShot() {
		File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return screenShotFile;
	}

	/**
	 * Method to take ScreenShot using Web Driver
	 */
	public static File takeScreenShot(WebDriver driver) {
		File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return screenShotFile;
	}

	/**
	 * Verify if text is present in the page
	 * 
	 * @param myDriver
	 * @param value
	 * @return
	 */
	public static boolean verifyTextPresent(WebDriver myDriver, String value) {
		return getPageSource(myDriver).contains(value);
	}

	/**
	 * Retrieve the source of the current page.
	 * 
	 * @param myDriver
	 * @return a <code>String</code> containing the source of the current page
	 */
	public static String getPageSource(WebDriver myDriver) {
		return myDriver.getPageSource();
	}

	/**
	 * Retrieve the source of the current page.
	 * 
	 * @return a <code>String</code> containing the source of the current page
	 */
	public static String getPageSource() {
		return getPageSource(driver);
	}

	/**
	 * Verify if text is present in the page
	 * 
	 * @param value
	 * @return
	 */
	public static boolean verifyTextPresent(String value) {
		return verifyTextPresent(driver, value);
	}

	/**
	 * Get the text of the element
	 * 
	 * @param myDriver
	 * @param elementID
	 * @return
	 */
	public static String getTextForElement(WebDriver myDriver, String elementID) {
		return myDriver.findElement(By.xpath(elementID)).getText();
	}

	/**
	 * Get the text of the element
	 * 
	 * @param elementID
	 * @return
	 */
	public static String getTextForElement(String elementID) {
		return getTextForElement(driver, elementID);
	}

	/**
	 * Type the text value
	 * 
	 * @param by
	 * @param textField
	 */
	public static void type(By by, String textField) {
		type(driver, by, textField);
	}

	/**
	 * Type the text value for the second browser
	 * 
	 * @param myDriver
	 *            a <code>WebDriver</code> instance for a second browser
	 * @param by
	 *            an Xpath locator for the text field
	 * @param textField
	 *            the String to be entered in the text field
	 */
	public static void type(WebDriver myDriver, By by, String textField) {
		myDriver.findElement(by).sendKeys(textField);
	}

	/**
	 * Clear the text box
	 * 
	 * @param by
	 */
	public static void clearText(By by) {
		clearText(driver, by);
	}

	/**
	 * Clear the text box
	 * 
	 * @param myDriver
	 * @param by
	 */
	public static void clearText(WebDriver myDriver, By by) {
		myDriver.findElement(by).clear();
	}

	/**
	 * Get the value of the element using xpath locator
	 * 
	 * @param myDriver
	 * @param elementID
	 * @return
	 */
	public static String getValueOfElement(WebDriver myDriver, String elementID) {
		return myDriver.findElement(By.xpath(elementID)).getAttribute("value");
	}

	/**
	 * Get the value of the element using xpath locator
	 * 
	 * @param elementID
	 * @return
	 */
	public static String getValueOfElement(String elementID) {
		return getValueOfElement(driver, elementID);
	}

	/**
	 * Get the value of the element using <code>By</code> object
	 * 
	 * @param myDriver
	 * @param by
	 * @return
	 */
	public static String getValueOfElementUsingBy(WebDriver myDriver, By by) {
		return myDriver.findElement(by).getAttribute("value");
	}

	/**
	 * Get the value of the element using <code>By</code> object
	 * 
	 * @param by
	 * @return
	 */
	public static String getValueOfElementUsingBy(By by) {
		return getValueOfElementUsingBy(driver, by);
	}

	/**
	 * Check whether a checkbox or radio button is selected
	 * 
	 * @param myDriver
	 * @param by
	 * @return
	 */
	public static boolean isSelected(WebDriver myDriver, By by) {
		return myDriver.findElement(by).isSelected();
	}

	/**
	 * Check whether a checkbox or radio button is selected
	 * 
	 * @param by
	 * @return
	 */
	public static boolean isSelected(By by) {
		return isSelected(driver, by);
	}

	/**
	 * Get the table count
	 * 
	 * @param myDriver
	 * @param xpath
	 * @return
	 */
	public static int getTableRowCount(WebDriver myDriver, String xpath) {
		String rowXpath = xpath + "/tbody/tr";
		WebElement tableElement = myDriver.findElement(By.xpath(xpath));
		List<WebElement> trCollection = tableElement.findElements(By.xpath(rowXpath));
		return trCollection.size();
	}

	/**
	 * Get the data for a user
	 * 
	 * @param user
	 *            name
	 * @param Row
	 *            data of a particular user
	 * @return nothing
	 */

	public static void getDataUser(String user, int count) {
		String session;
		for (int i = 1; i <= count; i++) {
			session = SeleniumLibs.getTextForElement("//td[text()='" + i + "']/..");
		}
	}

	/**
	 * Get all session data from table
	 * 
	 * @return nothing
	 */

	public static void getData() {
		WebElement table = driver.findElement(By.xpath("//td[2]/div/table/tbody"));
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		Iterator<WebElement> i = allRows.iterator();
		while (i.hasNext()) {
			WebElement allRow = i.next();
			List<WebElement> cells = allRow.findElements(By.tagName("td"));
			Iterator<WebElement> j = cells.iterator();
			while (j.hasNext()) {
				WebElement cell = j.next();
			}
		}
	}

	/**
	 * Get the table row count
	 * 
	 * @param xpath
	 * @return integer number
	 */

	public static int getTableRC(String xpath) {
		return getcount(driver, xpath);
	}

	public static int getcount(WebDriver mydriver, String xpath) {
		String i = xpath + "/tbody/tr";
		int count;
		count = mydriver.findElements(By.xpath(i)).size();
		return count;
	}

	/**
	 * Get the table row count
	 * 
	 * @param xpath
	 * @return
	 */
	public static int getTableRowCount(String xpath) {
		return getTableRowCount(driver, xpath);
	}

	/**
	 * Get the dropdown list
	 * 
	 * @param by
	 * @return
	 */
	public static Select getDropDownList(By by) {
		WebElement dropDownListBox = driver.findElement(by);
		Select select = new Select(dropDownListBox);
		return select;
	}

	/**
	 * Get the dropdown list
	 * 
	 * @param myDriver
	 *            a <code>WebDriver</code> object representing a separate
	 *            browser instance.
	 * @param by
	 * @return a <code>Select</code> object for the desired drop down list
	 */
	public static Select getDropDownList(WebDriver myDriver, By by) {
		WebElement dropDownListBox = myDriver.findElement(by);
		Select select = new Select(dropDownListBox);
		return select;
	}

	/**
	 * Select element from the drop down list using given text
	 * 
	 * @param select
	 * @param selectText
	 */
	public static void selectListElement(Select select, String selectText) {
		select.selectByVisibleText(selectText);
	}

	/**
	 * Select element from the drop down list using Value
	 * 
	 * @param select
	 * @param selectText
	 */
	public static void selectListElementByValue(Select select, String selectValue) {
		select.selectByValue(selectValue);
	}

	/**
	 * Get the current URL
	 * 
	 * @param myDriver
	 *            - the <code>WebDriver</code> instance in use
	 * @return the URL for browser instance in use
	 */
	public static String getCurrentUrl(WebDriver myDriver) {
		return myDriver.getCurrentUrl();
	}

	/**
	 * Get the current URL
	 * 
	 * @return the URL for browser instance in use
	 */
	public static String getCurrentUrl() {
		return getCurrentUrl(driver);
	}

	/**
	 * Load a new web page in the current browser window
	 * 
	 * @param myDriver
	 *            - the <code>WebDriver</code> instance in use
	 * @param url
	 *            - a <code>String</code> containing the URL to be loaded
	 */
	public static void loadUrl(WebDriver myDriver, String url) {
		myDriver.get(url);
	}

	/**
	 * Load a new web page in the current browser window
	 * 
	 * @param url
	 *            - a <code>String</code> containing the URL to be loaded
	 */
	public static void loadUrl(String url) {
		loadUrl(driver, url);
	}

	/**
	 * Getter method for driver
	 * 
	 * @return the <code>WebDriver</code> reference
	 */
	public static WebDriver getDriver() {
		return driver;
	}

	/**
	 * Get the selected element from the drop down
	 * 
	 * @param select
	 * @param selectText
	 */
	public static String getSelectedListElement(Select select) {
		WebElement option = select.getFirstSelectedOption();
		String selectedOption = option.getText();
		return selectedOption;
	}

	/**
	 * Navigate to URL
	 * 
	 * @param String
	 *            - url
	 * 
	 */
	public static void navigateToURL(String url) {
		driver.navigate().to(url);
	}

	/**
	 * Navigate to URL
	 * 
	 * @param String
	 *            - url
	 * @param WebDriver
	 *            - myDriver
	 */
	public static void navigateToURL(WebDriver myDriver, String url) {
		myDriver.navigate().to(url);
	}

	/**
	 * Refresh the Page
	 * 
	 * @param WebDriver
	 *            -myDriver
	 */
	public static void refresh(WebDriver myDriver) {
		myDriver.navigate().refresh();
	}

	/**
	 * Refresh the Page
	 * 
	 * @param WebDriver
	 *            -myDriver
	 */
	public static void refresh() {
		driver.navigate().refresh();
	}

	/**
	 * Return the <code>Set</code> of handles for the open windows.
	 * 
	 * @param myDriver
	 *            - the <code>WebDriver</code> instance in use
	 * @return a <code>Set</code> of handles for the windows opened by myDriver
	 */
	public static Set<String> getWindowHandles(WebDriver myDriver) {
		return myDriver.getWindowHandles();
	}

	/**
	 * Return the <code>Set</code> of handles for the open windows.
	 * 
	 * @return a <code>Set</code> of handles for the windows opened by the
	 *         default <code>WebDriver</code> instance
	 */
	public static Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	/**
	 * Wait for an element to be visible to avoid
	 * <code>ElementNotVisibleException</code>
	 * 
	 * @param locator
	 *            - The xpath for the element that should be visible
	 */
	public static void waitForElementVisible(String locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
	}
	
//	Wait wait = new FluentWait(driver)
//		     .withTimeout(30, SECONDS) // Waiting 30 seconds for an element to be present on the page,               checking
//
//		    .pollingEvery(5, SECONDS) // for its presence once every 5 seconds.
//
//		    .ignoring(NoSuchElementException.class);
//
//		     WebElement fw = wait.until(new Function() {
//
//		    public WebElement apply(WebDriver driver) {
//
//		    return driver.findElement(By.id("fw"));
//
//		    }

//		   });

}