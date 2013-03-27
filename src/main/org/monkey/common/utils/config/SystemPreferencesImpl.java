package org.monkey.common.utils.config;

import com.mongodb.ServerAddress;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import static org.monkey.common.utils.config.PropertyUtils.toProperties;

@Scope("singleton")
@Component
public class SystemPreferencesImpl implements SystemPreferences {

    public static final String MONKEY_ENV = "MONKEY_ENV";

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
                PropertyUtils.setSystemProperty(propertyKey, propertyValue);
            }
        }
    }

    @Override
    public String get(String key) {
        return System.getProperty(key);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        String value = get(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    @Override
    public ServerAddress getMongoDbAddress() {
        String value = get("MONKEY.MONGODB.SET1");
        if (StringUtils.isNotBlank(value)) {
            String[] hostAndPort = value.split(":");
            try {
                return new ServerAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException("You have to specify the mongodb replica set address!");
    }

    private String getWithValidation(String key) {
        String value = get(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        throw new IllegalArgumentException("You have to specify property " + key);
    }

    @Override
    public String getMongoDbDefaultDbName() {
        return getWithValidation("MONKEY.MONGODB.DEFAULT.DB");
    }

    @Override
    public String getMongoDbWriteUser() {
        return getWithValidation("MONKEY.MONGODB.WRITE.USER");
    }

    @Override
    public char[] getMongoDbWritePassword() {
        return getWithValidation("MONKEY.MONGODB.WRITE.PASSWORD").toCharArray();
    }

    @Override
    public String getMongoDbAdminUser() {
        return getWithValidation("MONKEY.MONGODB.ADMIN.USER");
    }

    @Override
    public char[] getMongoDbAdminPassword() {
        return getWithValidation("MONKEY.MONGODB.ADMIN.PASSWORD").toCharArray();
    }


}
