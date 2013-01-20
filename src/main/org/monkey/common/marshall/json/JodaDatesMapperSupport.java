package org.monkey.common.marshall.json;

import org.joda.time.format.DateTimeFormatter;

public abstract class JodaDatesMapperSupport<T> {

    protected DateTimeFormatter formatter;
    protected DateTimeFormatter parser;

    protected JodaDatesMapperSupport(DateTimeFormatter formatter, DateTimeFormatter parser) {
        this.formatter = formatter;
        this.parser = parser;
    }

    abstract String toString(T t);

    abstract T toJavaType(String value);
}
