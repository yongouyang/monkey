package org.monkey.common.marshall.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.monkey.common.exception.MarshallException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;

public class JacksonJsonMarshaller implements JsonMarshaller {

    private final ObjectMapper mapper;
    final LocalDateMapper localDateMapper = new LocalDateMapper();
    final DateTimeMapper dateTimeMapper = new DateTimeMapper();

    public JacksonJsonMarshaller() {
        this.mapper = new ObjectMapper();

        mapper.registerModule(buildCustomSerializerFactory());
        mapper.registerModule(buildCustomDeserializerFactory());

        // do we need to have a custom annotation introspector?

        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
        mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
        mapper.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
        mapper.configure(MapperFeature.AUTO_DETECT_CREATORS, true);
        mapper.configure(MapperFeature.USE_ANNOTATIONS, true);

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

        VisibilityChecker.Std visibilityChecker = VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY);
        mapper.setVisibilityChecker(visibilityChecker);

    }

    public JacksonJsonMarshaller withPrettyPrint() {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return this;
    }

    public JacksonJsonMarshaller allowingComments() {
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        return this;
    }

    @Override
    public String marshall(Object pojo) throws MarshallException {
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, pojo);
            return writer.toString();
        } catch (IOException e) {
            throw new MarshallException("Failed to marshall to json", e);
        }
    }

    @Override
    public void marshall(OutputStream outputStream, Object pojo) throws MarshallException {
        try {
            mapper.writeValue(outputStream, pojo);
        } catch (IOException e) {
            throw new MarshallException("Failed to marshall to stream", e);
        }
    }

    @Override
    public <T> T unmarshall(String json, Class<T> pojoClass, Type... typeParameters) throws MarshallException {
        if (StringUtils.isBlank(json)) return null;

        try {
            json = json.trim();
            T obj = null;
            if (typeParameters == null || typeParameters.length == 0) {
                obj = mapper.readValue(json, pojoClass);
            } else {
                obj = (T) mapper.readValue(json, mapper.getTypeFactory().constructParametricType(pojoClass, classFromTypes(typeParameters)));
            }

            if (obj == null) {
                throw new MarshallException("Failed to unmarshall json to class " + pojoClass.getName() + " becauuse of null value");
            }
            return obj;
        } catch (IOException e) {
            throw new MarshallException("Failed to unmarshall json to class " + pojoClass.getName() + " - " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T unmarshall(InputStream jsonStream, Class<T> pojoClass, Type... typeParameters) throws MarshallException {
        try {
            T obj = null;
            if (typeParameters == null || typeParameters.length == 0) {
                obj = mapper.readValue(jsonStream, pojoClass);
            } else {
                obj = (T) mapper.readValue(jsonStream, mapper.getTypeFactory().constructParametricType(pojoClass, classFromTypes(typeParameters)));
            }

            if (obj == null) {
                throw new MarshallException("Failed to unmarshall json to class " + pojoClass.getName() + " becauuse of null value");
            }
            return obj;
        } catch (IOException e) {
            throw new MarshallException("Failed to unmarshall json to class " + pojoClass.getName() + " - " + e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }


    private JavaType[] classFromTypes(Type... types) {
        JavaType[] classes = new JavaType[types.length];
        int i = 0;
        for (Type t : types) {
            classes[i] = mapper.constructType(t);
            i++;
        }
        return classes;
    }


    private Module buildCustomSerializerFactory() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(localDateMapper.toString(localDate));
            }
        });

        module.addSerializer(DateTime.class, new JsonSerializer<DateTime>() {
            @Override
            public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
                jsonGenerator.writeString(dateTimeMapper.toString(dateTime));
            }
        });

        return module;
    }

    private Module buildCustomDeserializerFactory() {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return localDateMapper.toJavaType(jp.getText());
            }
        });

        module.addDeserializer(DateTime.class, new JsonDeserializer<DateTime>() {
            @Override
            public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return dateTimeMapper.toJavaType(jp.getText());
            }
        });

        return module;
    }
}
