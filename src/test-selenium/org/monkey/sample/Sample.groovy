package org.monkey.sample

import org.junit.Test
import org.openqa.selenium.firefox.FirefoxDriver

class Sample {

    @Test
    public void seleniumTest() {
        def driver = new FirefoxDriver()
        driver.get("http://localhost:30000/status")

        def title = driver.findElementByTagName("title")
        assert title.text == "Status Page"

        def checkStatusLink = driver.findElementById("checkStatus")
        checkStatusLink.click()

        assert driver.currentUrl == "http://localhost:30000/ping"
        driver.close()

    }
}
