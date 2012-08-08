package org.monkey.steps

import org.junit.After
import org.monkey.selenium.FirefoxDriverWrapper
import org.openqa.selenium.firefox.FirefoxDriver
import org.monkey.common.utils.SleepUtils

this.metaClass.mixin(cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(cucumber.runtime.groovy.EN)

FirefoxDriver driver

Before() {
    driver = FirefoxDriverWrapper.instance()
}

Given(~'^I am on the (.*) page$') { String uri ->
    driver.get("http://localhost:8899/${uri}")
}

Then(~'^The page title should be (.*)') { String title ->
    assert driver.getTitle() == title
}

Given(~'^I click the link with id (.*)') { String id ->
    driver.findElementById(id).click()
}

Then(~'^I should be on the (.*) page$') { String uri ->
    SleepUtils.waitFor(5000, 200) { driver.currentUrl == "http://localhost:8899/${uri}" }
    assert driver.currentUrl == "http://localhost:8899/${uri}"
}

