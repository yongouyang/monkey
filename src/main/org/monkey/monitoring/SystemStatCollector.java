package org.monkey.monitoring;

import org.hyperic.sigar.SigarException;
import org.monkey.monitoring.model.CpuStat;
import org.monkey.monitoring.model.MemoryStat;
import org.monkey.monitoring.model.SwapStat;

import java.util.List;

public interface SystemStatCollector {
    List<CpuStat> getCpuStat() throws SigarException;

    MemoryStat getMemStat() throws SigarException;

    SwapStat getSwapStat() throws SigarException;
}
