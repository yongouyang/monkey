package org.monkey.common.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public interface Clock {

    DateTime getNow();

    LocalDate getToday(DateTimeZone timezone);

    DateTime getTodayStartOfDay(DateTimeZone timezone);
}
