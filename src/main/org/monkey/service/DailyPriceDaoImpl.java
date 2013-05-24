package org.monkey.service;

import org.jongo.MongoCollection;
import org.monkey.sample.model.SampleDailyPrice;
import org.monkey.service.persistence.mongodb.MongoDbCollectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyPriceDaoImpl implements DailyPriceDao {

    private final MongoCollection dailyPriceCollection;

    @Autowired
    public DailyPriceDaoImpl(MongoDbCollectionProvider collectionProvider) {
        this.dailyPriceCollection = collectionProvider.getDailyPriceCollection();
    }

    @Override
    public void saveOrUpdate(SampleDailyPrice dailyPrice) {
        dailyPriceCollection.save(dailyPrice);
    }

    @Override
    public SampleDailyPrice findOne(String query) {
        return dailyPriceCollection.findOne(query).as(SampleDailyPrice.class);
    }
}
