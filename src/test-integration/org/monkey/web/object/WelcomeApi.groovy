package org.monkey.web.object

import org.monkey.common.utils.config.ApplicationStartupUtils

class WelcomeApi extends ApiSupport {

    public String welcome() {
        return httpTransport.doGet("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/welcome")
    }

    public Map<String, Object> welcome(String name) {
        return marshaller.unmarshall(httpTransport.doGet("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/welcome/${name}"), Map)
    }
}
