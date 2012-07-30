package org.monkey;

import cucumber.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.monkey.selenium.FirefoxDriverWrapper;
import org.monkey.system.TestingSystem;

@RunWith(Cucumber.class)
@Cucumber.Options(tags = {"@ui"})
public class RunSeleniumTest {

    @BeforeClass
    public static void init(){
        TestingSystem.init();
    }

}
