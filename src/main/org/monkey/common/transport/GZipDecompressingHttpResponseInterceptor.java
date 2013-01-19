package org.monkey.common.transport;

import org.apache.http.*;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class GZipDecompressingHttpResponseInterceptor implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        HttpEntity entity = response.getEntity();
        Header contentEncodingHeader = entity.getContentEncoding();
        if (contentEncodingHeader != null) {
            HeaderElement[] codecs = contentEncodingHeader.getElements();
            for (HeaderElement codec : codecs) {
                if ("gzip".equalsIgnoreCase(codec.getName())) {
                    response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                    return;
                }
            }
        }


    }
}
