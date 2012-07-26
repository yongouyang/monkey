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

package org.restlet.engine.connector;

import java.io.IOException;
import java.util.Iterator;

import org.restlet.Response;
import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * HTTP server helper based on NIO blocking sockets.
 * 
 * @author Jerome Louvel
 */
public class HttpServerHelper extends ServerConnectionHelper {

    /**
     * Constructor.
     * 
     * @param server
     *            The server to help.
     */
    public HttpServerHelper(Server server) {
        this(server, Protocol.HTTP);
    }

    /**
     * Constructor.
     * 
     * @param server
     *            The server to help.
     * @param protocol
     *            The protocol supported.
     */
    public HttpServerHelper(Server server, Protocol protocol) {
        super(server);
        getProtocols().add(protocol);
    }

    @Override
    protected boolean canHandle(Connection<Server> connection, Response response)
            throws IOException {
        boolean result = false;

        // Check if the response is indeed the next one to be written
        // for this connection
        HttpServerInboundWay inboundWay = (HttpServerInboundWay) connection
                .getInboundWay();
        Response nextResponse = inboundWay.getMessages().peek();

        if (nextResponse != null) {
            if (nextResponse.getRequest() == response.getRequest()) {
                result = true;
            } else {
                boolean found = false;

                for (Iterator<Response> iterator = inboundWay.getMessages()
                        .iterator(); iterator.hasNext() && !found;) {
                    Response next = iterator.next();
                    found = next.getRequest() == response.getRequest();
                }

                if (!found) {
                    throw new IOException(
                            "Can't find the parent request in the list of inbound messages.");
                }
            }
        } else {
            throw new IOException(
                    "Can't find the parent request in the empty list of inbound messages.");
        }

        return result;
    }

    @Override
    public ServerInboundWay createInboundWay(Connection<Server> connection,
            int bufferSize) {
        return new HttpServerInboundWay(connection, bufferSize);
    }

    @Override
    public OutboundWay createOutboundWay(Connection<Server> connection,
            int bufferSize) {
        return new HttpServerOutboundWay(connection, bufferSize);
    }

}
