package org.monkey.service.persistence.mongodb

import com.mongodb.DB
import com.mongodb.Mongo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.monkey.common.utils.config.SystemPreferences

import static org.mockito.Mockito.verify
import static org.mockito.Mockito.verifyZeroInteractions
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class MongoDbProviderImplTest {

    @Mock Mongo mongo
    @Mock SystemPreferences systemPreferences
    @Mock DB adminDb, defaultDb
    String adminUser = "adminUser", writeUser = "writeUser", defaultDbName="some-default-db-name"
    char[] adminPassword = "adminPass", writePassword = "writePass"

    MongoDbProviderImpl mongoDbProvider

    @Before
    public void before() {
        mongoDbProvider = new MongoDbProviderImpl(systemPreferences, mongo)

        when(systemPreferences.mongoDbAdminUser).thenReturn(adminUser)
        when(systemPreferences.mongoDbAdminPassword).thenReturn(adminPassword)
        when(systemPreferences.mongoDbWriteUser).thenReturn(writeUser)
        when(systemPreferences.mongoDbWritePassword).thenReturn(writePassword)
        when(systemPreferences.mongoDbDefaultDbName).thenReturn(defaultDbName)
    }

    @Test
    public void getAdminDbWhenItHasAuthenticatedBefore() {
        when(mongo.getDB("admin")).thenReturn(adminDb)
        when(adminDb.isAuthenticated()).thenReturn(true)

        assert mongoDbProvider.adminDb == adminDb
        verifyZeroInteractions(defaultDb)
    }

    @Test
    public void getDefaultDbWhenItHasNotAuthenticatedBefore() {
        when(mongo.getDB(defaultDbName)).thenReturn(defaultDb)
        when(defaultDb.isAuthenticated()).thenReturn(false)

        assert mongoDbProvider.defaultDb == defaultDb

        verify(defaultDb).authenticate(writeUser, writePassword)
        verifyZeroInteractions(adminDb)
    }
}
