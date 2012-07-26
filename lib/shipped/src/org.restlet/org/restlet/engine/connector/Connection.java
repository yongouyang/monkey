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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Connector;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.engine.io.IoState;
import org.restlet.engine.io.ReadableSelectionChannel;
import org.restlet.engine.io.ReadableSocketChannel;
import org.restlet.engine.io.ReadableTraceChannel;
import org.restlet.engine.io.WritableSelectionChannel;
import org.restlet.engine.io.WritableSocketChannel;
import org.restlet.engine.io.WritableTraceChannel;
import org.restlet.util.SelectionListener;
import org.restlet.util.SelectionRegistration;

/**
 * A network connection though which messages are exchanged by connectors.
 * Messages can be either requests or responses.
 * 
 * @param <T>
 *            The parent connector type.
 * @author Jerome Louvel
 */
public class Connection<T extends Connector> implements SelectionListener {

    /** The parent connector helper. */
    private final ConnectionHelper<T> helper;

    /** The inbound way. */
    private final InboundWay inboundWay;

    /** The timestamp of the last IO activity. */
    private volatile long lastActivity;

    /**
     * The time for an idle IO connection to wait for an operation before being
     * closed.
     */
    private volatile int maxIoIdleTimeMs;

    /** The outbound way. */
    private final OutboundWay outboundWay;

    /** Indicates if the connection should be persisted across calls. */
    private volatile boolean persistent;

    /** Indicates if idempotent sequences of requests can be pipelined. */
    private volatile boolean pipelining;

    /** The readable selection channel. */
    private volatile ReadableSelectionChannel readableSelectionChannel;

    /**
     * The socket's NIO selection registration holding the link between the
     * channel and the connection.
     */
    private volatile SelectionRegistration registration;

    /** The socket address. */
    private volatile SocketAddress socketAddress;

    /** The underlying socket channel. */
    private volatile SocketChannel socketChannel;

    /** The state of the connection. */
    private volatile ConnectionState state;

    /** The writable selection channel. */
    private volatile WritableSelectionChannel writableSelectionChannel;

    /**
     * Constructor.
     * 
     * @param helper
     *            The parent connector helper.
     * @param socketChannel
     *            The underlying NIO socket channel.
     * @param controller
     *            The IO controller.
     * @param socketAddress
     *            The associated IP address.
     * @throws java.io.IOException
     */
    public Connection(ConnectionHelper<T> helper, SocketChannel socketChannel,
            ConnectionController controller, InetSocketAddress socketAddress,
            int inboundBufferSize, int outboundBufferSize) throws IOException {
        this.helper = helper;
        this.inboundWay = helper.createInboundWay(this, inboundBufferSize);
        this.outboundWay = helper.createOutboundWay(this, outboundBufferSize);
        init(socketChannel, controller, socketAddress);
    }

    /**
     * Clears the connection so it can be reused. Typically invoked by a
     * connection pool.
     */
    public void clear() {
        this.inboundWay.clear();
        this.outboundWay.clear();
        this.readableSelectionChannel = null;
        this.socketChannel = null;
        this.registration = null;
        this.state = ConnectionState.CLOSED;
        this.writableSelectionChannel = null;
    }

