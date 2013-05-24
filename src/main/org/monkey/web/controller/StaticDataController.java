package org.monkey.web.controller;

import org.monkey.sample.market.SecuritiesDownloader;
import org.monkey.sample.model.Security;
import org.monkey.service.SecurityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/staticData")
public class StaticDataController extends ExceptionAwareController {

    private SecuritiesDownloader securitiesDownloader;
    private SecurityDao securityDao;

    @Autowired
    public StaticDataController(SecuritiesDownloader securitiesDownloader, SecurityDao securityDao) {
        this.securitiesDownloader = securitiesDownloader;
        this.securityDao = securityDao;
    }

    @RequestMapping(value = "/download/security", method = RequestMethod.GET)
    @ResponseBody
    public List<Security> downloadSecurity() throws IOException {
        List<Security> downloaded = securitiesDownloader.download();
        for (Security security : downloaded) {
            securityDao.saveOrUpdate(security);
        }
        return downloaded;
    }




}
