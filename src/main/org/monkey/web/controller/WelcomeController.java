package org.monkey.web.controller;

import org.joda.time.LocalDate;
import org.monkey.common.exception.ResourceNotFoundException;
import org.monkey.sample.model.Person;
import org.monkey.sample.model.WelcomeMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Random;

@Controller
public class WelcomeController extends ExceptionAwareController {

    @RequestMapping(value = "/welcome/{name}", method = RequestMethod.GET)
    @ResponseBody
    public WelcomeMessage welcome(@PathVariable String name) {
        Random random = new Random();
        Person person = new Person(name, BigDecimal.valueOf(1.8d), random.nextInt(60));
        return new WelcomeMessage(person, new LocalDate(), "welcome to visit monkey website");
    }

//    @RequestMapping(value = "/welcomes", method = RequestMethod.GET)
//    @ResponseBody
//    public List<WelcomeMessage> welcomes(@Request) {
//        Random random = new Random();
//        Person person = new Person("name1", BigDecimal.valueOf(1.8d), random.nextInt(60));
//        return new WelcomeMessage(person, new LocalDate(), "welcome to visit monkey website");
//    }

    @RequestMapping(value = "/welcomeWithError", method = RequestMethod.GET)
    @ResponseBody
    public WelcomeMessage welcomeWithError() {
        throw new ResourceNotFoundException("some error occured");
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    @ResponseBody
    public String welcome() {
        return "Welcome to Spring MVC";
    }


}
