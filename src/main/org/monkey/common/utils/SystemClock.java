package org.monkey.common.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public class SystemClock implements Clock {

    private long offsetMillis = 0;

    public DateTime getNow() {
        return new DateTime(System.currentTimeMillis() + offsetMillis);
    }

    public LocalDate getToday(DateTimeZone timezone) {
        return getNow().withZone(timezone).toLocalDate();
    }

    public DateTime getTodayStartOfDay(DateTimeZone timezone) {
        return getToday(timezone).toDateTimeAtStartOfDay();
    }

    public void setDateTime(DateTime datetime) {
        offsetMillis = (datetime.toDate()).getTime() - System.currentTimeMillis();
    }

    public void advance(int seconds) {
        setDateTime(getNow().plusSeconds(seconds + 1));
    }
}
