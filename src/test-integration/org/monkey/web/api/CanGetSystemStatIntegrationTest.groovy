package org.monkey.web.api

import org.hamcrest.Matchers
import org.junit.Test
import org.monkey.common.utils.AssertUtils
import org.monkey.system.IntegrationTestSupport

class CanGetSystemStatIntegrationTest extends IntegrationTestSupport {

    @Test
    public void canGetCpuStat() {
        List<Map<String, Object>> cpuStats = systemStatApi.cpuStat().succeed()

        assert cpuStats.size() >= 1

        // the assertions are almost the same as the unit test, except that
        // we assert a map instead of an object here
        def assertCpuStats = { Map stat ->
            AssertUtils.assertMapMatches([
                user: Matchers.notNullValue(BigDecimal),
                system: Matchers.notNullValue(BigDecimal),
                nice: Matchers.notNullValue(BigDecimal),
                wait: Matchers.notNullValue(BigDecimal),
                idle: Matchers.notNullValue(BigDecimal)
            ], stat)

            // includes < because of potential rounding issue
            assert stat.user + stat.system + stat.nice + stat.wait + stat.idle <= 1
        }

        cpuStats.each {
            println "Cpu Stats: ${it}"
            assertCpuStats(it)
        }
    }

    @Test
    public void canGetMemoryStat() {
        Map<String, Object> memoryStat = systemStatApi.memoryStat().succeed()

        // the assertions are almost the same as the unit test, except that
        // we assert a map instead of an object here
        AssertUtils.assertMapMatches([
            total: Matchers.notNullValue(long),
            used: Matchers.notNullValue(long),
            free: Matchers.notNullValue(long),
            usedPercent: Matchers.notNullValue(BigDecimal),
            freePercent: Matchers.notNullValue(BigDecimal)
        ], memoryStat)

        // includes < because of potential rounding issue
        assert memoryStat.used + memoryStat.free <= memoryStat.total
        assert memoryStat.usedPercent + memoryStat.freePercent <= 100
    }

    @Test
    public void canGetSwapStat() {
        Map<String, Object> swapStat = systemStatApi.swapStat().succeed()

        AssertUtils.assertMapMatches([
            total: Matchers.notNullValue(long),
            used: Matchers.notNullValue(long),
            free: Matchers.notNullValue(long)
        ], swapStat)

        // includes < because of potential rounding issue
        assert swapStat.used + swapStat.free <= swapStat.total
    }

}
