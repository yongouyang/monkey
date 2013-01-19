package org.monkey.common.utils.config

import org.junit.Before
import org.junit.Test

class SystemPreferencesImplTest {

    SystemPreferencesImpl preferences

    @Before
    public void before() {
        PropertyUtils.setSystemProperty("MONKEY_ENV", "TESTING")
        preferences = new SystemPreferencesImpl()
        preferences.init()
    }

    @Test
    public void initSuccessfullyAndCanGetProperties() {
        assert preferences.get("MONKEY.REGION") == "TESTING"
        assert preferences.get("MONKEY.DEFAULT_USED_FOR_TEST") == "Monkey King" // default property used for testing only
    }

    @Test
    public void canGetDefaultIntegerIfNotSet() {
        assert preferences.getInteger("some key", 12) == 12
    }

    @Test
    public void getReturnsNullWhenKeyIsNotFound() {
        assert preferences.get("key that doesn't exist") == null
    }
}
