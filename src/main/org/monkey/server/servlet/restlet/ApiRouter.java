package org.monkey.server.servlet.restlet;

import org.monkey.web.controller.PingController;
import org.monkey.web.controller.StatusController;

import javax.servlet.ServletContext;

public class ApiRouter extends SpringRouter {

    public ApiRouter(RestletSpringServlet restletSpringServlet, ServletContext servletContext) {
        super(servletContext, restletSpringServlet.getContext());
    }

    @Override
    protected void attachRoutes() {
        attach("/status", StatusController.class);
        attach("/ping", PingController.class);
    }
}
