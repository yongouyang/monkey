package org.monkey.common.utils.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import static org.monkey.common.utils.config.PropertyUtils.toProperties;

@Scope("singleton")
@Component
public class SystemPreferencesImpl implements SystemPreferences {

    public static final String MONKEY_ENV = "MONKEY_ENV";
    public static final Pattern PASSWORD_PROPERTY_PATTERN = Pattern.compile(".*password.*", Pattern.CASE_INSENSITIVE);

    @PostConstruct
    public void init() {
        String envName = System.getProperty(MONKEY_ENV);

        // Load environment specific property file as system properties, without overwriting existing system properties
        System.out.println("Loading environment properties from: Environment - " + envName);
        ResourceBundle envBundle = ResourceBundle.getBundle("MonkeyEnvironment-" + envName);
        mergeToSystemProperties(toProperties(envBundle));

        // load default properties
        ResourceBundle defaultBundle = ResourceBundle.getBundle("MonkeyEnvironment-Defaults");
        mergeToSystemProperties(toProperties(defaultBundle));
    }

    private void mergeToSystemProperties(Properties properties) {
        Set<String> propertyKeys = properties.stringPropertyNames();
        for (String propertyKey : propertyKeys) {
            String existingPropertyValue = System.getProperty(propertyKey);
            if (StringUtils.isBlank(existingPropertyValue)) {
                String propertyValue = properties.getProperty(propertyKey);
                System.out.println("Setting property for environment: " + toString(propertyKey, propertyValue));
                System.setProperty(propertyKey, propertyValue);
            }
        }
    }

    @Override
    public String get(String key) {
        return System.getProperty(key);
    }

    protected String toString(String propertyName, String propertyValue) {
        if (propertyValue != null) {
            if (PASSWORD_PROPERTY_PATTERN.matcher(propertyName).matches()) {
                propertyValue = "****";
            }
        }
        return propertyName + " = " + propertyValue;
    }
}
