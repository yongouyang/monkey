package org.monkey.monitoring;

import org.hyperic.sigar.*;
import org.monkey.monitoring.model.CpuStat;
import org.monkey.monitoring.model.MemoryStat;
import org.monkey.monitoring.model.SwapStat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SystemStatCollectorImpl implements SystemStatCollector {

    private final Sigar sigar = new Sigar();

    @Override
    public List<CpuStat> getCpuStat() throws SigarException {
        // simply map from sigar's pojo to our pojo
        CpuPerc[] cpuPercList = sigar.getCpuPercList();
        List<CpuStat> cpuStats = new ArrayList<CpuStat>(cpuPercList.length);
        for (CpuPerc cpuPerc : cpuPercList) {
            cpuStats.add(new CpuStat(
                    cpuPerc.getUser(),
                    cpuPerc.getSys(),
                    cpuPerc.getNice(),
                    cpuPerc.getWait(),
                    cpuPerc.getIdle()
            ));
        }
        return cpuStats;
    }

    @Override
    public MemoryStat getMemStat() throws SigarException {
        Mem mem = sigar.getMem();
        return new MemoryStat(mem.getTotal(), mem.getUsed(), mem.getFree(), mem.getUsedPercent(), mem.getFreePercent());
    }

    @Override
    public SwapStat getSwapStat() throws SigarException {
        Swap swap = sigar.getSwap();
        return new SwapStat(swap.getTotal(), swap.getUsed(), swap.getFree());
    }

}
