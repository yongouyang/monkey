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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Trace byte channel that sends a copy of all data on the trace output stream.
 * It is important to implement {@link org.restlet.engine.io.SelectionChannel} as some framework
 * classes rely on this down the processing chain.
 * 
 * @author Jerome Louvel
 */
public class ReadableTraceChannel extends
        TraceChannel<ReadableSelectionChannel> implements
        ReadableSelectionChannel {

    /**
     * Constructor.
     * 
     * @param wrappedChannel
     *            The wrapped channel.
     */
    public ReadableTraceChannel(ReadableSelectionChannel wrappedChannel) {
        super(wrappedChannel);
    }

    /**
     * Constructor.
     * 
     * @param wrappedChannel
     *            The wrapped channel.
     * @param traceStream
     *            The trace stream.
     */
    public ReadableTraceChannel(ReadableSelectionChannel wrappedChannel,
            OutputStream traceStream) {
        super(wrappedChannel, traceStream);
    }

    /**
     * Reads the available byte from the wrapped channel to the destination
     * buffer while writing them to the console.
     * 
     * @param dst
     *            The destination buffer.
     * @return The number of bytes read.
     */
    public int read(ByteBuffer dst) throws IOException {
        int off = dst.arrayOffset() + dst.position();
        int result = getWrappedChannel().read(dst);

        if (result > 0) {
            System.out.write(dst.array(), off, result);
        }

        return result;
    }

}
