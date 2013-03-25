package org.monkey.common.transport

import org.junit.Before
import org.junit.Test

class HttpTransportSmokeTest {

    HttpTransport transport

    @Before
    public void before() {
        transport = new PooledHttpTransport()
    }

    @Test
    void doGet() {
        def response = transport.doGet("http://www.google.com")
        assert response.length() > 0
    }

    @Test
    void doGetLargeResource() {
        def resource = transport.doGetLargeResource("http://www.apple.com")

        assert resource.read() > 0
    }
}
