package org.monkey.common.utils.config;

import com.mongodb.ServerAddress;

public interface SystemPreferences {
    String get(String key);
    Integer getInteger(String key, Integer defaultValue);

    ServerAddress getMongoDbAddress();
    String getMongoDbDefaultDbName();

    String getMongoDbWriteUser();

    char[] getMongoDbWritePassword();

    String getMongoDbAdminUser();

    char[] getMongoDbAdminPassword();
}
