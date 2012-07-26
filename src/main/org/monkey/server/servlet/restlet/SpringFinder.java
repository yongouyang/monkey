package org.monkey.server.servlet.restlet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.springframework.context.ApplicationContext;

public class SpringFinder extends Finder {

    private final ApplicationContext applicationContext;

    public SpringFinder(ApplicationContext applicationContext, Context context, Class<? extends ServerResource> targetClass) {
        super(context, targetClass);
        this.applicationContext = applicationContext;
    }

    @Override
    public ServerResource create(Class<? extends ServerResource> targetClass, Request request, Response response) {
        return applicationContext.getBean(targetClass);
    }
}
