package org.monkey.selenium;

import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxDriverWrapper extends FirefoxDriver {

    private static FirefoxDriver driver;

    public static FirefoxDriver instance() {
        if (driver == null){
            driver = new FirefoxDriver();
        }
        return driver;
    }
}
