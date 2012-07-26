package org.monkey.server.servlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringApplicationContextHolder implements ServletContextListener {
    private static WebApplicationContext applicationContext;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public static WebApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
