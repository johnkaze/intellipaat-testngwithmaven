package util;

import base.TestBase;
import com.google.common.base.Function;
import base.Constants;
import base.PropertyConstants;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static base.TestBase.driver;

/**
 * @author Chandrashekhar Gomasa
 * @project TestNGWithMavenFramework
 */

public class SeleniumUtils {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);

    public int implicitWait;
    public int explicitWait;
    public int pageRefreshWait;
    public int waitInMilliSec;
    public int refreshWaitInMilliSec;
    public int alertWaitInMilliSec;

    public SeleniumUtils(Properties properties) {
        implicitWait = Integer.parseInt(properties.getProperty(PropertyConstants.IMPLICIT_WAIT));
        explicitWait = Integer.parseInt(properties.getProperty(PropertyConstants.EXPLICIT_WAIT));
        pageRefreshWait = Integer.parseInt(properties.getProperty(PropertyConstants.PAGE_REFRESH_WAIT));
        waitInMilliSec = Integer.parseInt(properties.getProperty(PropertyConstants.WAIT_IN_MILLI_SEC));
        alertWaitInMilliSec = Integer.parseInt(properties.getProperty(PropertyConstants.ALERT_WAIT_IN_MILLI_SEC));
        refreshWaitInMilliSec = Integer.parseInt(properties.getProperty(PropertyConstants.REFRESH_WAIT_IN_MILLI_SEC));
    }

    public enum Identifier {ID, NAME, CLASS_NAME, CSS, XPATH}

    /**
     * Navigate to the given url.
     */
    public void getUrl(String url) {
        if (driver == null)
            return;
        driver.get(url);
    }

    /**
     * Find element with given parameter.
     */
    public WebElement findElement(WebDriver driver, Identifier identifier, String locator) {
        By by = getByLocator(identifier, locator);
        return driver.findElement(by);
    }

    /**
     * Find all element with given parameter.
     */
    public List<WebElement> findElements(WebDriver driver, Identifier identifier, String locator) {
        By by = getByLocator(identifier, locator);
        return driver.findElements(by);
    }

    public By getByLocator(Identifier identifier, String locator) {
        By by = null;
        switch (identifier) {
            case ID:
                by = By.id(locator);
                break;
            case NAME:
                by = By.name(locator);
                break;
            case CLASS_NAME:
                by = By.className(locator);
                break;
            case CSS:
                by = By.cssSelector(locator);
                break;
            case XPATH:
                by = By.xpath(locator);
                break;
        }
        return by;
    }

    /**
     * Send text key to webelement. Clear the text before sending the key.
     */
    public void sendKeys(WebElement webElement, String text) {
        waitForElementDisplay(webElement, explicitWait);
        webElement.sendKeys(text);
    }

    public void modifyEnteredText(String textboxId, String inputData) throws InterruptedException {
        WebElement element = explicitWait(driver, textboxId);
        element.clear();
        element.sendKeys(inputData);
        Thread.sleep(50);
    }

    /**
     * Waits until given element is displayed.
     */
    public boolean waitForElementDisplay(WebElement webElement, int timeOut) {
        try {
            Wait<WebElement> wait = new FluentWait<WebElement>(webElement).withTimeout(timeOut, TimeUnit.SECONDS).
                    ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(new Function<WebElement, Boolean>() {
                           @Override
                           public Boolean apply(WebElement element) {
                               return element.isDisplayed();
                           }
                       }
            );
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits till given element is clickable.
     */
    public boolean waitForElementClickable(WebDriver driver, Identifier identifier, String locator) {
        try {
            Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(explicitWait, TimeUnit.SECONDS).
                    ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.elementToBeClickable(getByLocator(identifier, locator)));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits until page is loaded.
     */
    public void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, explicitWait);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        });
    }

    public WebElement explicitWait(WebDriver driver, final String elementLoc) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(50, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        if (elementLoc.contains(".//")) {
            WebElement element = wait.until(new Function<WebDriver, WebElement>() {

                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.xpath(elementLoc));
                }
            });
            return element;
        } else {
            WebElement element = wait.until(new Function<WebDriver, WebElement>() {

                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.id(elementLoc));
                }
            });
            return element;
        }
    }

    public String takeScreenshot(WebDriver driver) {

        TakesScreenshot screenshot = (TakesScreenshot) driver;

        File source = screenshot.getScreenshotAs(OutputType.FILE);

        File destination = new File(Constants.SCREENSHOT_DIR + "Screenshot_" + TestBase.getCurrentTimeStamp() + ".png");
        try {
            org.apache.commons.io.FileUtils.copyFile(source, destination);

        } catch (IOException e) {
        }
        return destination.getAbsolutePath();
    }

    public void implicitlyWait(int timeOut, TimeUnit timeUnit) {
        driver.manage().timeouts().implicitlyWait(timeOut, timeUnit);
    }

    public void moveToElement(WebDriver driver, WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
        } catch (StaleElementReferenceException e) {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
        }
    }

    public void selectByValueFromDropDown(WebElement element, String value) {
        Select selectFromDropDown = new Select(element);
        selectFromDropDown.selectByValue(value);
    }


    public void selectByVisibleTextFromDropDown(WebElement element, String value) {
        Select selectFromDropDown = new Select(element);
        selectFromDropDown.selectByVisibleText(value);
    }

    public void selectByIndexFromDropDown(WebElement element, int index) {
        Select selectFromDropDown = new Select(element);
        selectFromDropDown.selectByIndex(index);
    }

    public boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public static void setClipboardData(String path) {
        //StringSelection is a class that can be used for copy and paste operations.
        StringSelection stringSelection = new StringSelection(path);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    // code for file upload
    public void uploadFile(String pathOfFile) throws Exception {

        try {
            //Setting clipboard with file location
            setClipboardData(pathOfFile);

            //native key strokes for CTRL, V and ENTER keys
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


}
