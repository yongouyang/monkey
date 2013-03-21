package org.monkey.server;

import org.monkey.common.utils.config.ApplicationStartupUtils;
import org.monkey.server.handler.SimpleServletRequestHandler;
import org.monkey.server.servlet.JsonServletAdapter;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;


public class JettyServer implements InertialComponent {

    private int port;
    private int sslPort;
    private Server server;
    private String serviceName;
    private String serviceDescription;
    private Context rootContext;

    public JettyServer(int port, String serviceName, String serviceDescription) {
        this(port, -1, serviceName, serviceDescription);
    }

    public JettyServer(int port, int sslPort, String serviceName, String serviceDescription) {
        this.port = port;
        this.sslPort = sslPort;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        initServerInternal();
    }

    protected void initServerInternal() {
        this.server = new Server();
        server.addConnector(new HttpConnector(port, sslPort));
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        rootContext = new DefaultWebAppContext(contextHandlerCollection, ApplicationStartupUtils.getWarPath(), "/");

    }

    protected void mountStatusHandler(SimpleServletRequestHandler statusHandler) {
        mount("/Status", statusHandler);
    }

    protected void mount(String routerPath, SimpleServletRequestHandler handler) {
        mount(routerPath, new JsonServletAdapter(handler));
    }

    protected void mount(String routerPath, Servlet servlet) {
        rootContext.addServlet(new ServletHolder(servlet), routerPath);
    }

    protected void join() throws InterruptedException {
        if (server != null) {
            server.join();
        }
    }

    public static void runWithShutdownHookAndWait(final JettyServer server) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.stop();
            }
        });
        server.start();
        try {
            server.join();
        } catch (InterruptedException e) {
            System.out.println("Server interrupted: " + e);
        }
    }

    @Override
    public void start() {
        if (server != null) {
            try {
                server.start();
            } catch (Exception e) {
                throw new RuntimeException("Failed to start jetty", e);
            }
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new RuntimeException("Failed to stop jetty", e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return server != null && server.isRunning();
    }
}
