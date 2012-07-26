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

package org.restlet.util;

import java.io.IOException;
import java.io.Reader;

import org.restlet.engine.io.IoUtils;
import org.restlet.representation.Representation;

/**
 * Selection listener notifying new content as a string.
 * 
 * @author Jerome Louvel
 */
public abstract class StringListener extends ReaderListener {

    /**
     * Default constructor. Uses a byte buffer of {@link org.restlet.engine.io.IoUtils#BUFFER_SIZE}
     * length.
     * 
     * @param source
     *            The source representation.
     * @throws java.io.IOException
     */
    public StringListener(Representation source) throws IOException {
        super(source);
    }

    /**
     * Constructor. Uses a byte buffer of a given size.
     * 
     * @param source
     *            The source representation.
     * @param bufferSize
     *            The byte buffer to use.
     * @throws java.io.IOException
     */
    public StringListener(Representation source, int bufferSize)
            throws IOException {
        super(source, bufferSize);
    }

    @Override
    protected final void onContent(Reader reader) {
        try {
            int r = reader.read();
            StringBuilder sb = new StringBuilder();

            while (r != -1) {
                sb.append((char) r);
                r = reader.read();
            }

            String s = sb.toString();
            onContent(s);
        } catch (IOException ioe) {
            onError(ioe);
        }
    }

    /**
     * Callback invoked when new content is available.
     * 
     * @param content
     *            The new content.
     */
    protected abstract void onContent(String content);

}
