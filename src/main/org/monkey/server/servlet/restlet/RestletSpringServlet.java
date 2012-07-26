package org.monkey.server.servlet.restlet;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.engine.application.StatusFilter;
import org.restlet.ext.servlet.ServletAdapter;
import org.restlet.service.StatusService;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class RestletSpringServlet extends HttpServlet {

    private Context context;
    private ServletAdapter adapter;
    private StatusService statusService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        context = new Context();
        statusService = new StatusService();

        Application application = new Application();
        application.setContext(context);
        application.setStatusService(statusService);
        application.setInboundRoot(new ApiRouter(this, servletContext));
//        RestletUtils.replaceConverter(org.restlet.ext.jackson.JacksonConverter.class, new JacksonConverter());

        adapter = new ServletAdapter(servletContext);
        adapter.setNext(filters(application));

    }

    private Restlet filters(Application application) {
        StatusFilter statusFilter = new StatusFilter(context, statusService);
        statusFilter.setOverwriting(true);
        statusFilter.setNext(application);
        return statusFilter;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        adapter.service(request, response);
    }

    public Context getContext() {
        return context;
    }
}
