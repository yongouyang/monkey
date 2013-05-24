package org.monkey.service.persistence.mongodb;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.monkey.common.marshall.json.JacksonJsonMarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoDbCollectionProviderImpl implements MongoDbCollectionProvider {

    private final Jongo jongo;

    @Autowired
    public MongoDbCollectionProviderImpl(MongoDbProvider mongoDbProvider) {
        // i wanna re-use the the custom deser and ser factories used by jackson here
        this(new Jongo(mongoDbProvider.getDefaultDb(), new JacksonMapper.Builder()
                .registerModule(JacksonJsonMarshaller.buildCustomDeserializerFactory())
                .registerModule(JacksonJsonMarshaller.buildCustomSerializerFactory())
                .enable(MapperFeature.AUTO_DETECT_FIELDS)
                .disable(MapperFeature.AUTO_DETECT_GETTERS)
                .disable(MapperFeature.AUTO_DETECT_IS_GETTERS)
                .enable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
                .enable(MapperFeature.USE_ANNOTATIONS)
                .enable(MapperFeature.AUTO_DETECT_CREATORS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .build()));
    }

    // used for unit test
    MongoDbCollectionProviderImpl(Jongo jongo) {
        this.jongo = jongo;
    }

    @Override
    public MongoCollection getWelcomesCollection() {
        return jongo.getCollection("welcomes");
    }

    @Override
    public MongoCollection getDailyPriceCollection() {
        return jongo.getCollection("dailyPrice");
    }

    @Override
    public MongoCollection getSecurityCollection() {
        return jongo.getCollection("security");
    }
}
