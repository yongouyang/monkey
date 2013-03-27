package org.monkey.web.object

import org.apache.http.Header
import org.apache.http.message.BasicHttpResponse

import java.lang.reflect.Type

class ApiSupport<T extends ApiSupport<T>> {

    protected TestOnlyHttpTransport httpTransport = new TestOnlyHttpTransport()

    public T expectError(int statusCode) {
        httpTransport.expectedStatusCode = statusCode
        return this as T
    }

    protected <E> Maybe<E> get(String uri, Class<E> pojo, Type[] typeParameters = []) {
        String response = httpTransport.doGet(uri)
        return new Maybe(response, pojo, typeParameters)
    }

    protected BasicHttpResponse post(String uri, String payload, Header[] headers = []) {
        return httpTransport.post(uri, payload, headers)
    }

}
