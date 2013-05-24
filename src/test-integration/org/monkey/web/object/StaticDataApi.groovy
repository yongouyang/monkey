package org.monkey.web.object


class StaticDataApi extends ApiSupport<StaticDataApi> {

    public Maybe<List> downloadSecurity() {
        return get("${baseUri_core}/staticData/download/security", List, Map)
    }
}
