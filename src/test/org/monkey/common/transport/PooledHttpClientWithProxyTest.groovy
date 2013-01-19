package org.monkey.common.transport

import org.apache.http.HttpHost
import org.apache.http.conn.params.ConnRouteParams
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.monkey.common.utils.config.SystemPreferences

import static org.mockito.Mockito.when
import static org.mockito.MockitoAnnotations.initMocks

@RunWith(MockitoJUnitRunner.class)
class PooledHttpClientWithProxyTest {

    @Mock SystemPreferences systemPreferences

    PooledHttpClientWithProxy httpClientWithProxy
    int proxyPort = 1234

    @Before
    public void before() {
        initMocks(this)

        when(systemPreferences.getInteger("HTTP.PROXY.LOCAL.PORT", 3128)).thenReturn(proxyPort)

        httpClientWithProxy = PooledHttpClientWithProxy.newHttpClientWithLocalProxy(systemPreferences)
    }

    @Test
    public void constructorSetsProxyProperly() {
        assert httpClientWithProxy.getParams().getParameter(ConnRouteParams.DEFAULT_PROXY) == new HttpHost("127.0.0.1", proxyPort, "http")
    }
}
