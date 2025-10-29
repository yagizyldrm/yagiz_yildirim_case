package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Page Object for the job application page after clicking View Role. */
public class ViewJobPage extends BasePage {
    /** Apply for this job button locator. */
    public By applyForThisJobButton = By.xpath("//div[contains(@class,'postings-btn-wrapper')]//a[contains(@class,'postings-btn') and contains(text(),'Apply for this job')]");

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
    
    /** Check if element exists without waiting. */
    public boolean exists(By locator) {
        return super.exists(locator);
    }
}
