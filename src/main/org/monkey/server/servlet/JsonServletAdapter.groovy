package org.monkey.server.servlet

import javax.servlet.Servlet
import javax.servlet.ServletConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import org.monkey.server.handler.SimpleServletRequestHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static org.monkey.server.servlet.ResponseDecorator.responseDecorator
import static org.monkey.server.servlet.SupportedContentType.APPLICATION_JSON

class JsonServletAdapter implements Servlet {

    SimpleServletRequestHandler handler
    ServletConfig servletConfig

    JsonServletAdapter(SimpleServletRequestHandler handler) {
        this.handler = handler
    }

    @Override
    void init(ServletConfig servletConfig) {
        this.servletConfig = servletConfig
    }

    @Override
    ServletConfig getServletConfig() {
        return this.servletConfig
    }

    @Override
    void service(ServletRequest request, ServletResponse response) {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            httpService((HttpServletRequest) request, (HttpServletResponse) response)
        }
    }

    @Override
    String getServletInfo() {
        return null
    }

    @Override
    void destroy() {

    }

    def httpService(HttpServletRequest request, HttpServletResponse response) {
        def responseJson = handler.handle(request)

        responseDecorator(request, response).withDefaultResponseHeaders()

        response.setContentType(APPLICATION_JSON.mimeType)

        def writer = response.writer
        writer.write(responseJson)
        writer.close()
    }
}
