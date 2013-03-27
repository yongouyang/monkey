package org.monkey.service.persistence.mongodb;

import com.mongodb.DB;

public interface MongoDbProvider {
    DB getAdminDb();

    DB getDefaultDb();
}
