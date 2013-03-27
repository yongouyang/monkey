package org.monkey.common.transport;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PooledHttpTransport implements HttpTransport {

    private static final int NO_RETRY = 0;


    protected HttpClient httpClient;

    public PooledHttpTransport() {
        this(new PooledHttpClient());
    }

    public PooledHttpTransport(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void addRequestInterceptor(HttpRequestInterceptor requestInterceptor) {
        if (httpClient instanceof DefaultHttpClient) {
            ((DefaultHttpClient) httpClient).addRequestInterceptor(requestInterceptor);
        }
    }

    @Override
    public void addResponseInterceptor(HttpResponseInterceptor responseInterceptor) {
        if (httpClient instanceof DefaultHttpClient) {
            ((DefaultHttpClient) httpClient).addResponseInterceptor(responseInterceptor);
        }
    }

    @Override
    public String doGet(final String uri, Header... headers) throws IOException {
        HttpGet request = createGetRequest(uri, headers);
        return executeRequestWithRetry(uri, request, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws IOException {
                validateResponse(uri, response);
                return HttpResponseUtils.toString(response);
            }
        }, NO_RETRY);
    }

    @Override
    public InputStream doGetLargeResource(final String uri, Header... headers) throws IOException {
        HttpGet request = createGetRequest(uri, headers);
        return executeRequestWithRetry(uri, request, new ResponseHandler<InputStream>() {
            @Override
            public InputStream handleResponse(HttpResponse response) throws IOException {
                validateResponse(uri, response);
                return new BufferedInputStream(HttpResponseUtils.toStream(response));
            }
        }, NO_RETRY);
    }

    @Override
    public String doPost(final String uri, String payload, Header... headers) throws IOException {
        HttpPost request = createPostRequest(uri, payload, headers);
        return executeRequestWithRetry(uri, request, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws IOException {
                validateResponse(uri, response);
                Header location = response.getFirstHeader("Location");
                return (location != null) ? location.getValue() : HttpResponseUtils.toString(response);
            }
        }, NO_RETRY);
    }

    protected void validateResponse(String requestUri, HttpResponse response) throws ServerException {
        StatusLine status = response.getStatusLine();
        if (!isSuccess(status)) {
            throw new ServerException(requestUri, response);
        }
    }

    protected boolean isSuccess(StatusLine status) {
        return status != null && (status.getStatusCode() >= HttpStatus.SC_OK && status.getStatusCode() <= HttpStatus.SC_TEMPORARY_REDIRECT);
    }

    private HttpGet createGetRequest(String uri, Header[] headers) {
        HttpGet request = new HttpGet(uri);
        request.setHeaders(headers);
        return request;
    }

    protected HttpPost createPostRequest(String uri, String payload, Header[] headers) throws IOException {
        HttpPost request = new HttpPost(uri);
        request.setHeaders(headers);
        request.setHeader("Content-Type", "application/json");
        if (StringUtils.isNotBlank(payload)) request.setEntity(new StringEntity(payload));
        return request;
    }

    private <T> T executeRequestWithRetry(String uri, HttpUriRequest request, ResponseHandler<T> responseHandler, int retryCount) throws IOException {
        int executeCount = 0;
        while (true) {
            executeCount++;
            try {
                // todo: we should probably log the request here
                return httpClient.execute(request, responseHandler);
            } catch (IOException e) {
                // log.warn("failed to execute request");
                if (executeCount > retryCount || !isRecoverable(e)) {
                    throw e;
                }
            }
            // log.warn("retrying request");
        }
    }

    private boolean isRecoverable(IOException e) {
        return isConnectionReset(e) || e instanceof SocketTimeoutException;
    }

    private boolean isConnectionReset(IOException e) {
        return e instanceof SocketException && StringUtils.equalsIgnoreCase("Connection reset", e.getMessage());
    }
}
