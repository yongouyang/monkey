package org.monkey.common.utils.config

import org.junit.Test

import static org.monkey.common.utils.config.ApplicationStartupUtils.MONKEY_HTTP_PORT

class ApplicationStartupUtilsTest {

    @Test
    public void initStartupOptions() {
        assert System.getProperty(MONKEY_HTTP_PORT) == null

        ApplicationStartupUtils.initStartupOptions("TESTING")

        assert System.getProperty(MONKEY_HTTP_PORT) == "8899"
    }

}
