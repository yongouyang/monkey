package org.monkey.web.object

import org.monkey.common.utils.config.ApplicationStartupUtils

class WelcomeApi extends ApiSupport<WelcomeApi> {

    public Maybe<String> welcome() {
        return get("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/welcome", String)
    }

    public Maybe<Map> welcome(String name) {
        return get("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/welcome/${name}", Map)
    }

    public Maybe<Map> welcomeWithError() {
        return get("http://localhost:${ApplicationStartupUtils.monkeyHttpPort}/welcomeWithError", Map)
    }

}
