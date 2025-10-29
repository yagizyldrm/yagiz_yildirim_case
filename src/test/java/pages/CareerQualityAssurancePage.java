package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Page Object for the Quality Assurance careers landing page. */
public class CareerQualityAssurancePage extends BasePage {
    /** QA careers landing page URL. */
    public String careerQualityAssurancePageUrl = "https://useinsider.com/careers/quality-assurance/";
    /** 'See all QA jobs' navigation button. */
    public By seeAllJobsButton = By.xpath("//a[contains(@class,'btn') and contains(text(),'See all QA jobs')]");

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
}
