package org.monkey.web.api

import org.junit.Test
import org.monkey.system.IntegrationTestSupport


class CanPingSystemTest extends IntegrationTestSupport{

    @Test
    public void ping() {
        assert statusMonitoringApi.ping().succeed() == [success: "success"]
    }
}
