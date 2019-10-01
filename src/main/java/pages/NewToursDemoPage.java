package pages;

import base.PropertyConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SeleniumUtils;

import java.util.Properties;

public class NewToursDemoPage {

    private WebDriver driver;
    private Properties properties;
    private SeleniumUtils seleniumUtils;

    @FindBy(xpath = "//input[@name='userName']")
    private WebElement userId;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement password;

    @FindBy(xpath = "//input[@name='login']")
    private WebElement loginButton;

    @FindBy(xpath = "//a[text()='SIGN-OFF']")
    private WebElement logoutButton;

    public NewToursDemoPage(WebDriver driver, Properties properties, SeleniumUtils seleniumUtils) {
        this.driver = driver;
        this.properties = properties;
        this.seleniumUtils = seleniumUtils;
        PageFactory.initElements(driver, this);
    }

    public void loginToNewTours() {
        userId.clear();
        userId.sendKeys("demo");
        password.sendKeys("demo");
        loginButton.click();
        seleniumUtils.waitForPageLoad(driver);
    }

    public String getSignOFF(){
        return logoutButton.getText();
    }

    public  void logououtNewTours() {
        logoutButton.click();
    }

}
