package org.monkey.common.transport

import org.apache.http.client.params.ClientPNames
import org.apache.http.cookie.params.CookieSpecPNames
import org.apache.http.params.CoreConnectionPNames
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test


class PooledHttpClientTest {

    PooledHttpClient httpClient

    @Before
    public void before() {
        httpClient = new PooledHttpClient()

        // inject a has method at runtime to List class
        List.metaClass.has = { Matcher matcher ->
            def found = delegate.find {
                matcher.matches(it)
            }
            return found != null
        }
    }

    @Test
    public void httpClientSaysInTheRequestThatItAllowsGZipResponse() {
        def interceptors = []
        (0..httpClient.getRequestInterceptorCount() - 1).each { int index ->
            interceptors << httpClient.getRequestInterceptor(index)
        }

        assert interceptors.has(Matchers.instanceOf(AllowGZipResponseRequestInterceptor))
    }

    @Test
    public void httpClientCanHandleGZipResponse() {
        def interceptors = []
        (0..httpClient.getResponseInterceptorCount() - 1).each { int index ->
            interceptors << httpClient.getResponseInterceptor(index)
        }

        assert interceptors.has(Matchers.instanceOf(GZipDecompressingHttpResponseInterceptor))
    }

    @Test
    public void paramsAreSetProperly() {
        def params = httpClient.getParams()
        assert params.getParameter(ClientPNames.COOKIE_POLICY) == "best-match"
        assert params.getParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER)
        assert params.getParameter(CoreConnectionPNames.SO_TIMEOUT) == 200000
        assert params.getParameter(CoreConnectionPNames.TCP_NODELAY)
        assert params.getParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE) == 8192
        assert params.getParameter(CoreConnectionPNames.CONNECTION_TIMEOUT) == 30000
    }
}
