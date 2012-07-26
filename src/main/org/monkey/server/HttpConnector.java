package org.monkey.server;

import org.mortbay.jetty.nio.SelectChannelConnector;

public class HttpConnector extends SelectChannelConnector {

    public HttpConnector(int port, int sslPort) {
        setPort(port);
        setMaxIdleTime(30000);
        setAcceptors(4);
        setStatsOn(false);
        if (sslPort > 0) setConfidentialPort(sslPort);
    }
}
