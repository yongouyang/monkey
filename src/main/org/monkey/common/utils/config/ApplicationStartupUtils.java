package org.monkey.common.utils.config;

import com.google.common.collect.Iterables;

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
        Properties defaults = new Properties();
        Properties envOverrides = new Properties();
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            if (key.startsWith("DEFAULT")){
                String newKey = key.replaceFirst("DEFAULT.", "");
                defaults.setProperty(newKey, properties.getProperty(key));
            } else if (key.startsWith(environmentName)) {
                String newKey = key.replaceFirst(environmentName + ".", "");
                envOverrides.setProperty(newKey, properties.getProperty(key));
            }
        }


        for (String key : defaults.stringPropertyNames()) {
            System.setProperty(key, defaults.getProperty(key));
        }

        for (String key : envOverrides.stringPropertyNames()) {
            System.setProperty(key, envOverrides.getProperty(key));
        }
    }

    public static int getMonkeyHttpPort() {
        return Integer.parseInt(System.getProperty(MONKEY_HTTP_PORT));
    }

    public static String getWarPath() {
        return System.getProperty("MONKEY.WAR.PATH");
    }
}
