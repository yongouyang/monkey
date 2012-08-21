package org.monkey.common.transport;

import com.google.common.collect.Maps;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class ServerException extends IOException {

    private int httpResponseCode;
    private String responseBody;
    private Map<String, String> headers = Maps.newHashMap();
    private String requestUri;

    public ServerException(String requestUri, HttpResponse response) {
        super(String.format("Call to server [%s] failed [%s]", requestUri, response.getStatusLine().getStatusCode()));
        this.httpResponseCode = HttpResponseUtils.getResponseCode(response);
        this.responseBody = getResponseBody(response);
        this.headers = HttpResponseUtils.getAllHeaders(response);
        this.requestUri = requestUri;
    }

    private String getResponseBody(HttpResponse response) {
        try {
            return HttpResponseUtils.toString(response);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get the response body from the response", e);
        }
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequestUri() {
        return requestUri;
    }
}
