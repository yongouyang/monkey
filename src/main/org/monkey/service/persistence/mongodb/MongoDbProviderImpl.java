package org.monkey.service.persistence.mongodb;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.monkey.common.utils.config.SystemPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoDbProviderImpl implements MongoDbProvider {

    private final Mongo mongo;
    private final SystemPreferences systemPreferences;

    @Autowired
    public MongoDbProviderImpl(SystemPreferences systemPreferences) {
        this(systemPreferences, new Mongo(systemPreferences.getMongoDbAddress()));
    }

    // used for unit test
    MongoDbProviderImpl(SystemPreferences systemPreferences, Mongo mongo) {
        this.systemPreferences = systemPreferences;
        this.mongo = mongo;
    }

    @Override
    public DB getAdminDb() {
        DB admin = mongo.getDB("admin");
        authenticateIfRequired(admin, systemPreferences.getMongoDbAdminUser(), systemPreferences.getMongoDbAdminPassword());
        return admin;
    }

    @Override
    public DB getDefaultDb() {
        DB db = mongo.getDB(systemPreferences.getMongoDbDefaultDbName());
        authenticateIfRequired(db, systemPreferences.getMongoDbWriteUser(), systemPreferences.getMongoDbWritePassword());
        return db;
    }

    private synchronized void authenticateIfRequired(DB db, String user, char[] password) {
        if (!db.isAuthenticated()) {
            db.authenticate(user, password);
        }
    }
}
