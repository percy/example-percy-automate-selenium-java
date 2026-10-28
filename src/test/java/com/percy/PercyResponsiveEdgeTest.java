package com.percy;

import io.percy.selenium.Percy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Responsive DOM snapshot of an Edge session on Automate. percy.snapshot()
// needs a Percy web project token; the Automate project token used by
// PercyTest only accepts percy.screenshot().
class PercyResponsiveEdgeTest {

    public WebDriver driver;

    public String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public String AUTOMATE_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        EdgeOptions options = new EdgeOptions();
        options.setCapability("browserVersion", "latest");
        // WebDriver BiDi: lets the SDK set exact viewport widths and read
        // closed shadow roots where CDP is unavailable.
        options.setCapability("webSocketUrl", true);
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("projectName", "My Project");
        bstackOptions.put("buildName", "test percy_snapshot responsive");
        bstackOptions.put("sessionName", "Percy responsive Edge");
        bstackOptions.put("local", "false");
        bstackOptions.put("os", "Windows");
        bstackOptions.put("osVersion", "11");
        // Automate only hands back a webSocketUrl when BiDi is enabled here
        // too, and BiDi needs a 4.20+ Selenium server jar.
        bstackOptions.put("seleniumBidi", true);
        bstackOptions.put("seleniumVersion", "4.33.0");
        options.setCapability("bstack:options", bstackOptions);
        driver = new RemoteWebDriver(new URL(URL), options);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        if (driver != null) driver.quit();
    }

    @Test
    public void responsiveHomePage() throws Exception {
        driver.get("https://www.bstackdemo.com");
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.titleContains("StackDemo"));

        Percy percy = new Percy(driver);
        Map<String, Object> options = new HashMap<>();
        options.put("widths", Arrays.asList(375, 1280));
        options.put("responsiveSnapshotCapture", true);
        percy.snapshot("Home page - responsive Edge", options);
    }
}
