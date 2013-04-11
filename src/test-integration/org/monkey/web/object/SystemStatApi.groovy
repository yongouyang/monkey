package org.monkey.web.object

class SystemStatApi extends ApiSupport<SystemStatApi> {

    public Maybe<List> cpuStat() {
        return get("${baseUri_core}/systemStat/cpu", List, Map)
    }

    public Maybe<Map> memoryStat() {
        return get("${baseUri_core}/systemStat/memory", Map)
    }

    public Maybe<Map> swapStat() {
        return get("${baseUri_core}/systemStat/swap", Map)
    }
}
