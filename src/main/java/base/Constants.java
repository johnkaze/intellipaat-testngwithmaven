package base;

/**
 * @author Chandrashekhar Gomasa
 * @project TestNGWithMavenFramework
 */

public class Constants {

    public static final String SEPARATOR = System.getProperty("file.separator");

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String PROPERTIES_DIR = new StringBuilder(USER_DIR)
            .append(SEPARATOR).append("src").append(SEPARATOR).append("test").append(SEPARATOR)
            .append("resources").append(SEPARATOR).
                    append("properties").append(SEPARATOR).toString();

    public static final String DRIVER_DIR = new StringBuilder(USER_DIR)
            .append(SEPARATOR).append("src").append(SEPARATOR).append("test").append(SEPARATOR)
            .append("resources").append(SEPARATOR).
                    append("driver").append(SEPARATOR).toString();

    public static final String SCREENSHOT_DIR = new StringBuilder(USER_DIR)
            .append(SEPARATOR).append("target").append(SEPARATOR).append("screenshot").append(SEPARATOR)
            .toString();

    public static final String REPORT_DIR = new StringBuilder(USER_DIR)
            .append(SEPARATOR).append("target").append(SEPARATOR).append("extent-report")
            .append(SEPARATOR).toString();

    public static final String CHROME_DRIVER_NAME = "chromedriver.exe";

    public static final String GECKO_DRIVER_NAME = "geckodriver.exe";

    public static final String BROWSER = "browser";

    public static final String FIREFOX = "firefox";

    public static final String CHROME = "chrome";

    public static final String VALUE = "value";

}
