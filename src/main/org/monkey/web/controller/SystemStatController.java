package org.monkey.web.controller;

import org.hyperic.sigar.SigarException;
import org.monkey.monitoring.SystemStatCollector;
import org.monkey.monitoring.model.CpuStat;
import org.monkey.monitoring.model.MemoryStat;
import org.monkey.monitoring.model.SwapStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/systemStat")
public class SystemStatController extends ExceptionAwareController {

    private final SystemStatCollector systemStatCollector;

    @Autowired
    public SystemStatController(SystemStatCollector systemStatCollector) {
        this.systemStatCollector = systemStatCollector;
    }

    @RequestMapping(value = "/cpu", method = RequestMethod.GET)
    @ResponseBody
    public List<CpuStat> collectCpuStat() throws SigarException {
        return systemStatCollector.getCpuStat();
    }

    @RequestMapping(value = "/memory", method = RequestMethod.GET)
    @ResponseBody
    public MemoryStat collectionMemoryStat() throws SigarException {
        return systemStatCollector.getMemStat();
    }

    @RequestMapping(value = "/swap", method = RequestMethod.GET)
    @ResponseBody
    public SwapStat collectionSwapStat() throws SigarException {
        return systemStatCollector.getSwapStat();
    }


}
