package org.monkey.common.transport;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class AllowGZipResponseRequestInterceptor implements HttpRequestInterceptor{
    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        httpRequest.addHeader("Accept-Encoding", "gzip");
    }
}
