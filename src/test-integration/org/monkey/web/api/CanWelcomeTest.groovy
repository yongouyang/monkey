package org.monkey.web.api

import org.hamcrest.Matchers
import org.joda.time.LocalDate
import org.junit.Test
import org.monkey.common.utils.AssertUtils
import org.monkey.system.IntegrationTestSupport

class CanWelcomeTest extends IntegrationTestSupport {

    @Test
    public void welcome() {
        assert welcomeApi.welcome() == "Welcome to Spring MVC"
    }

    @Test
    public void welcomeWithAName() {
        def welcomed = welcomeApi.welcome("somebody")
        AssertUtils.assertMapMatches([
            message: "welcome to visit monkey website",
            data: null,
            date: new LocalDate().toString(),
            aPerson: Matchers.instanceOf(Map)
        ], welcomed)
    }
}
