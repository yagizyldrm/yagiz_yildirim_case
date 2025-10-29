package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Scrolling utilities to center elements via JS smooth scroll with Actions fallback. */
public final class ScrollUtils {
    private ScrollUtils() {}
    /* ----------------------------- HIGH-LEVEL API ----------------------------- */

    /** Soft-scroll to the target element at locator: try JS smooth first, then Actions micro-steps as fallback. */
    public static WebElement smoothScrollToAndCenter(WebDriver driver, By locator, long durationMs, long timeoutSec) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        WebElement el = driver.findElement(locator);

        boolean centered = jsSmoothScrollToCenter(driver, el);  // 1) JS native smooth
        if (!centered) {
            actionsSmoothScrollToCenter(driver, el, durationMs); // 2) Fallback: Actions micro-steps
        }

        // Stabilize visibility and center check
        wait.until(ExpectedConditions.visibilityOf(el));
        wait.until(drv -> isVerticallyCentered(drv, el, 6)); // ±6px tolerance
        return el;
    }

    /* ----------------------------- JS SMOOTH ----------------------------- */

    /** JS native smooth scroll; returns true if centered. */
    private static boolean jsSmoothScrollToCenter(WebDriver driver, WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "if (arguments[0]) {" +
                            "  arguments[0].scrollIntoView({behavior:'smooth', block:'center', inline:'nearest'});" +
                            "}", el);

            // 600–900ms typical smooth duration; smart-wait here
            long start = System.currentTimeMillis();
            long maxWait = 2000; // 2s
            while (System.currentTimeMillis() - start < maxWait) {
                if (isVerticallyCentered(driver, el, 6)) return true;
                try { Thread.sleep(30); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        } catch (JavascriptException | StaleElementReferenceException ignored) {}
        return false;
    }

    /* ----------------------------- ACTIONS EASING ----------------------------- */

    /* Smooth scrolling with Actions */

    private static void actionsSmoothScrollToCenter(WebDriver driver, WebElement el, long durationMs) {
        try {
            Actions actions = new Actions(driver);

            // target deltaY: element center distance to viewport center
            int deltaY = verticalDeltaToCenter(driver, el);
            if (Math.abs(deltaY) < 2) return;

            long frames = Math.max(1, durationMs / 16); // ~60fps
            long t0 = System.currentTimeMillis();

            for (int i = 1; i <= frames; i++) {
                double progress = (System.currentTimeMillis() - t0) / (double) durationMs;
                if (progress > 1) progress = 1;

                // easeOutCubic: smooth ending
                double eased = 1 - Math.pow(1 - progress, 3);
                double target = deltaY * eased;

                // how far are we now
                int currentDelta = verticalDeltaToCenter(driver, el);
                // tiny steps when close to the element
                int step = clamp((int) Math.round(currentDelta / 5.0), -120, 120);
                if (Math.abs(currentDelta) <= 4) break;

                actions.scrollByAmount(0, step).perform();
                try { Thread.sleep(16); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            // final touch: ensure centered (short, not a jump)
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);

        } catch (StaleElementReferenceException ignored) {
            // may need to re-locate during render; handled by caller flow
        }
    }

    /* ----------------------------- GEOMETRY HELPERS ----------------------------- */

    /** Vertical delta (in px) between element center and viewport center. */
    private static int verticalDeltaToCenter(WebDriver driver, WebElement el) {
        try {
            Object res = ((JavascriptExecutor) driver).executeScript(
                    "if (!arguments[0]) return 0;" +
                            "const r = arguments[0].getBoundingClientRect();" +
                            "const vh = (window.innerHeight || document.documentElement.clientHeight);" +
                            "const centerY = r.top + (r.height/2);" +
                            "return Math.floor(centerY - (vh/2));", el);
            return (res instanceof Long) ? ((Long) res).intValue()
                    : (res instanceof Number) ? ((Number) res).intValue() : 0;
        } catch (JavascriptException | StaleElementReferenceException e) {
            return 0;
        }
    }

    /** Is the element vertically centered within ±tolerance pixels? */
    private static boolean isVerticallyCentered(WebDriver driver, WebElement el, int tolerancePx) {
        int delta = Math.abs(verticalDeltaToCenter(driver, el));
        return delta <= Math.max(0, tolerancePx);
    }

    /* ----------------------------- ELEMENT FINDING BY SCROLLING ----------------------------- */

    /** Find element by scrolling down the page until found or page bottom reached. */
    public static WebElement findElementByScrolling(WebDriver driver, By locator, int scrollStep, int maxAttempts) {
        for (int i = 0; i < maxAttempts; i++) {
            try {
                // Element var mı kontrol et
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    return element; // Element bulundu!
                }
            } catch (NoSuchElementException e) {
                // Element bulunamadı, scroll yap
            }
            
            // Smooth scroll - küçük adımlarla
            int smallStep = scrollStep / 4; // 500px yerine 125px
            for (int j = 0; j < 4; j++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy({top: " + smallStep + ", behavior: 'smooth'});");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            
            // Sayfa sonuna geldik mi kontrol et
            Long scrollHeight = (Long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight;");
            Long scrollTop = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
            Long windowHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight;");
            
            if (scrollTop + windowHeight >= scrollHeight) {
                // Sayfa sonuna geldik, element bulunamadı
                throw new NoSuchElementException("Element not found after scrolling to bottom of page");
            }
        }
        
        throw new NoSuchElementException("Element not found after " + maxAttempts + " scroll attempts");
    }

    /* ----------------------------- MISC ----------------------------- */

    private static int clamp(int v, int min, int max) { return Math.max(min, Math.min(max, v)); }

}
