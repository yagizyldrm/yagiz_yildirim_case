package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Page Object for Careers page sections used in validations. */
public class CareersPage extends BasePage {
    /** Careers page URL. */
    public String careersUrl = "https://useinsider.com/careers/";
    /** Team section container. */
    public By teamBlockElement = By.xpath("(//div[contains(@class,'job-item')])[1]");
    /** Location slider list. */
    public By locationBlockElement = By.xpath("//div[@id='location-slider']//ul[@class='glide__slides']");
    /** Life at Insider carousel wrapper. */
    public By lifeAtInsiderBlockElement = By.xpath("//div[contains(@class,'elementor-swiper')]//div[contains(@class,'swiper-wrapper')]");

    /** Open page by URL using BasePage helper. */
    public void openPageByUrl(String url) {
        super.open(url);
    }
    
    /** Get current URL using BasePage helper. */
    public String getCurrentUrl() {
        return super.getCurrentUrl();
    }
    
    /** Wait for element to be visible using BasePage helper. */
    public WebElement waitVisible(By locator) {
        return super.waitVisible(locator);
    }
    
    /** Wait for element to be clickable using BasePage helper. */
    public WebElement waitClickable(By locator) {
        return super.waitClickable(locator);
    }
    
    /** Click element using BasePage helper. */
    public void click(By locator) {
        super.click(locator);
    }
    
    /** Wait for page to be ready using BasePage helper. */
    public void waitForPageReady(long timeoutSec) {
        super.waitForPageReady(timeoutSec);
    }
    
    /** Wait for Ajax to finish using BasePage helper. */
    public void waitForAjax(int timeoutSec) {
        super.waitForAjax(timeoutSec);
    }

    /** Hover WebElement with Actions using BasePage helper. */
    public void hoverWithActions(WebElement element){ super.hoverWithActions(element);}

    /** Hover WebElement with Actions including Human Pause using BasePage helper. */
    public void hoverWithActionsAndHumanPause(WebElement element,int pauseMs) {super.hoverWithActionsAndHumanPause(element,pauseMs);}
}