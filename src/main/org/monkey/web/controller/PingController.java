package org.monkey.web.controller;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Scope("prototype")
@Controller
public class PingController extends ServerResource {

    @Get("json")
    public String view() {
        return "{\"success\":\"success\"}";
    }
}
