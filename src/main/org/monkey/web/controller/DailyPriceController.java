package org.monkey.web.controller;

import org.monkey.sample.model.SampleDailyPrice;
import org.monkey.service.DailyPriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class DailyPriceController extends ExceptionAwareController {

    private DailyPriceDao dailyPriceDao;

    @Autowired
    public DailyPriceController(DailyPriceDao dailyPriceDao) {
        this.dailyPriceDao = dailyPriceDao;
    }

    @RequestMapping(value = "/DailyPrice", method = RequestMethod.GET)
    @ResponseBody
    public SampleDailyPrice viewByRicAndTradeDate(@RequestParam String ricCode, @RequestParam String tradeDate) {
        return dailyPriceDao.find(String.format("{ricCode:'%s', tradeDate:'%s'}", ricCode, tradeDate));
    }

    @RequestMapping(value = "/DailyPrice", method = RequestMethod.POST, headers = "Content-Type=application/json")
    @ResponseBody
    public String create(@RequestBody SampleDailyPrice payload, HttpServletResponse response) {
        dailyPriceDao.saveOrUpdate(payload);
        String uri = String.format("/DailyPrice?ricCode=%s&tradeDate=%s", payload.getRicCode(), payload.getTradeDate());
        response.setHeader("Location", uri);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return uri;
    }


}
