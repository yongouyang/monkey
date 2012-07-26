package org.monkey.steps

import org.junit.After
import org.openqa.selenium.firefox.FirefoxDriver

this.metaClass.mixin(cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(cucumber.runtime.groovy.EN)

FirefoxDriver driver

Before() {
    driver = new FirefoxDriver()
}

Given(~'^I am on the (.*) page$') { String uri ->
    driver.get("http://localhost:30000/${uri}")
}

Then(~'^The page title should be (.*)') { String title ->
    assert driver.getTitle() == title
}

Given(~'^I click the link with id (.*)') { String id ->
    driver.findElementById(id).click()
}

Then(~'^I should be on the (.*) page$') { String uri ->
    assert driver.currentUrl == "http://localhost:30000/${uri}"
}

After() {
    driver.close()
}

