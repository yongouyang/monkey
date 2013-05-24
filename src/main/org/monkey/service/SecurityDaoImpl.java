package org.monkey.service;

import org.jongo.MongoCollection;
import org.monkey.sample.model.Security;
import org.monkey.service.persistence.mongodb.MongoDbCollectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityDaoImpl implements SecurityDao {

    private final MongoCollection securityCollection;

    @Autowired
    public SecurityDaoImpl(MongoDbCollectionProvider mongoDbCollectionProvider) {
        this.securityCollection = mongoDbCollectionProvider.getSecurityCollection();
    }

    @Override
    public void saveOrUpdate(Security security) {
        securityCollection.save(security);
    }

    @Override
    public Security findOne(String ricCode) {
        return securityCollection.findOne(String.format("{ricCode:'%s'}", ricCode)).as(Security.class);
    }

    @Override
    public Iterable<Security> findAll() {
        return securityCollection.find().as(Security.class);
    }
}
