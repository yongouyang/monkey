package org.monkey.common.marshall.json;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

public class LocalDateMapper extends JodaDatesMapperSupport<LocalDate> {

    public LocalDateMapper() {
        super(ISODateTimeFormat.date(), ISODateTimeFormat.dateParser());
    }

    @Override
    String toString(LocalDate localDate) {
        return formatter.print(localDate);
    }

    @Override
    LocalDate toJavaType(String value) {
        return parser.parseLocalDate(value);
    }
}
