package automation;

import base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pages.NewToursDemoPage;

public class NewToursDemoTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(NewToursDemoTest.class);// Using slf4j

    @Test(priority = 1)
    public void newTourseDemoLoginTest() {
        Reporter.log("", true);
        Reporter.log("*** Start of Priority 1 ***", true);
        Reporter.log("", true);

        NewToursDemoPage newToursDemoPage = new NewToursDemoPage(driver, properties, seleniumUtils);

        String url = "http://newtours.demoaut.com/";//properties.getProperty(PropertyConstants.URL);
        seleniumUtils.getUrl(url);
        seleniumUtils.waitForPageLoad(driver);

        newToursDemoPage.loginToNewTours();

        seleniumUtils.waitForPageLoad(driver);

        String signOFF = newToursDemoPage.getSignOFF();
        Reporter.log("SignOff text from appln is :: "+signOFF, true);
        Assert.assertEquals(signOFF,"SIGN-OFF");
        
        newToursDemoPage.logououtNewTours();

        Reporter.log("", true);
        Reporter.log("*** End of Priority 1 ***", true);
        Reporter.log("", true);

    }

    @Test(priority = 2)
    public void newTourseDemoLoginTestTwo() {
        Reporter.log("", true);
        Reporter.log("*** Start of Priority 2 ***", true);
        Reporter.log("", true);

        NewToursDemoPage newToursDemoPage = new NewToursDemoPage(driver, properties, seleniumUtils);

        String url = "http://newtours.demoaut.com/";//properties.getProperty(PropertyConstants.URL);
        seleniumUtils.getUrl(url);
        seleniumUtils.waitForPageLoad(driver);

        newToursDemoPage.loginToNewTours();

        seleniumUtils.waitForPageLoad(driver);

        String signOFF = newToursDemoPage.getSignOFF();
        logger.info("SignOff text from appln is :: "+signOFF);
        Assert.assertEquals(signOFF,"SIGN-OF");

        newToursDemoPage.logououtNewTours();

        Reporter.log("", true);
        Reporter.log("*** End of Priority 2 ***", true);
        Reporter.log("", true);

    }

}
