package org.monkey.common.marshall.json;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeMapper extends JodaDatesMapperSupport<DateTime> {
    public DateTimeMapper() {
        super(ISODateTimeFormat.dateTimeNoMillis(), ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }

    @Override
    String toString(DateTime dateTime) {
        return formatter.print(dateTime);
    }

    @Override
    DateTime toJavaType(String value) {
        return parser.parseDateTime(value);
    }
}
