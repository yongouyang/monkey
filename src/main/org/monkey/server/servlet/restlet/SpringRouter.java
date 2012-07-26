package org.monkey.server.servlet.restlet;

import org.restlet.Context;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public abstract class SpringRouter extends Router {

    private final ApplicationContext applicationContext;

    public SpringRouter(ServletContext servletContext, Context context) {
        super(context);
        this.applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        attachRoutes();
    }

    @Override
    public Finder createFinder(Class<? extends ServerResource> targetClass) {
        return new SpringFinder(applicationContext, getContext(), targetClass);
    }

    protected abstract void attachRoutes();
}
