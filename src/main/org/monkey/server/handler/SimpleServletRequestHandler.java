package org.monkey.server.handler;

import javax.servlet.ServletRequest;

public interface SimpleServletRequestHandler {

    String handle(ServletRequest request);
}
