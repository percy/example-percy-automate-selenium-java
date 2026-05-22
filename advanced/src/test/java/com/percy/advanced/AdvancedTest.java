package com.percy.advanced;

// PER-8195 Phase 3 — automate-selenium-java advanced example.
// Each @Test exercises one row of the Percy on Automate matrix. See
// ../../../../matrix.yml for the canonical mapping.

import io.percy.selenium.Percy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdvancedTest {
    private RemoteWebDriver driver;
    private Percy percy;

    @BeforeClass
    public void setUp() throws Exception {
        String user = System.getenv("BROWSERSTACK_USERNAME");
        String key = System.getenv("BROWSERSTACK_ACCESS_KEY");
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserName", "Chrome");
        options.setCapability("projectName", System.getenv().getOrDefault("PERCY_PROJECT", "Percy Automate Selenium-Java Advanced"));
        options.setCapability("buildName", System.getenv().getOrDefault("PERCY_BUILD", "Advanced Selenium Java"));
        options.setCapability("sessionName", "advanced_visual_test");
        options.setCapability("browserVersion", "latest");
        options.setCapability("os", "Windows");
        options.setCapability("os_version", "11");
        driver = new RemoteWebDriver(
            new URL("https://" + user + ":" + key + "@hub-cloud.browserstack.com/wd/hub"),
            options);
        driver.manage().window().setSize(new Dimension(1280, 1024));
        driver.get("https://bstackdemo.com/");
        percy = new Percy(driver);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void exercisesBaseline() {
        percy.screenshot("BStackDemo — baseline");
    }

    @Test
    public void exercisesIgnoreRegionXpaths() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("ignoreRegionXpaths", Arrays.asList("//*[@id=\"signin\"]"));
        percy.screenshot("BStackDemo — ignore via xpath", opts);
    }

    @Test
    public void exercisesIgnoreRegionSelectors() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("ignoreRegionSelectors", Arrays.asList("#signin", ".shelf-container-header"));
        percy.screenshot("BStackDemo — ignore via CSS selector", opts);
    }

    @Test
    public void exercisesCustomIgnoreRegions() {
        Map<String, Object> region = new HashMap<>();
        region.put("top", 0);
        region.put("bottom", 100);
        region.put("left", 0);
        region.put("right", 1280);
        Map<String, Object> opts = new HashMap<>();
        opts.put("customIgnoreRegions", Arrays.asList(region));
        percy.screenshot("BStackDemo — custom ignore region", opts);
    }

    @Test
    public void exercisesConsiderRegionXpaths() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("considerRegionXpaths", Arrays.asList("//*[@id=\"__next\"]"));
        percy.screenshot("BStackDemo — consider via xpath", opts);
    }

    @Test
    public void exercisesFreezeAnimation() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("freezeAnimation", true);
        percy.screenshot("BStackDemo — freeze_animation", opts);
    }

    @Test
    public void exercisesPercyCss() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("percyCSS", ".shelf-container { background: #fffde7 !important; }");
        percy.screenshot("BStackDemo — percy_css", opts);
    }

    @Test
    public void exercisesSyncMode() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("sync", true);
        percy.screenshot("BStackDemo — sync", opts);
    }

    @Test
    public void exercisesTestCaseAndLabels() {
        Map<String, Object> opts = new HashMap<>();
        opts.put("testCase", "home-smoke");
        opts.put("labels", "smoke,automate-selenium-java");
        percy.screenshot("BStackDemo — test_case + labels", opts);
    }
}
