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

/**
 * Interface called back when IO buffer draining or filling can actually be
 * done.
 * 
 * @author Jerome Louvel
 */
public interface BufferProcessor {

    /**
     * Indicates if the processing loop can continue.
     * 
     * @param buffer
     *            The IO buffer to drain.
     * @param args
     *            The optional arguments to pass back to the callbacks.
     * @return True if the processing loop can continue.
     */
    public boolean canLoop(Buffer buffer, Object... args);

    /**
     * Indicates if the buffer could be filled again.
     * 
     * @param buffer
     *            The IO buffer to fill.
     * @param args
     *            The optional arguments to pass back to the callbacks.
     * @return True if the buffer could be filled again.
     */
    public boolean couldFill(Buffer buffer, Object... args);

    /**
     * Drains the byte buffer.
     * 
     * @param buffer
     *            The IO buffer to drain.
     * @param maxDrained
     *            The maximum number of bytes drained by this call.
     * @param args
     *            The optional arguments to pass back to the callbacks.
     * @return The number of bytes drained.
     * @throws java.io.IOException
     */
    public int onDrain(Buffer buffer, int maxDrained, Object... args)
            throws IOException;

    /**
     * Fills the byte buffer.
     * 
     * @param buffer
     *            The IO buffer to drain.
     * @param args
     *            The optional arguments to pass back to the callbacks.
     * @return The number of bytes filled.
     * @throws java.io.IOException
     */
    public int onFill(Buffer buffer, Object... args) throws IOException;

    /**
     * Called back when a fill operation returns with an EOF status.
     */
    public void onFillEof();

    /**
     * Called back after a complete processing pass.
     * 
     * @param drained
     *            The number of bytes drained or -1 if the filling source has
     *            ended.
     */
    public void onProcessed(int drained) throws IOException;

}
