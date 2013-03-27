package org.monkey.service

import org.jongo.MongoCollection
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.monkey.sample.model.SampleDailyPrice
import org.monkey.service.persistence.mongodb.MongoDbCollectionProvider

import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class DailyPriceDaoImplTest {

    @Mock MongoDbCollectionProvider collectionProvider
    @Mock MongoCollection dailyPriceCollection
    @Mock SampleDailyPrice dailyPrice

    DailyPriceDaoImpl dao

    @Before
    public void before() {
        when(collectionProvider.dailyPriceCollection).thenReturn(dailyPriceCollection)

        dao = new DailyPriceDaoImpl(collectionProvider)
    }

    @Test
    public void canSaveOrUpdateADailyPrice() {
        dao.saveOrUpdate(dailyPrice)

        verify(dailyPriceCollection).save(dailyPrice)
    }

    // cannot mock a final class FindOne, thus not unit testing the find method, but will test it on integration level anyway
}
