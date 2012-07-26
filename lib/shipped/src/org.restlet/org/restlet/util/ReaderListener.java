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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.restlet.data.CharacterSet;
import org.restlet.engine.io.IoUtils;
import org.restlet.representation.Representation;

/**
 * Selection listener notifying new content as a {@link java.io.Reader}. It relies on
 * the representation's character set for proper character decoding.
 * 
 * @author Jerome Louvel
 */
public abstract class ReaderListener extends InputListener {

    /** The character set of the associated representation. */
    private final CharacterSet characterSet;

    /**
     * Default constructor. Uses a byte buffer of {@link org.restlet.engine.io.IoUtils#BUFFER_SIZE}
     * length.
     * 
     * @param source
     *            The source representation.
     * @throws java.io.IOException
     */
    public ReaderListener(Representation source) throws IOException {
        this(source, IoUtils.BUFFER_SIZE);
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
    public ReaderListener(Representation source, int bufferSize)
            throws IOException {
        super(source, bufferSize);
        this.characterSet = source.getCharacterSet();
    }

    @Override
    protected final void onContent(InputStream inputStream) {
        InputStreamReader isr = new InputStreamReader(inputStream,
                this.characterSet.toCharset());
        onContent(isr);
    }

    /**
     * Callback invoked when new content is available.
     * 
     * @param reader
     *            The reader allowing to retrieve the new content.
     */
    protected abstract void onContent(Reader reader);

}
