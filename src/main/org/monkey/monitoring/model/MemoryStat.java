package org.monkey.monitoring.model;

import java.math.BigDecimal;

import static org.monkey.common.utils.BigDecimalUtils.roundHalfUpTo2DP;

public class MemoryStat {

    // size is stored in byte, which is pretty long number, we could introduce a Size object if required
    private long total;
    private long used;
    private long free;
    private BigDecimal usedPercent;
    private BigDecimal freePercent;

    public MemoryStat(long total, long used, long free, double usedPercent, double freePercent) {
        this.total = total;
        this.used = used;
        this.free = free;
        this.usedPercent = roundHalfUpTo2DP(usedPercent);
        this.freePercent = roundHalfUpTo2DP(freePercent);
    }

    public long getTotal() {
        return total;
    }

    public long getUsed() {
        return used;
    }

    public long getFree() {
        return free;
    }

    public BigDecimal getUsedPercent() {
        return usedPercent;
    }

    public BigDecimal getFreePercent() {
        return freePercent;
    }

    @Override
    public String toString() {
        return "MemoryStat{" +
                "total=" + total +
                ", used=" + used +
                ", free=" + free +
                ", usedPercent=" + usedPercent +
                ", freePercent=" + freePercent +
                '}';
    }
}
