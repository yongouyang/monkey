package org.monkey.web.object

import org.apache.http.message.BasicHttpResponse
import org.monkey.common.utils.config.ApplicationStartupUtils


class DailyPriceApi extends ApiSupport<DailyPriceApi> {

    public BasicHttpResponse create(String dailyPricePayload) {
        return post("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/DailyPrice", dailyPricePayload)
    }

    public Maybe<Map> find(String dailyPriceUri) {
        return get("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}${dailyPriceUri}", Map)
    }
}
