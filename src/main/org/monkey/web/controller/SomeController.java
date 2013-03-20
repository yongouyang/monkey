package org.monkey.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SomeController {

    @RequestMapping(value = "/welcome/{name}", method = RequestMethod.GET)
    @ResponseBody
    public String welcomeHandler(@PathVariable String name) {
        return name + ", Welcome to Spring MVC";
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    @ResponseBody
    public String welcomeHandler() {
        return "Welcome to Spring MVC";
    }
}
