package org.monkey.web.object

import org.apache.http.HttpResponse
import org.monkey.common.marshall.json.JacksonJsonMarshaller
import org.monkey.common.marshall.json.JsonMarshaller
import org.monkey.common.transport.HttpTransport
import org.monkey.common.transport.PooledHttpTransport
import org.monkey.common.transport.ServerException

import java.lang.reflect.Type


class ApiSupport<T extends ApiSupport<T>> {

    protected int expectedStatusCode = -1

    protected HttpTransport httpTransport = new PooledHttpTransport() {
        @Override
        protected void validateResponse(String requestUri, HttpResponse response) throws ServerException {
            if (expectedStatusCode != -1) {
                assert response.statusLine.statusCode == expectedStatusCode, "We expect http status is ${expectedStatusCode}, but it is ${response.statusLine.statusCode} when requesting for ${requestUri}"
            }


        }
    }

    protected JsonMarshaller marshaller = new JacksonJsonMarshaller()

    public T expectError(int statusCode) {
        this.expectedStatusCode = statusCode
        return this as T
    }

    protected <E> Maybe<E> get(String uri, Class<E> pojo, Type... typeParameters) {
        String response = httpTransport.doGet(uri)
        return new Maybe(response, pojo, typeParameters)
    }

}