    /**
     * Closes the connection. By default, set the state to
     * {@link org.restlet.engine.connector.ConnectionState#CLOSED}.
     * 
     * @param graceful
     *            Indicates if a graceful close should be attempted.
     */
    public void close(boolean graceful) {
        if (graceful) {
            if (getLogger().isLoggable(Level.FINER)) {
                getLogger().log(
                        Level.FINER,
                        "Closing connection to " + getSocketAddress()
                                + " gracefully");
            }

            setState(ConnectionState.CLOSING);
        } else {
            if (getLogger().isLoggable(Level.FINER)) {
                getLogger().log(
                        Level.FINER,
                        "Closing connection to " + getSocketAddress()
                                + " immediately");
            }

            try {
                Socket socket = getSocket();

                if ((socket != null) && !socket.isClosed()) {
                    shutdown(socket);
                }
            } catch (IOException ex) {
                getLogger().log(Level.FINE,
                        "Unable to properly shutdown socket", ex);
            }

            try {
                Socket socket = getSocket();
                if ((socket != null) && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ex) {
                getLogger().log(Level.FINE, "Unable to properly close socket",
                        ex);
            } finally {
                getInboundWay().onClosed();
                getOutboundWay().onClosed();
                setState(ConnectionState.CLOSED);

                if (getLogger().isLoggable(Level.FINE)) {
                    getLogger().log(
                            Level.FINE,
                            "Connection to " + getSocketAddress()
                                    + " is now closed");
                }
            }
        }
    }

    /**
     * Asks the server connector to immediately commit the given response
     * associated to this request, making it ready to be sent back to the
     * client. Note that all server connectors don't necessarily support this
     * feature.
     * 
     * @param response
     *            The response to commit.
     */
    public void commit(Response response) {
        getHelper().getOutboundMessages().add(response);
    }

    /**
     * Creates a new readable channel.
     * 
     * @return A new readable channel.
     */
    protected ReadableSelectionChannel createReadableSelectionChannel() {
        return new ReadableSocketChannel(getSocketChannel(), getRegistration());
    }

    /**
     * Creates a new writable channel.
     * 
     * @return A new writable channel.
     */
    protected WritableSelectionChannel createWritableSelectionChannel() {
        return new WritableSocketChannel(getSocketChannel(), getRegistration());
    }

    /**
     * Returns the socket IP address.
     * 
     * @return The socket IP address.
     */
    public String getAddress() {
        return (getSocket() == null) ? null
                : (getSocket().getInetAddress() == null) ? null : getSocket()
                        .getInetAddress().getHostAddress();
    }

    /**
     * Returns the parent connector helper.
     * 
     * @return The parent connector helper.
     */
    public ConnectionHelper<T> getHelper() {
        return helper;
    }

    /**
     * Returns the size of the content buffer for receiving messages. By
     * default, it calls {@link #getInboundBufferSize()}.
     * 
     * @return The size of the content buffer for receiving messages.
     */
    public int getInboundBufferSize() {
        return getHelper().getInboundBufferSize();
    }

    /**
     * Returns the inbound way.
     * 
     * @return The inbound way.
     */
    public InboundWay getInboundWay() {
        return inboundWay;
    }

    /**
     * Returns the timestamp of the last IO activity.
     * 
     * @return The timestamp of the last IO activity.
     */
    public long getLastActivity() {
        return lastActivity;
    }

    /**
     * Returns a score representing the connection load and that could be
     * compared with other connections of the same parent connector.
     * 
     * @return A score representing the connection load.
     */
    public int getLoadScore() {
        return getInboundWay().getLoadScore() + getOutboundWay().getLoadScore();
    }

    /**
     * Returns the logger.
     * 
     * @return The logger.
     */
    public Logger getLogger() {
        return getHelper().getLogger();
    }

    /**
     * Returns the time for an idle IO connection to wait for an operation
     * before being closed.
     * 
     * @return The time for an idle IO connection to wait for an operation
     *         before being closed.
     */
    public int getMaxIoIdleTimeMs() {
        return maxIoIdleTimeMs;
    }

    /**
     * Returns the size of the content buffer for sending responses. By default,
     * it calls {@link #getOutboundBufferSize()}.
     * 
     * @return The size of the content buffer for sending responses.
     */
    public int getOutboundBufferSize() {
        return getHelper().getOutboundBufferSize();
    }

    /**
     * Returns the outbound way.
     * 
     * @return The outbound way.
     */
    public OutboundWay getOutboundWay() {
        return outboundWay;
    }

    /**
     * Returns the socket port.
     * 
     * @return The socket port.
     */
    public int getPort() {
        return (getSocket() == null) ? -1 : getSocket().getPort();
    }

    /**
     * Returns the underlying socket channel as a readable selection channel.
     * 
     * @return The underlying socket channel as a readable selection channel.
     */
    public ReadableSelectionChannel getReadableSelectionChannel() {
        return readableSelectionChannel;
    }

    /**
     * Returns the socket's NIO registration holding the link between the
     * {@link java.nio.channels.SocketChannel} and the {@link org.restlet.engine.connector.Connection}.
     * 
     * @return The socket's NIO registration holding the link between the
     *         channel and the connection.
     */
    public SelectionRegistration getRegistration() {
        return registration;
    }

    /**
     * Returns the underlying socket.
     * 
     * @return The underlying socket.
     */
    public Socket getSocket() {
        return (getSocketChannel() == null) ? null : getSocketChannel()
                .socket();
    }

    /**
     * Returns the socket address.
     * 
     * @return The socket address.
     */
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    /**
     * Returns the underlying NIO socket channel.
     * 
     * @return The underlying NIO socket channel.
     */
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    /**
     * Returns the state of the connection.
     * 
     * @return The state of the connection.
     */
    public ConnectionState getState() {
        return state;
    }

    /**
     * Returns the underlying socket channel as a writable selection channel.
     * 
     * @return The underlying socket channel as a writable selection channel.
     */
    public WritableSelectionChannel getWritableSelectionChannel() {
        return writableSelectionChannel;
    }

    /**
     * Indicates if the connection has timed out.
     * 
     * @return True if the connection has timed out.
     */
    public boolean hasTimedOut() {
        return (getMaxIoIdleTimeMs() > 0)
                && (System.currentTimeMillis() - this.lastActivity) >= getMaxIoIdleTimeMs();
    }

    /**
     * Initializes the connection and associates it to the given socket.
     * 
     * @param socketChannel
     *            The underlying NIO socket channel.
     * @param controller
     *            The underlying IO controller.
     * @param socketAddress
     *            The associated socket address.
     * @throws java.io.IOException
     */
    public void init(SocketChannel socketChannel,
            ConnectionController controller, InetSocketAddress socketAddress)
            throws IOException {
        this.persistent = helper.isPersistingConnections();
        this.pipelining = helper.isPipeliningConnections();
        this.maxIoIdleTimeMs = helper.getMaxIoIdleTimeMs();
        this.state = ConnectionState.OPENING;
        this.socketChannel = socketChannel;
        this.socketAddress = socketAddress;

        if ((controller != null) && (socketChannel != null)
                && (socketAddress != null)) {
            this.registration = (controller == null) ? null : controller
                    .register(socketChannel, 0, this);
            this.readableSelectionChannel = createReadableSelectionChannel();
            this.writableSelectionChannel = createWritableSelectionChannel();

            if (getHelper().isTracing()) {
                this.readableSelectionChannel = new ReadableTraceChannel(
                        this.readableSelectionChannel);
                this.writableSelectionChannel = new WritableTraceChannel(
                        this.writableSelectionChannel);
            }

        }

        this.lastActivity = System.currentTimeMillis();
    }

    /**
     * Indicates if the connection is available to handle new messages.
     * 
     * @return True if the connection is available to handle new messages.
     */
    public boolean isAvailable() {
        return isPersistent() && getState().equals(ConnectionState.OPEN)
                && isEmpty() && getInboundWay().isAvailable()
                && getOutboundWay().isAvailable();
    }

    /**
     * Indicates if it is a client-side connection.
     * 
     * @return True if it is a client-side connection.
     */
    public boolean isClientSide() {
        return getHelper().isClientSide();
    }

    /**
     * Indicates if the connection is empty of messages and bytes.
     * 
     * @return True if the connection is empty.
     */
    public boolean isEmpty() {
        return getInboundWay().isEmpty() && getOutboundWay().isEmpty();
    }

    /**
     * Indicates if the connection should be persisted across calls.
     * 
     * @return True if the connection should be persisted across calls.
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     * Indicates if idempotent sequences of requests can be pipelined.
     * 
     * @return True requests pipelining is enabled.
     */
    public boolean isPipelining() {
        return pipelining;
    }

    /**
     * Indicates if it is a server-side connection.
     * 
     * @return True if it is a server-side connection.
     */
    public boolean isServerSide() {
        return getHelper().isServerSide();
    }

    /**
     * Called on error. By default, it calls {@link #close(boolean)} with a
     * 'false' parameter.
     * 
     * @param message
     *            The error message.
     * @param throwable
     *            The cause of the error.
     * @param status
     *            The error status.
     */
    public void onError(String message, Throwable throwable, Status status) {
        if (getLogger().isLoggable(Level.FINER)) {
            getLogger().log(Level.FINER, message, throwable);
        } else if (getLogger().isLoggable(Level.FINE)) {
            getLogger().log(Level.FINE, message);
        }

        status = new Status(status, throwable, message);
        getInboundWay().onError(status);
        getOutboundWay().onError(status);
        close(false);
    }

    /**
     * Callback method invoked when the connection has been selected for IO
     * operations it registered interest in. By default it updates the timestamp
     * that allows the detection of expired connections and calls
     * {@link org.restlet.util.SelectionRegistration#onSelected(int)} on the inbound or outbound
     * way.
     */
    public void onSelected() {
        this.lastActivity = System.currentTimeMillis();

        if (getLogger().isLoggable(Level.FINER)) {
            String trace = null;

            if (isClientSide()) {
                trace = "Client ";
            } else {
                trace = "Server ";
            }

            getLogger().finer(
                    trace + "connection (state | inbound | outbound): "
                            + toString());

            if (this.registration != null) {
                getLogger()
                        .finer(trace
                                + "NIO selection (interest | ready | cancelled): "
                                + this.registration.toString());
            }
        }

        try {
            if (registration == null) {
                getLogger().warning(
                        "Connection with no registration selected: " + this);
            } else if (registration.isReadable()) {
                synchronized (getInboundWay().getBuffer().getLock()) {
                    getInboundWay().getRegistration().onSelected(
                            registration.getReadyOperations());
                }
            } else if (registration.isWritable()) {
                synchronized (getOutboundWay().getBuffer().getLock()) {
                    getOutboundWay().getRegistration().onSelected(
                            registration.getReadyOperations());
                }
            } else if (registration.isConnectable()) {
                // Client-side asynchronous connection
                try {
                    if (getSocketChannel().finishConnect()) {
                        open();
                    } else {
                        onError("Unable to establish a connection to "
                                + getSocketAddress(), null,
                                Status.CONNECTOR_ERROR_CONNECTION);
                    }
                } catch (IOException e) {
                    onError("Unable to establish a connection to "
                            + getSocketAddress(), e,
                            Status.CONNECTOR_ERROR_CONNECTION);
                }
            }
        } catch (Throwable t) {
            onError("Unexpected error detected. Closing the connection.", t,
                    Status.CONNECTOR_ERROR_INTERNAL);
        }
    }

    /**
     * Called back by the controller when an IO time out has been detected.
     */
    public void onTimeOut() {
        if (getHelper().getLogger().isLoggable(Level.FINE)) {
            getHelper().getLogger().fine(
                    "Closing connection with \"" + getSocketAddress()
                            + "\" due to lack of activity during "
                            + getHelper().getMaxIoIdleTimeMs() + " ms");
        }

        getInboundWay().onTimeOut();
        getOutboundWay().onTimeOut();
        close(false);
    }

    /**
     * Opens the connection. By default, set the IO state of the connection to
     * {@link org.restlet.engine.connector.ConnectionState#OPEN} and the IO state of the inbound way to
     * {@link org.restlet.engine.io.IoState#INTEREST}.
     */
    public void open() {
        setState(ConnectionState.OPEN);
        updateState();
    }

    /**
     * Reuses the connection and associates it to the given socket.
     * 
     * @param socketChannel
     *            The underlying NIO socket channel.
     * @param controller
     *            The underlying IO controller.
     * @param socketAddress
     *            The associated socket address.
     * @throws java.io.IOException
     */
    public void reuse(SocketChannel socketChannel,
            ConnectionController controller, InetSocketAddress socketAddress)
            throws IOException {
        init(socketChannel, controller, socketAddress);
    }

    /**
     * Indicates if the connection should be persisted across calls.
     * 
     * @param persistent
     *            True if the connection should be persisted across calls.
     */
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * Indicates if idempotent sequences of requests can be pipelined.
     * 
     * @param pipelining
     *            True requests pipelining is enabled.
     */
    public void setPipelining(boolean pipelining) {
        this.pipelining = pipelining;
    }

    /**
     * Sets the socket's NIO registration holding the link between the channel
     * and the way.
     * 
     * @param registration
     *            The socket's NIO registration holding the link between the
     *            channel and the way.
     */
    public void setRegistration(SelectionRegistration registration) {
        this.registration = registration;
    }

    /**
     * Sets the state of the connection.
     * 
     * @param state
     *            The state of the connection.
     */
    public void setState(ConnectionState state) {
        if (getState() != state) {
            if (getLogger().isLoggable(Level.FINER)) {
                getLogger().log(
                        Level.FINER,
                        "Connection state (old | new) : " + this.state + " | "
                                + state);
            }

            this.state = state;
        }
    }

    /**
     * Shutdowns the socket, first its input then its output.
     * 
     * @param socket
     *            The socket to shutdown.
     * @throws java.io.IOException
     */
    protected void shutdown(Socket socket) throws IOException {
        socket.shutdownInput();
        socket.shutdownOutput();
    }

    @Override
    public String toString() {
        return getState() + " | " + getInboundWay() + " | " + getOutboundWay()
                + " | " + isEmpty();
    }

    /**
     * Updates the connection states.
     */
    public boolean updateState() {
        boolean result = true;

        if (getState() != ConnectionState.CLOSED) {
            getInboundWay().updateState();
            getOutboundWay().updateState();

            // Update the registration
            result = getRegistration().setInterestOperations(
                    getInboundWay().getRegistration().getInterestOperations()
                            | getOutboundWay().getRegistration()
                                    .getInterestOperations());
        }

        return result;
    }

}
