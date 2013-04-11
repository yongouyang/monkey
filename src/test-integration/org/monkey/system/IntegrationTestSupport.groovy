package org.monkey.system

import org.junit.BeforeClass
import org.monkey.common.marshall.json.JacksonJsonMarshaller
import org.monkey.common.marshall.json.JsonMarshaller
import org.monkey.web.object.DailyPriceApi
import org.monkey.web.object.StatusMonitoringApi
import org.monkey.web.object.SystemStatApi
import org.monkey.web.object.WelcomeApi

class IntegrationTestSupport {

    StatusMonitoringApi statusMonitoringApi = new StatusMonitoringApi()
    WelcomeApi welcomeApi = new WelcomeApi()
    DailyPriceApi dailyPriceApi = new DailyPriceApi()
    SystemStatApi systemStatApi = new SystemStatApi()

    JsonMarshaller marshaller = new JacksonJsonMarshaller()

    @BeforeClass
    public static void beforeClass() {
        TestingSystem.init()
    }
}
