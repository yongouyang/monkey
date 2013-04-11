package org.monkey.monitoring.model;

public class SwapStat {

    // size is stored in byte, which is pretty long number, we could introduce a Size object if required
    private long total;
    private long used;
    private long free;

    public SwapStat(long total, long used, long free) {
        this.total = total;
        this.used = used;
        this.free = free;
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

    @Override
    public String toString() {
        return "SwapStat{" +
                "total=" + total +
                ", used=" + used +
                ", free=" + free +
                '}';
    }
}
