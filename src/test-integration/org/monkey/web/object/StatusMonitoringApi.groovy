package org.monkey.web.object

import org.monkey.common.utils.config.ApplicationStartupUtils


class StatusMonitoringApi extends ApiSupport {

    public Map ping() {
        marshaller.unmarshall(httpTransport.doGet("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/ping"), Map)
    }
}
