package org.monkey.service.persistence.mongodb

import org.jongo.Jongo
import org.jongo.MongoCollection
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class MongoDbCollectionProviderImplTest {

    @Mock Jongo jongo
    @Mock MongoCollection welcomes, dailyPrice

    MongoDbCollectionProvider collectionProvider

    @Before
    public void before() {
        collectionProvider = new MongoDbCollectionProviderImpl(jongo)
    }


    @Test
    public void getWelcomesCollection() {
        when(jongo.getCollection("welcomes")).thenReturn(welcomes)
        assert collectionProvider.welcomesCollection == welcomes
    }

    @Test
    public void getDailyPriceCollection() {
        when(jongo.getCollection("dailyPrice")).thenReturn(dailyPrice)
        assert collectionProvider.dailyPriceCollection == dailyPrice
    }
}
