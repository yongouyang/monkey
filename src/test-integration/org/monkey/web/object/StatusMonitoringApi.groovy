package org.monkey.web.object

import org.monkey.common.utils.config.ApplicationStartupUtils


class StatusMonitoringApi extends ApiSupport<StatusMonitoringApi> {

    public Maybe<Map> ping() {
        return get("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/ping", Map)
    }
}
