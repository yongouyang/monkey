package org.monkey.common.utils.config

import com.mongodb.ServerAddress
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

    @Test
    public void getMongoDbAddress() {
        assert preferences.getMongoDbAddress() == new ServerAddress("monkey-dev-01", 6646)
    }

    @Test
    public void getMongoDbDefaultDbName() {
        assert preferences.getMongoDbDefaultDbName() == "monkey-youyang-TESTING"
    }

    @Test
    public void getMongoDbWriteUser() {
        assert preferences.getMongoDbWriteUser() == "writeUser"
    }

    @Test
    public void getMongoDbWritePassword() {
        assert preferences.getMongoDbWritePassword() == "writePass" as char[]
    }

    @Test
    public void getMongoDbAdminUser() {
        assert preferences.getMongoDbAdminUser() == "adminUser"
    }

    @Test
    public void getMongoDbAdminPassword() {
        assert preferences.getMongoDbAdminPassword() == "adminPass" as char[]
    }

}
