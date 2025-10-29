package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import com.thoughtworks.gauge.Logger;

/** Common element helpers for waits, scroll, hover, and safe click. */
public final class ElementUtils {
    
    /** Common wait durations for better readability and consistency. */
    public enum WaitTime {
        VERY_SHORT(200, "Element stability check"),
        SHORT(500, "Quick action response"),
        MEDIUM(1000, "Standard action wait"),
        LONG(2000, "Page element rendering"),
        VERY_LONG(3000, "Complex form or AJAX");

        private final int milliseconds;
        private final String description;

        WaitTime(int milliseconds, String description) {
            this.milliseconds = milliseconds;
            this.description = description;
        }

        public int getMilliseconds() {
            return milliseconds;
        }

        public String getDescription() {
            return description;
        }
    }
    private static boolean SLOW_MODE = true;         // enable/disable slow-mode pacing
    private static int HUMAN_MIN_MS = 500;           // min pause after actions
    private static int HUMAN_MAX_MS = 1000;           // max pause after actions

    private static void humanPause() {
        if (!SLOW_MODE) return;
        int delta = HUMAN_MIN_MS + (int)(Math.random() * (HUMAN_MAX_MS - HUMAN_MIN_MS + 1));
        try { Thread.sleep(delta); } catch (InterruptedException ignored) {}
    }
    private ElementUtils() {}
    /** Wait until document is complete and jQuery (if present) is idle. */
    public static void waitForPageToBeReady(WebDriver driver, long timeoutSec) {

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSec));

        // a) document.readyState === 'complete'
        wait.until(d -> ((org.openqa.selenium.JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));

        // b) if jQuery is present, ensure no active ajax
        try {
            wait.until(d -> {
                Object jq = ((org.openqa.selenium.JavascriptExecutor) d)
                        .executeScript("return window.jQuery ? jQuery.active : 0;");
                if (jq instanceof Long)    return ((Long) jq) == 0L;
                if (jq instanceof Number)  return ((Number) jq).intValue() == 0;
                return true; // if no jQuery, proceed
            });
        } catch (Exception ignored) {}
    }
    private static final long DEFAULT_TIMEOUT_SEC = 10;


    /* ====================== PUBLIC API ====================== */

    /** With locator: find, wait visible+clickable, scroll into view, hover, click. */
    public static void click(WebDriver driver, By locator) {
        click(driver, locator, DEFAULT_TIMEOUT_SEC, true);
    }

    /** With locator: customizable timeout and optional JS fallback. */
    public static void click(WebDriver driver, By locator, long timeoutSec, boolean jsFallback) {
        WebElement el = waitVisible(driver, locator, timeoutSec);
        waitClickable(driver, el, timeoutSec);
        safeScrollIntoView(driver, el);
        hover(driver, el);
        safeClick(driver, el, jsFallback);
    }

    /** With WebElement: wait clickable, scroll into view, hover, click. */
    public static void click(WebDriver driver, WebElement element) {
        waitClickable(driver, element, DEFAULT_TIMEOUT_SEC);
        safeScrollIntoView(driver, element);
        hover(driver, element);
        safeClick(driver, element, true);
    }

    /** Wait until visible and return element; throws TimeoutException if not found. */
    public static WebElement waitVisible(WebDriver driver, By locator, long timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until clickable (WebElement). */
    public static void waitClickable(WebDriver driver, WebElement el, long timeoutSec) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.elementToBeClickable(el));
    }

    /** Wait until clickable (By). */
    public static WebElement waitClickable(WebDriver driver, By locator, long timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Quickly check whether an element exists. */
    public static boolean exists(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* ====================== INTERNAL HELPERS ====================== */
    /* These helpers are internal to this class. */

    /** Scroll element into view (center) via JS. */
    private static void safeScrollIntoView(WebDriver driver, WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
        } catch (Exception ignored) {
        }
    }

    /** Hover (some controls become actionable after hover). */
    private static void hover(WebDriver driver, WebElement el) {
        try {

            new Actions(driver)
                    .moveToElement(el)
                    .pause(Duration.ofMillis(2000))
                    .perform();
                    humanPause();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("move target out of bounds")) {
            }
        }
    }

    /** Try native click first; optionally fallback to JS click on failure. */
    private static void safeClick(WebDriver driver, WebElement el, boolean jsFallback) {
        try {
            humanPause();
            el.click();
            humanPause();
        } catch (Exception e) {
            if (!jsFallback) throw e;
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            } catch (Exception inner) {
                // If both fail, rethrow original
                throw e;
            }
        }
    }

    /** Select an option by visible text from custom or native dropdown. */
    public static void selectFromDropdown(WebDriver driver,
                                          By dropdownButton,         // custom dropdown button or <select>
                                          By dropdownPanelOrSelect,  // custom panel locator or same locator for <select>
                                          By optionItems,            // custom option locator (e.g., [role='option'], .dropdown-item)
                                          String visibleText,
                                          long timeoutSec) {
        Logger.info("selectFromDropdown: " + visibleText);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        waitForPageToBeReady(driver, 12);
        WebElement control = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownButton));

        // If it's a native <select>
        if ("select".equalsIgnoreCase(control.getTagName())) {
            Select select = new Select(control);
            select.selectByVisibleText(visibleText);
            return;
        }

        // Custom dropdown
        // 1) Click the button (open panel)
        safeScrollIntoView(driver, control);
        safeClick(driver, control, false);

        // 2) Wait panel visible
        WebElement panel = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownPanelOrSelect));
        
        // 3) Stabilization wait after panel opens (page may not be stable)
        waitFor(1500);

        // 4) Try until the target option is found (incremental scroll)
        final int maxScrolls = 20;
        int tries = 0;
        while (tries++ < maxScrolls) {
            // Get currently visible options
            java.util.List<WebElement> options = panel.findElements(optionItems);

            // Find first item containing the text
            WebElement target = options.stream()
                    .filter(o -> {
                        String t = safeGetText(o);
                        return t != null && t.trim().toLowerCase().contains(visibleText.trim().toLowerCase());
                    })
                    .findFirst().orElse(null);

            if (target != null) {
                // Bring near visible area (nearest, not a jump)
                try {
                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].scrollIntoView({block:'nearest', inline:'nearest'});", target);
                } catch (Exception ignored) {}

                // Short wait for clickability
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(target));
                } catch (Exception ignored) {}

                // Safe click (no JS fallback needed here)
                safeClick(driver, target, false);
                return;
            }

            // Not found: scroll panel a bit more
            jsScrollContainerBy(driver, panel, 25);
            waitFor(150);
        }

        throw new TimeoutException("Dropdown option not found: '" + visibleText + "'");
    }
    /** Wait until jQuery Ajax queue is empty. */
    public static void waitForAjaxToFinish(WebDriver driver, int timeoutSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
            wait.until(webDriver -> {
                Object result = ((JavascriptExecutor) webDriver)
                        .executeScript("return (typeof jQuery !== 'undefined') ? (jQuery.active === 0) : true;");
                return result != null && (Boolean) result;
            });
        } catch (Exception e) {
            // jQuery not available or timeout - proceed anyway
        }
    }

    /** Wait for specified milliseconds with proper interrupt handling. */
    public static void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Wait using predefined WaitTime enum for better readability. */
    public static void waitFor(WaitTime waitTime) {
        waitFor(waitTime.getMilliseconds());
    }

    /* ----------------- internal helpers ----------------- */

    private static String safeGetText(WebElement el) {
        try { return el.getText(); } catch (Exception e) { return null; }
    }

    /** Scroll container by delta on scrollTop. */
    private static void jsScrollContainerBy(WebDriver driver, WebElement container, int deltaY) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "if(arguments[0]) { arguments[0].scrollTop = arguments[0].scrollTop + arguments[1]; }",
                    container, deltaY);
        } catch (Exception ignored) {}
    }

    /* ----------------- ACTION METHODS ----------------- */

    /** Hover over an element using Actions. */
    public static void hoverElement(WebDriver driver, WebElement element) {
        try {
            new Actions(driver)
                .moveToElement(element)
                .perform();
            waitFor(200);
        } catch (Exception e) {
            Logger.info("Hover failed: " + e.getMessage());
        }
    }

    /** Hover over an element with custom pause duration. */
    public static void hoverElement(WebDriver driver, WebElement element, int pauseMs) {
        try {
            new Actions(driver)
                .moveToElement(element)
                .pause(Duration.ofMillis(pauseMs))
                .perform();
            humanPause();
        } catch (Exception e) {
            Logger.info("Hover failed: " + e.getMessage());
        }
    }

    /** Send keys to an element using Actions (hover, click, then type). */
    public static void sendKeysWithActions(WebDriver driver, WebElement element, String text) {
        new Actions(driver)
            .moveToElement(element)
            .click()
            .sendKeys(text)
            .perform();
        waitFor(500);
    }

    /** Click element using Actions. */
    public static void clickWithActions(WebDriver driver, WebElement element) {
        new Actions(driver)
            .click(element)
            .perform();
        waitFor(300);
    }

    /** Clear and send keys to an element with JavaScript scroll. */
    public static void clearAndSendKeys(WebDriver driver, WebElement element, String text) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element);
            waitFor(500);
            element.clear();
            element.sendKeys(text);
            waitFor(500);
        } catch (Exception e) {
            Logger.info("Clear and send keys failed: " + e.getMessage());
        }
    }

    /** Switch to a new window/tab that wasn't in the beforeHandles set. */
    public static void switchToNewWindow(WebDriver driver, java.util.Set<String> beforeHandles) {
        java.util.Set<String> afterHandles = driver.getWindowHandles();
        for (String handle : afterHandles) {
            if (!beforeHandles.contains(handle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    /** Scroll to bottom of page using JavaScript. */
    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /** Wait until either a new window opens or URL changes. */
    public static void waitForNewWindowOrUrlChange(WebDriver driver, java.util.Set<String> beforeHandles, 
                                                    String beforeUrl, long timeoutSec) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        wait.until(d -> d.getWindowHandles().size() > beforeHandles.size() 
                        || !d.getCurrentUrl().equals(beforeUrl));
    }
}