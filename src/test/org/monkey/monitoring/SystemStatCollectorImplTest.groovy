package org.monkey.monitoring

import org.hamcrest.Matchers
import org.junit.Test
import org.monkey.common.utils.AssertUtils
import org.monkey.monitoring.model.CpuStat


class SystemStatCollectorImplTest {

    SystemStatCollector collector = new SystemStatCollectorImpl()

    @Test
    public void getCpuStats() {
        def stats = collector.cpuStat
        assert stats != null
        assert stats.size() >= 1

        def assertCpuStats = { CpuStat stat ->
            AssertUtils.assertProperties(stat, [
                user: Matchers.notNullValue(BigDecimal),
                system: Matchers.notNullValue(BigDecimal),
                nice: Matchers.notNullValue(BigDecimal),
                wait: Matchers.notNullValue(BigDecimal),
                idle: Matchers.notNullValue(BigDecimal)
            ])

            // includes < because of potential rounding issue
            assert stat.user + stat.system + stat.nice + stat.wait + stat.idle <= 1
        }

        stats.each {
            println "Cpu Stats: ${it}"
            assertCpuStats(it)
        }
    }

    @Test
    public void canGetMemStats() {
        def stat = collector.memStat
        assert stat != null

        AssertUtils.assertProperties(stat, [
            total: Matchers.notNullValue(long),
            used: Matchers.notNullValue(long),
            free: Matchers.notNullValue(long),
            usedPercent: Matchers.notNullValue(BigDecimal),
            freePercent: Matchers.notNullValue(BigDecimal)
        ])

        // includes < because of potential rounding issue
        assert stat.used + stat.free <= stat.total
        assert stat.usedPercent + stat.freePercent <= 100
    }

    @Test
    public void canGetSwapStats() {
        def stat = collector.swapStat
        assert stat != null

        AssertUtils.assertProperties(stat, [
            total: Matchers.notNullValue(long),
            used: Matchers.notNullValue(long),
            free: Matchers.notNullValue(long)
        ])

        // includes < because of potential rounding issue
        assert stat.used + stat.free <= stat.total
    }
}
