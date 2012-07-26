package org.monkey.server.servlet.restlet;

import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;

import java.util.List;

public class RestletUtils {

    public static void replaceConverter(Class<? extends ConverterHelper> converterClass, ConverterHelper newConverter) {
        ConverterHelper oldConverter = null;

        final List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters();
        for (int i = 0; i < converters.size(); i++) {
            final ConverterHelper converterHelper = converters.get(i);
            if (converters.getClass().equals(converterClass)) {
                oldConverter = converters.set(i, newConverter);
                break;
            }
        }

        if (oldConverter == null) {
            System.out.println(String.format("Added %s to Restlet Engine", newConverter.getClass()));
        } else {
            System.out.println(String.format("Replaced %S with %s in Restlet Engine", oldConverter.getClass(), newConverter.getClass()));
        }

    }
}
