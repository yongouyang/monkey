package org.monkey.server.handler;

import javax.servlet.ServletRequest;


public class PingHandler implements SimpleServletRequestHandler {

    public static final String SUCCESS = "{\"success\":\"success\"}";

    @Override
    public String handle(ServletRequest request) {
        return SUCCESS;
    }
}
