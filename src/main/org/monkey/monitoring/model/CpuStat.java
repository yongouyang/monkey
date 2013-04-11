package org.monkey.monitoring.model;

import java.math.BigDecimal;

import static org.monkey.common.utils.BigDecimalUtils.roundHalfUpTo2DP;

public class CpuStat {

    private final BigDecimal user;
    private final BigDecimal system;
    private final BigDecimal nice;
    private final BigDecimal wait;
    private final BigDecimal idle;

    public CpuStat(Double user, Double system, Double nice, Double wait, Double idle) {
        this.user = roundHalfUpTo2DP(user);
        this.system = roundHalfUpTo2DP(system);
        this.nice = roundHalfUpTo2DP(nice);
        this.wait = roundHalfUpTo2DP(wait);
        this.idle = roundHalfUpTo2DP(idle);
    }

    public BigDecimal getUser() {
        return user;
    }

    public BigDecimal getSystem() {
        return system;
    }

    public BigDecimal getNice() {
        return nice;
    }

    public BigDecimal getWait() {
        return wait;
    }

    public BigDecimal getIdle() {
        return idle;
    }

    @Override
    public String toString() {
        return "CpuStat{" +
                "user=" + user +
                ", system=" + system +
                ", nice=" + nice +
                ", wait=" + wait +
                ", idle=" + idle +
                '}';
    }
}
