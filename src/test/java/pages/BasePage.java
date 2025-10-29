package pages;

import driver.DriverFactory;
import utils.ElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Base Page Object providing common driver access and utility methods for all page classes. */
public abstract class BasePage {
    /** Shared WebDriver instance from DriverFactory. */
    protected final WebDriver driver;
    /** Default WebDriverWait with 10 second timeout. */
    protected final WebDriverWait wait;

    /** Initialize driver and wait instances. */
    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Navigate to the given URL. */
    protected void open(String url) {
        driver.get(url);
    }

    /** Wait until element is visible and return it. */
    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is clickable and return it. */
    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Scroll element into view using JavaScript. */
    protected void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
        } catch (Exception ignored) {}
    }

    /** Get current page URL. */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** Check if element exists without waiting. */
    protected boolean exists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Wait for page to be ready (document complete + jQuery idle). */
    protected void waitForPageReady(long timeoutSec) {
        ElementUtils.waitForPageToBeReady(driver, timeoutSec);
    }

    /** Wait for jQuery Ajax to finish. */
    protected void waitForAjax(int timeoutSec) {
        ElementUtils.waitForAjaxToFinish(driver, timeoutSec);
    }

    /** Click element by locator with default timeout. */
    protected void click(By locator) {
        ElementUtils.click(driver, locator);
    }

    /** Click element by locator with custom timeout. */
    protected void click(By locator, long timeoutSec) {
        ElementUtils.click(driver, locator, timeoutSec, true);
    }

    /** Click WebElement directly. */
    protected void click(WebElement element) {
        ElementUtils.click(driver, element);
    }
}
