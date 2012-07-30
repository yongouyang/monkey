package org.monkey.common.utils.config;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import static org.monkey.common.utils.config.SystemPreferencesImpl.MONKEY_ENV;

public class ApplicationStartupUtils {

    public static final String MONKEY_HTTP_PORT = "MONKEY.HTTP.PORT";

    public static void initStartupOptions(String environmentName) {
        System.setProperty(MONKEY_ENV, environmentName);

        ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationStartupConfig");
        Properties properties = PropertyUtils.toProperties(resourceBundle);
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            String newKey = key.replaceFirst(environmentName + ".", "");
            System.setProperty(newKey, properties.getProperty(key));
        }
    }

    public static int getMonkeyHttpPort() {
        return Integer.parseInt(System.getProperty(MONKEY_HTTP_PORT));
    }
}
