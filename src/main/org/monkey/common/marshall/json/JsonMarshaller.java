package org.monkey.common.marshall.json;

import org.monkey.common.exception.MarshallException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

public interface JsonMarshaller {

    String marshall(Object pojo) throws MarshallException;

    void marshall(OutputStream outputStream, Object pojo) throws MarshallException;

    <T> T unmarshall(String json, Class<T> pojoClass, Type... typeParameters) throws MarshallException;

    <T> T unmarshall(InputStream jsonStream, Class<T> pojoClass, Type... typeParameters) throws MarshallException;
}
