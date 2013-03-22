package org.monkey.web.object;

import org.monkey.common.exception.MarshallException;
import org.monkey.common.marshall.json.JacksonJsonMarshaller;
import org.monkey.common.marshall.json.JsonMarshaller;

import java.lang.reflect.Type;
import java.util.Map;

public class Maybe<T> {

    private String response;
    private Class<T> pojo;
    private Type[] typeParameters;
    private JsonMarshaller marshaller = new JacksonJsonMarshaller();

    public Maybe(String response, Class<T> pojo, Type[] typeParameters) {
        this.response = response;
        this.pojo = pojo;
        this.typeParameters = typeParameters;
    }

    public T succeed() throws MarshallException {
        if ("String".equalsIgnoreCase(pojo.getSimpleName())) {
            return (T) response;
        }
        return marshaller.unmarshall(response, pojo, typeParameters);
    }

    public Map fail() throws MarshallException {
        return marshaller.unmarshall(response, Map.class, typeParameters);
    }

}
