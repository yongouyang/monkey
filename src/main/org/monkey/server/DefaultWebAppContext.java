package org.monkey.server;

import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.HashMap;

public class DefaultWebAppContext extends WebAppContext {

    public DefaultWebAppContext(HandlerContainer parent, String webApp, String contextPath) {
        super(parent, webApp, contextPath);
        HashMap<String, Object> initParams = new HashMap<String, Object>();
        initParams.put(ContextLoader.CONFIG_LOCATION_PARAM, XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION);
        setInitParams(initParams);
        setParentLoaderPriority(true);
    }

    public DefaultWebAppContext() {
        setContextPath("/");
        setWar("src/web");

        HashMap<String, Object> initParams = new HashMap<String, Object>();
        initParams.put(ContextLoader.CONFIG_LOCATION_PARAM, XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION);
        setInitParams(initParams);

        setErrorHandler(new ErrorHandler()); // should create a custom error handler

        setParentLoaderPriority(true);
    }

}
