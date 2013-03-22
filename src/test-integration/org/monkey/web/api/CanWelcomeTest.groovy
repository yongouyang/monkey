package org.monkey.web.api

import org.hamcrest.Matchers
import org.joda.time.LocalDate
import org.junit.Test
import org.monkey.common.utils.AssertUtils
import org.monkey.system.IntegrationTestSupport

class CanWelcomeTest extends IntegrationTestSupport {

    @Test
    public void welcome() {
        assert welcomeApi.welcome().succeed() == "Welcome to Spring MVC"
    }

    @Test
    public void welcomeWithAName() {
        def welcomed = welcomeApi.welcome("somebody").succeed()
        AssertUtils.assertMapMatches([
            message: "welcome to visit monkey website",
            data: null,
            date: new LocalDate().toString(),
            aPerson: Matchers.instanceOf(Map)
        ], welcomed)
    }

    @Test
    public void welcomeWithError() {
        def failed = welcomeApi.expectError(404).welcomeWithError().fail()
        AssertUtils.assertMapMatches([
            responseCode: 404,
            type: "ResourceNotFoundException",
            message: "some error occured",
            stackTrace: Matchers.notNullValue(String)
        ], failed)
    }
}
