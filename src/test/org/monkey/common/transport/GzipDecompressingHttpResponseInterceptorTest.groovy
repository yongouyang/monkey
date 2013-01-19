package org.monkey.common.transport

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.entity.GzipDecompressingEntity
import org.apache.http.entity.BasicHttpEntity
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class GZipDecompressingHttpResponseInterceptorTest {

    @Mock HttpResponse response

    @Test
    public void processWrapsEntityWithGZipDecompressingEntityIfContentEncodingIsGZip() {
        def entity = new BasicHttpEntity()
        entity.content = new BufferedInputStream(new ByteArrayInputStream(new byte[1]))
        entity.setContentEncoding("gzip")

        when(response.entity).thenReturn(entity)

        new GZipDecompressingHttpResponseInterceptor().process(response, null)

        Mockito.verify(response).setEntity((HttpEntity) Mockito.argThat(Matchers.is(GzipDecompressingEntity.class)))

    }

}
