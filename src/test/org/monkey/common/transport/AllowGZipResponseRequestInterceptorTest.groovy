package org.monkey.common.transport

import org.apache.http.HttpRequest
import org.apache.http.client.methods.HttpGet
import org.junit.Before
import org.junit.Test

class AllowGZipResponseRequestInterceptorTest {

    AllowGZipResponseRequestInterceptor interceptor
    HttpRequest request

    @Before
    public void before() {
        request = new HttpGet()
        interceptor = new AllowGZipResponseRequestInterceptor()
    }

    @Test
    public void processAddsHeaderToTheRequest() {
        assert request.allHeaders.length == 0

        interceptor.process(request, null)

        assert request.getHeaders("Accept-Encoding").length == 1
        assert request.getHeaders("Accept-Encoding")[0].value == "gzip"
    }

    @Test
    public void processAppendHeaderValueToTheRequestIfHeaderExists() {
        request.addHeader("accePt-EncOding", "deflate")

        interceptor.process(request, null)

        assert request.getHeaders("Accept-Encoding").length == 2
        assert request.getHeaders("Accept-Encoding")[1].value == "gzip"
    }
}
