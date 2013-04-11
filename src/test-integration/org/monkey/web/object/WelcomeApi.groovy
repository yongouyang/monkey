package org.monkey.web.object

import org.monkey.common.utils.config.ApplicationStartupUtils

class WelcomeApi extends ApiSupport<WelcomeApi> {

    public Maybe<String> welcome() {
        return get("${baseUri_core}/welcome", String)
    }

    public Maybe<Map> welcome(String name) {
        return get("${baseUri_core}/welcome/${name}", Map)
    }

    public Maybe<Map> welcomeWithError() {
        return get("${baseUri_core}/welcomeWithError", Map)
    }

}
