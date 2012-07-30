package org.monkey.common.utils.config;

import java.util.Properties;
import java.util.ResourceBundle;

public class PropertyUtils {

    public static Properties toProperties(ResourceBundle resourceBundle) {
        Properties properties = new Properties();
        for (String key : resourceBundle.keySet()) {
            properties.setProperty(key, resourceBundle.getString(key));
        }
        return properties;
    }

}
