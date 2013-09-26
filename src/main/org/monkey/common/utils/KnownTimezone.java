package org.monkey.common.utils;

import org.joda.time.DateTimeZone;

public enum KnownTimezone {

    UTC(DateTimeZone.UTC),
    NewZealand_Auckland(DateTimeZone.forID("Pacific/Auckland")),
    HongKong(DateTimeZone.forID("Asia/Hong_Kong")),
    Singapore(DateTimeZone.forID("Asia/Singapore")),
    Sydney(DateTimeZone.forID("Australia/Sydney")),
    Japan(DateTimeZone.forID("Asia/Tokyo")),
    Korea(DateTimeZone.forID("Asia/Seoul")),
    Europe_London(DateTimeZone.forID("Europe/London")),
    America_NewYork(DateTimeZone.forID("America/New_York")),
    America_LosAngeles(DateTimeZone.forID("America/Los_Angeles"));

    private DateTimeZone jodaTimezone;

    KnownTimezone(DateTimeZone timezone) {
        this.jodaTimezone = timezone;
    }

    public DateTimeZone getJodaTimezone() {
        return this.jodaTimezone;
    }

    public static KnownTimezone DEFAULT = HongKong;
}
