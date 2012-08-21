package org.monkey.common.transport

import org.junit.Before
import org.junit.Test
import org.junit.Ignore


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

    @Ignore("need to investigate why it fails")
    @Test
    void doGetLargeResource() {
        def resource = transport.doGetLargeResource("http://www.google.com.hk/images/srpr/nav_logo37.png")

        assert resource.read() > 0
    }
}
