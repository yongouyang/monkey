package org.monkey.system

import org.junit.BeforeClass
import org.monkey.web.object.StatusMonitoringApi

class IntegrationTestSupport {

    StatusMonitoringApi statusMonitoringApi = new StatusMonitoringApi()

    @BeforeClass
    public static void beforeClass() {
        TestingSystem.init()
    }
}
