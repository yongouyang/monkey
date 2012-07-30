package org.monkey.common.utils.config

import org.junit.Before
import org.junit.Test

import static org.monkey.common.utils.config.ApplicationStartupUtils.MONKEY_HTTP_PORT

class PropertyUtilsTest {

    ResourceBundle resourceBundle

    @Before
    public void before() {
        resourceBundle = ResourceBundle.getBundle("ApplicationStartupConfig")
    }

    @Test
    public void toPropertiesCanConvertResourceBundleToProperties() {
        def properties = PropertyUtils.toProperties(resourceBundle)

        assert properties.getProperty("TESTING.${MONKEY_HTTP_PORT}") == "8899"
    }
}
