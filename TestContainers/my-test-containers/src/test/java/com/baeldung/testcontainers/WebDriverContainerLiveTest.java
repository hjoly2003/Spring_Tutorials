package com.baeldung.testcontainers;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

public class WebDriverContainerLiveTest {
    
    /**
     * [N]:test-container - Enables running Chrome and Firefox in docker-selenium containers.
     */
    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer().withCapabilities(new ChromeOptions());

    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {

        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://example.com");

        String heading = driver.findElement(By.xpath("/html/body/div/h1")).getText();

        assertEquals("Example Domain", heading);
    }
    
}
