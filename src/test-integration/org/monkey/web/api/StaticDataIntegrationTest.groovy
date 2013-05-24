package org.monkey.web.api

import org.junit.Test
import org.monkey.common.utils.AssertUtils
import org.monkey.system.IntegrationTestSupport


class StaticDataIntegrationTest extends IntegrationTestSupport {

    @Test
    public void canDownloadSecurity() {
        List<Map<String, Object>> securities = staticDataApi.downloadSecurity().succeed()
        assert securities.size() > 0

        // verify the first one is 0001.HK
        AssertUtils.assertMapMatches([
            ricCode: "0001.HK",
            name: "CHEUNG KONG",
            lotSize: 1000
        ], securities[0] as Map<String, Object>)
    }
}
