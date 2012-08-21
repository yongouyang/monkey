package org.monkey.common.transport

import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.when
import org.apache.http.HttpEntity
import org.apache.http.message.BasicHttpResponse
import org.apache.http.entity.BasicHttpEntity

@RunWith(MockitoJUnitRunner.class)
class ServerExceptionTest {

    ServerException exception
    String requestUri, responseBody
    int httpResponseCode
    Map headers
    InputStream inputStream


    @Mock HttpResponse response
    @Mock StatusLine statusLine
    @Mock Header header
    @Mock HttpEntity entity


    @Before
    public void before() {
        requestUri = "http://localhost:1234/ping"
        httpResponseCode = 200
        headers = [headerName: "headerValue"]
        responseBody = "some response body"
        inputStream = new ByteArrayInputStream(responseBody.bytes);
    }

    @Test
    public void canCreateANewServerException() {
        when(response.statusLine).thenReturn(statusLine)
        when(statusLine.statusCode).thenReturn(httpResponseCode)
        when(response.allHeaders).thenReturn([header] as Header[])
        when(header.name).thenReturn("headerName")
        when(header.value).thenReturn("headerValue")
        when(response.entity).thenReturn(entity)
        when(entity.content).thenReturn(inputStream)

        exception = new ServerException(requestUri, response)

        assert exception.requestUri == requestUri
        assert exception.responseBody == responseBody
        assert exception.headers == headers
        assert exception.httpResponseCode == httpResponseCode
    }
}
