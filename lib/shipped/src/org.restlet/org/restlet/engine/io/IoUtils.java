/**
 * Copyright 2005-2011 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.engine.io;

import java.io.BufferedReader;

/**
 * IO manipulation utilities.
 * 
 * @author Thierry Boileau
 */
public class IoUtils {

    /**
     * The size to use when instantiating buffered items such as instances of
     * the {@link java.io.BufferedReader} class. It looks for the System property
     * "org.restlet.engine.io.bufferSize" and if not defined, uses the "8192"
     * default value.
     */
    public static final int BUFFER_SIZE = getProperty(
            "org.restlet.engine.io.bufferSize", 8192);

    /**
     * The number of milliseconds after which IO operation will time out. It
     * looks for the System property "org.restlet.engine.io.timeoutMs" and if
     * not defined, uses the "60000" default value.
     */
    public final static int TIMEOUT_MS = getProperty(
            "org.restlet.engine.io.timeoutMs", 60000);

    private static int getProperty(String name, int defaultValue) {
        int result = defaultValue;

        try {
            result = Integer.parseInt(System.getProperty(name));
        } catch (NumberFormatException nfe) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * Private constructor to ensure that the class acts as a true utility class
     * i.e. it isn't instantiable and extensible.
     */
    private IoUtils() {
    }
}
