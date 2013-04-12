package org.monkey.web.api

import org.junit.Test
import org.monkey.common.transport.HttpResponseUtils
import org.monkey.common.utils.AssertUtils
import org.monkey.system.IntegrationTestSupport

class DailyPriceIntegrationTest extends IntegrationTestSupport {

    @Test
    public void canCreateAndLoadADailyPrice() {
        def dailyPrice = [
            ricCode: "0001.HK",
            tradeDate: "2013-03-26",
            open: 13.45,
            high: 14.23,
            low: 13.28,
            close: 13.97,
            adjClose: 13.97
        ]

        def response = dailyPriceApi.create(marshaller.marshall(dailyPrice))

        def dailyPriceUri = HttpResponseUtils.toString(response)
        assert response.statusLine.statusCode == 201
        assert dailyPriceUri == "/DailyPrice?ricCode=0001.HK&tradeDate=2013-03-26"

        def loaded = dailyPriceApi.find(dailyPriceUri).succeed()
        AssertUtils.assertMapMatches(dailyPrice, loaded)
    }
}
