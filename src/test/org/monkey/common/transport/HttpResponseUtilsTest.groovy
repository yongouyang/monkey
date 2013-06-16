package org.monkey.common.transport

import org.apache.http.HttpResponse
import org.apache.http.entity.BasicHttpEntity
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.monkey.common.utils.GZipUtils

import java.nio.charset.Charset

import static org.mockito.Mockito.when
import org.apache.http.Header
import org.apache.http.message.BasicHeader
import org.apache.http.HttpEntity
import java.util.zip.GZIPInputStream

@RunWith(MockitoJUnitRunner.class)
class HttpResponseUtilsTest {

    @Mock HttpResponse response
    @Mock HttpEntity entity

    @Test
    public void toStringReturnsEmptyWhenEntityIsNull() {
        assert HttpResponseUtils.toString(response) == ""
    }

    @Test
    public void toStreamReturnsNullWhenEntityIsNull() {
        assert HttpResponseUtils.toStream(response) == null
    }

    @Test
    public void toStringWhenContentEncodingIsNotGZip() {
        String content = "some content"

        def entity = new BasicHttpEntity()
        entity.content = new BufferedInputStream(new ByteArrayInputStream(content.bytes))

        when(response.entity).thenReturn(entity)

        assert HttpResponseUtils.toString(response) == content
    }

    @Test
    public void toStringWhenContentEncodingIsGZip() {
        def charset = Charset.forName("utf-8")
        def originalContent = "some content"
        def gzipContent = GZipUtils.compress(originalContent, charset)

        def entity = new BasicHttpEntity()
        entity.content = new BufferedInputStream(new ByteArrayInputStream(gzipContent))
        entity.setContentEncoding("gzip")
        entity.setContentType("charset=${charset.displayName()}")

        when(response.entity).thenReturn(entity)

        assert HttpResponseUtils.toString(response) == originalContent
    }

    @Test
    public void toStreamWhenContentEncodingIsNotGZip() {
        String content = "some content"

        def entity = new BasicHttpEntity()
        def expected = new BufferedInputStream(new ByteArrayInputStream(content.bytes))
        entity.content = expected

        when(response.entity).thenReturn(entity)

        assert HttpResponseUtils.toStream(response) == expected
    }

    @Test
    public void toStreamWhenContentEncodingIsGZip() {
        def charset = Charset.forName("utf-8")
        def originalContent = "some content"
        def gzipContent = GZipUtils.compress(originalContent, charset)

        def entity = new BasicHttpEntity()
        entity.content = new BufferedInputStream(new ByteArrayInputStream(gzipContent))
        entity.setContentEncoding("gzip")
        entity.setContentType("charset=${charset.displayName()}")

        when(response.entity).thenReturn(entity)

        def wrapped = HttpResponseUtils.toStream(response)
        assert wrapped instanceof GZIPInputStream
        assert wrapped.readLines() == [originalContent]
    }

    @Test
    public void getAllHeaders() {
        when(response.allHeaders).thenReturn([new BasicHeader("header1", "value1")] as Header[])

        assert HttpResponseUtils.getAllHeaders(response) == [header1: "value1"]
    }
}
