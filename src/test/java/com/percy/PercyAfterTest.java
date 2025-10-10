package com.percy;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.percy.selenium.Percy;

import java.net.URL;
import java.time.Duration;

class PercyAfterTest {

    public Percy percy ;
    public WebDriverWait webDriverWait;

    public WebDriver driver;

    public String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public String AUTOMATE_KEY =  System.getenv("BROWSERSTACK_ACCESS_KEY");
    public String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @BeforeMethod(alwaysRun = true)
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
    // W3C-style capabilities: browserName and browserVersion stay on top-level
    options.setCapability("browserName", "chrome");
    options.setCapability("browserVersion", "latest");

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("projectName", "My Project");
        bstackOptions.put("buildName", "test percy_screenshot");
        bstackOptions.put("sessionName", "Percy second_test");
        bstackOptions.put("local", "false");
        bstackOptions.put("seleniumVersion", "3.141");
        bstackOptions.put("os", "Windows");
        bstackOptions.put("osVersion", "11");

        options.setCapability("bstack:options", bstackOptions);

        driver = new RemoteWebDriver(new URL(URL), options);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }


    @Test
    public void addProductToCart() throws Exception {

        try{
            //webdriver initilization
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            //percy initilization
            percy = new Percy(driver);

            driver.manage().window().setSize(new Dimension(1280, 1024));
            // navigate to bstackdemo
            driver.get("https://www.bstackdemo.com");

            // Check the title
            webDriverWait.until(ExpectedConditions.titleContains("StackDemo"));

            // click on the samsung product
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__next']/div/div/main/div[1]/div[2]/label/span")));
            driver.findElement(By.xpath("//*[@id='__next']/div/div/main/div[1]/div[2]/label/span")).click();

            // [percy note: important step]
            // Percy Snapshot 1
            // take percy snapshot using the appropriate command for the project token
            takePercySnapshot(percy, "screenshot_1");

            // Save the text of the product for later verify
            String productOnScreenText = driver.findElement(By.xpath("//*[@id=\"10\"]/p")).getText();

            // Click on add to cart button
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"10\"]/div[4]")));
            driver.findElement(By.xpath("//*[@id=\"10\"]/div[4]")).click();

            // See if the cart is opened or not
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("float-cart__content")));

            // Get text of product in cart
            String productOnCartText = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")).getText();

            // [percy note: important step]
            // Percy Snapshot 2
            // take percy snapshot using the appropriate command for the project token
            takePercySnapshot(percy, "screenshot_2");

            Assert.assertEquals(productOnScreenText, productOnCartText);


        } catch (Exception e) {
            System.out.println("Error occured while executing script :" + e);
        }
    }

    /**
     * Call the appropriate Percy API depending on the PERCY_TOKEN.
     * If token starts with "auto_" this is an Automate project and uses screenshot().
     * Otherwise use snapshot() for web projects.
     */
    private void takePercySnapshot(Percy percy, String name) {
        String token = System.getenv("PERCY_TOKEN");
        if (token != null && token.startsWith("auto_")) {
            percy.screenshot(name);
        } else {
            percy.snapshot(name);
        }
    }
}
