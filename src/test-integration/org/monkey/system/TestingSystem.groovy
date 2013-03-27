package org.monkey.system

import org.monkey.server.JettyServer
import org.monkey.server.RunLocalServer
import org.monkey.service.persistence.mongodb.MongoDbCollectionProviderImpl
import org.monkey.service.persistence.mongodb.MongoDbProviderImpl

class TestingSystem {

    private static boolean isRunning = false
    private static JettyServer coreApplication;

    public synchronized static void init() {
        if (!isRunning) {
            coreApplication = RunLocalServer.start()
            refreshMongoDb()
            isRunning = true
        }
    }

    private static void refreshMongoDb() {
        def collectionProvider = (MongoDbCollectionProviderImpl) coreApplication.spring.getBean("mongoDbCollectionProviderImpl")
        // drop collections from the default db
        println "*** Refreshing MongoDb ***"
        collectionProvider.welcomesCollection.drop()
        collectionProvider.dailyPriceCollection.drop()

        // rebuild indices
    }


}
