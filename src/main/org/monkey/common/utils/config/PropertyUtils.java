package org.monkey.common.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class PropertyUtils {

    private static final Logger log = LoggerFactory.getLogger(PropertyUtils.class);
    public static final Pattern PASSWORD_PROPERTY_PATTERN = Pattern.compile(".*password.*", Pattern.CASE_INSENSITIVE);

    public static Properties toProperties(ResourceBundle resourceBundle) {
        Properties properties = new Properties();
        for (String key : resourceBundle.keySet()) {
            properties.setProperty(key, resourceBundle.getString(key));
        }
        return properties;
    }

    public static void setSystemProperty(String key, String value) {
        log.info("Setting system property: {}", toString(key, value));
        System.setProperty(key, value);
    }

    protected static String toString(String propertyName, String propertyValue) {
        if (propertyValue != null) {
            if (PASSWORD_PROPERTY_PATTERN.matcher(propertyName).matches()) {
                propertyValue = "****";
            }
        }
        return propertyName + " = " + propertyValue;
    }

}
