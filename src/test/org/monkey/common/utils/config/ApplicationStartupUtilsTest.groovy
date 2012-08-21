package org.monkey.common.utils.config

import org.junit.Test

import static org.monkey.common.utils.config.ApplicationStartupUtils.MONKEY_HTTP_PORT

class ApplicationStartupUtilsTest {

    @Test
    public void initStartupOptionsSetupDefaultProperties() {
        assert System.getProperty(MONKEY_HTTP_PORT) == null

        ApplicationStartupUtils.initStartupOptions("env that doesn't exist")

        assert ApplicationStartupUtils.monkeyHttpPort == 8899
        assert ApplicationStartupUtils.warPath == "pkgs/@MONKEY_VERSION@/web"
    }

}
