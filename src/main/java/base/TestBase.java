package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import util.FileUtils;
import util.SeleniumUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * @author Chandrashekhar Gomasa
 * @project TestNGWithMavenFramework
 */

public class TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TestBase.class);

    //environment property instance
    public static Properties properties;
    //selenium utility instance
    public static SeleniumUtils seleniumUtils = null;
    //selenium WebDriver instance
    public static WebDriver driver;
    public static WebDriverWait wait;
    //Extent Report Parent test object
    public static ExtentTest parentTest;
    //Extent Report test object
    public static ExtentTest test;
    //extent report instance
    private static ExtentReports extent = null;
    private static Boolean prevTestSkipped = false;

    /**
     * Instantiate environment properties and report instance
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        FileUtils fileUtils = new FileUtils();        // Creating FileUtils object.
        properties = fileUtils.readTestProps(Constants.PROPERTIES_DIR + "test.properties");
        // getting the name of the report from PropertyConstants properties file
        String reportName = properties.getProperty(PropertyConstants.REPORT_NAME).concat(".html");
        // Generating Extent reports
        initExtentReport(Constants.REPORT_DIR + reportName, context.getSuite().getName());
        initWebdriver(); // Calling WebDriver method to launch browser
        seleniumUtils = new SeleniumUtils(properties); // Calling SeleniumUtils class to use webDriver methods
    }

    /**
     * Close all the resources which are being used in the test run.
     */
    @AfterSuite
    public void afterSuite() {
        driver.quit();
        extent.flush();
    }

    /**
     * Set params required before test method execution is started.
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        if (null == parentTest) {
            parentTest = extent.createTest(getClass().getSimpleName());
            parentTest.getModel().setStartTime(getTime());
        } else if (!parentTest.getModel().getName().equals(getClass().getSimpleName())) {
            parentTest = extent.createTest(getClass().getSimpleName());
            parentTest.getModel().setStartTime(getTime());
        }
        Parameter[] parameters = method.getParameters();
        if (null == test || !test.getModel().getName().equals(method.getName())
                || parameters.length > 0) {
            if (prevTestSkipped && null != test) {
                test.skip("TestPage is skipped");
                prevTestSkipped = false;
            }
            test = parentTest.createNode(method.getName());
            test.getModel().setStartTime(getTime());
        }
    }

    /**
     * Close resources which are used for test method.
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, result.getThrowable());
            if (Boolean
                    .valueOf(properties.getProperty(PropertyConstants.TAKE_SCREENSHOT_ON_FAILURE))) {
                logger.info("Take screen shot");
                String screenshot = seleniumUtils.takeScreenshot(driver);
                try {
                    test.addScreenCaptureFromPath(getRelativePath(Constants.REPORT_DIR, screenshot));
                    logger.info("Screenshot has taken.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (result.getStatus() == ITestResult.SKIP) {
            test.info("TestPage is Skipped");
            prevTestSkipped = true;
        }

        if (result.getStatus() == ITestResult.SUCCESS) {
            prevTestSkipped = false;
            test.pass("TestPage execution is passed.");
        }

        test.getModel().setEndTime(getTime());
        String status = result.getStatus() == ITestResult.SUCCESS ? "PASSED" : "FAILED";

        parentTest.getModel().setEndTime(getTime());
        extent.flush();
    }

    private void initExtentReport(String reportFilePath, String suiteName) {
        if (null == extent) {
            logger.info("Extent report location - " + reportFilePath);
            generateFile(reportFilePath);
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFilePath);
            htmlReporter.config().setDocumentTitle("Automation TestPage Report");
            htmlReporter.config().setReportName("Suite - " + suiteName + " - Result");
            htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
            htmlReporter.config().setTheme(Theme.STANDARD);
            htmlReporter.setAppendExisting(false);
            htmlReporter.config().setChartVisibilityOnOpen(true);
            htmlReporter.config().setEncoding("utf-8");
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setReportUsesManualConfiguration(true);
        }
    }

    // Method to get browser name from Constants.properties file
    public String getBrowser() {
        String browser = System.getProperty(Constants.BROWSER);
        if (null == browser) {
            return Constants.CHROME;
        }
        return browser;
    }

    // Initializing webdriver to open browser
    public WebDriver getWebDriver() {
        WebDriver driver = null;
        String browser = getBrowser();
        switch (browser) {
            case Constants.CHROME:
                System.setProperty("webdriver.chrome.driver", Constants.DRIVER_DIR + Constants.CHROME_DRIVER_NAME);
                driver = new ChromeDriver();
                break;
            case Constants.FIREFOX:
                System.setProperty("webdriver.gecko.driver", Constants.DRIVER_DIR + Constants.GECKO_DRIVER_NAME);
                driver = new FirefoxDriver();
                break;
        }
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Initialize selenium WebDriver instance.
     */
    public void initWebdriver() {
        driver = getWebDriver();
    }

    public synchronized static long getCurrentTimeStamp() {
        return System.nanoTime();
    }

    public static Date getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static void generateFile(String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try {
            FileWriter writer = new FileWriter(file);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRelativePath(String basePath, String otherPath) {
        Path basePathObj = Paths.get(basePath);
        Path otherPathObj = Paths.get(otherPath);
        Path relativePath = basePathObj.relativize(otherPathObj);
        return relativePath.toString();
    }

}




