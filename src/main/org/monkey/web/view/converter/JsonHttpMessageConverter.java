package org.monkey.web.view.converter;

import org.monkey.common.marshall.json.JacksonJsonMarshaller;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.List;

public class JsonHttpMessageConverter implements HttpMessageConverter<Object> {

    private HttpMessageConverter<Object> delegate;

    public JsonHttpMessageConverter(MappingJackson2HttpMessageConverter delegate) {
        this.delegate = delegate;
        delegate.setObjectMapper(new JacksonJsonMarshaller().getMapper());
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return delegate.canRead(clazz, mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return delegate.canWrite(clazz, mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return delegate.getSupportedMediaTypes();
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return delegate.read(clazz, inputMessage);
    }

    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        delegate.write(o, contentType, outputMessage);
    }
}
