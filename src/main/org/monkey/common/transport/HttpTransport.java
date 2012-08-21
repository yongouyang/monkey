package org.monkey.common.transport;

import org.apache.http.Header;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

import java.io.IOException;
import java.io.InputStream;

public interface HttpTransport {
    String doGet(String uri, Header... headers) throws IOException;

    InputStream doGetLargeResource(String uri, Header... headers) throws IOException;

    String doPost(String uri, String payload, Header... headers) throws IOException;

    void addRequestInterceptor(HttpRequestInterceptor requestInterceptor);

    void addResponseInterceptor(HttpResponseInterceptor responseInterceptor);
}
