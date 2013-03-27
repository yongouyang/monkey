package org.monkey.service;

import org.monkey.sample.model.SampleDailyPrice;

public interface DailyPriceDao {
    void saveOrUpdate(SampleDailyPrice dailyPrice);

    SampleDailyPrice find(String query);
}
