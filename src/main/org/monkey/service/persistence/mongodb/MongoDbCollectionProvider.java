package org.monkey.service.persistence.mongodb;

import org.jongo.MongoCollection;

public interface MongoDbCollectionProvider {
    MongoCollection getWelcomesCollection();

    MongoCollection getDailyPriceCollection();

    MongoCollection getSecurityCollection();
}
