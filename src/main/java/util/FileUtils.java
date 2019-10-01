package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Chandrashekhar Gomasa
 * @project TestNGWithMavenFramework
 */

public class FileUtils {
    /**
     * Read given file as java.util.Properties
     *
     * @return props
     */
    public Properties readPropsFile(String filePath) throws IOException {
        Properties props = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(filePath);
            props.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
        return props;
    }

    /**
     * Load system props & test properties file
     */
    public Properties readTestProps(String filePath) {
        Properties testProps = null;
        try {
            testProps = readPropsFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<Object> iter = testProps.keySet().iterator();

        while (iter.hasNext()) {
            String key = iter.next().toString();
            String value = System.getProperty(key);
            if (value != null) {
                testProps.setProperty(key, value);
            }
        }

        //add system properties which may not exist in property files
        Properties systemProperties = System.getProperties();
        testProps.putAll(systemProperties);
        return testProps;
    }

}
