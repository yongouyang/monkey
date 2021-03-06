package org.monkey.web.object

import org.monkey.common.utils.config.ApplicationStartupUtils


class StatusMonitoringApi extends ApiSupport<StatusMonitoringApi> {

    public Maybe<Map> ping() {
        return get("${baseUri_core}/ping", Map)
    }
}
