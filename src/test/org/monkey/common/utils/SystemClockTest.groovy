package org.monkey.common.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.junit.Test

import static org.junit.Assert.assertEquals


class SystemClockTest {

    private final SystemClock clock = new SystemClock();

    @Test
    public void testWillConvertCurrentUTCTimeToALocalDateForAGivenTimezone() {
        clock.setDateTime(new DateTime(1973, 7, 4, 11, 24, 23, 12, DateTimeZone.UTC));

        // Same day
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.UTC.getJodaTimezone()));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.NewZealand_Auckland.getJodaTimezone()));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.HongKong.getJodaTimezone()));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.America_NewYork.getJodaTimezone()));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.America_LosAngeles.getJodaTimezone()));

        // early in the UTC day
        clock.setDateTime(new DateTime(1973, 7, 4, 1, 24, 23, 12, DateTimeZone.UTC));
        assertLocalDate(1973, 7, 4, clock.getToday(DateTimeZone.UTC));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.NewZealand_Auckland.getJodaTimezone()));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.HongKong.getJodaTimezone()));
        assertLocalDate(1973, 7, 3, clock.getToday(KnownTimezone.America_NewYork.getJodaTimezone()));
        assertLocalDate(1973, 7, 3, clock.getToday(KnownTimezone.America_LosAngeles.getJodaTimezone()));

        // late in the UTC day
        clock.setDateTime(new DateTime(1973, 7, 4, 21, 24, 23, 12, DateTimeZone.UTC));
        assertLocalDate(1973, 7, 4, clock.getToday(DateTimeZone.UTC));
        assertLocalDate(1973, 7, 5, clock.getToday(KnownTimezone.NewZealand_Auckland.getJodaTimezone()));
        assertLocalDate(1973, 7, 5, clock.getToday(KnownTimezone.HongKong.getJodaTimezone()));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.America_NewYork.getJodaTimezone()));
        assertLocalDate(1973, 7, 4, clock.getToday(KnownTimezone.America_LosAngeles.getJodaTimezone()));
    }

    private void assertLocalDate(int year, int monthOfYear, int dayOfMonth, LocalDate utcDate) {
        assertEquals(new LocalDate(year, monthOfYear, dayOfMonth), utcDate);
    }
}
