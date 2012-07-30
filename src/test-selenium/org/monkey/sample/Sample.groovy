package org.monkey.sample

import org.junit.Test
import org.openqa.selenium.firefox.FirefoxDriver
import org.monkey.selenium.FirefoxDriverWrapper

class Sample {

    @Test
    public void seleniumTest() {
        def driver = FirefoxDriverWrapper.instance()
        driver.get("http://localhost:30000/status")

        def title = driver.findElementByTagName("title")
        assert title.text == "Status Page"

        def checkStatusLink = driver.findElementById("checkStatus")
        checkStatusLink.click()

        assert driver.currentUrl == "http://localhost:30000/ping"
        driver.close()

    }
}
