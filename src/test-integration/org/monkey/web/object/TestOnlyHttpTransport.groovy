package org.monkey.web.object

import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicHttpResponse
import org.monkey.common.transport.PooledHttpTransport
import org.monkey.common.transport.ServerException


class TestOnlyHttpTransport extends PooledHttpTransport {

    def int expectedStatusCode = -1

    public TestOnlyHttpTransport() {
        super()
    }

    @Override
    protected void validateResponse(String requestUri, HttpResponse response) throws ServerException {
        if (expectedStatusCode != -1) {
            assert response.statusLine.statusCode == expectedStatusCode, "We expect http status is ${expectedStatusCode}, but it is ${response.statusLine.statusCode} when requesting for ${requestUri}"
        }
    }

    public BasicHttpResponse post(String uri, String payload, Header[] headers = []) {
        HttpPost request = createPostRequest(uri, payload, headers)
        return (BasicHttpResponse) httpClient.execute(request)
    }
}
