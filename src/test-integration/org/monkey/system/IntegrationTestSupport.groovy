package org.monkey.system

import org.junit.BeforeClass
import org.monkey.web.object.StatusMonitoringApi
import org.monkey.web.object.WelcomeApi

class IntegrationTestSupport {

    StatusMonitoringApi statusMonitoringApi = new StatusMonitoringApi()
    WelcomeApi welcomeApi = new WelcomeApi()

    @BeforeClass
    public static void beforeClass() {
        TestingSystem.init()
    }
}
