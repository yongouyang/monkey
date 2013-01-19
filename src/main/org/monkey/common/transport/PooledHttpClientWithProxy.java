package org.monkey.common.transport;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.monkey.common.utils.config.SystemPreferences;

public class PooledHttpClientWithProxy extends PooledHttpClient {

    public static PooledHttpClientWithProxy newHttpClientWithLocalProxy(SystemPreferences systemPreferences) {
        // squid's default port number is 3128
        return new PooledHttpClientWithProxy("127.0.0.1", systemPreferences.getInteger("HTTP.PROXY.LOCAL.PORT", 3128));
    }

    public PooledHttpClientWithProxy(String proxyHost, int proxyPort) {
        super();
        setProxy(proxyHost, proxyPort);
    }

    private void setProxy(String proxyHost, int proxyPort) {
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
        getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }
}
