package org.monkey.common.transport;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;

public class PooledHttpClient extends DefaultHttpClient {

    private static final int DEFAULT_SOCKET_TIMEOUT = 200000; // 200 seconds socket read timeout
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000; // 30 seconds connection timeout
    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
    private static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 100;

    public static PooledHttpClient newPooledHttpClient() {
        return new PooledHttpClient();
    }

    public PooledHttpClient() {
        super(defaultConnectionManager());
        setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        setSocketTimeout(DEFAULT_SOCKET_TIMEOUT);
        getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
        getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, true);

        addRequestInterceptor(new AllowGZipResponseRequestInterceptor());
        addResponseInterceptor(new GZipDecompressingHttpResponseInterceptor());
    }

    private void setSocketTimeout(int socketTimeout) {
        HttpConnectionParams.setSoTimeout(this.getParams(), socketTimeout);
    }

    private void setConnectionTimeout(int connectionTimeout) {
        HttpConnectionParams.setConnectionTimeout(this.getParams(), connectionTimeout);
    }

    private static ClientConnectionManager defaultConnectionManager() {
        ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager();
        connManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_HOST);
        return connManager;
    }
}
