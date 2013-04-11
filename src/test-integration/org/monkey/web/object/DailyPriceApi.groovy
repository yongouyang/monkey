package org.monkey.web.object

import org.apache.http.message.BasicHttpResponse
import org.monkey.common.utils.config.ApplicationStartupUtils


class DailyPriceApi extends ApiSupport<DailyPriceApi> {

    public BasicHttpResponse create(String dailyPricePayload) {
        return post("${baseUri_core}/DailyPrice", dailyPricePayload)
    }

    public Maybe<Map> find(String dailyPriceUri) {
        return get("${baseUri_core}${dailyPriceUri}", Map)
    }
}
