package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Page Object for the Open Positions listing and filters. */
public class OpenPositionsPage extends BasePage {
    /** Open Positions URL for QA department. */
    public String openPositionsPageUrl = "https://useinsider.com/careers/open-positions/?department=qualityassurance";
    /** Location filter dropdown button. */
    public By filterByLocationDropdown = By.xpath("//span[@id='select2-filter-by-location-container']");
    /** Location filter options container. */
    public By locationDropdownContainer = By.xpath("//ul[@id='select2-filter-by-location-results']");
    /** Location option items (used to match Istanbul, Turkiye). */
    public By istanbulValue = By.xpath("//ul[@id='select2-filter-by-location-results']//li[@role='option']");
    /** Department filter dropdown button. */
    public By filterByDepartmentDropdown = By.xpath("//span[@id='select2-filter-by-department-container']");
    /** Department filter options container. */
    public By departmentDropdownContainer = By.xpath("//ul[@id='select2-filter-by-department-results']");
    /** Department option items (used to match Quality Assurance). */
    public By departmentValue = By.xpath("//ul[@id='select2-filter-by-department-results']//li[@role='option']");
    /** Filtered job item container list. */
    public By filtredListItem = By.xpath("//*[@id='jobs-list']/div");
    /** Department label inside a filtered job item. */
    public By filtredItemDepartment = By.xpath(".//span[contains(@class,'position-department')]");
    /** Location label inside a filtered job item. */
    public By filtredItemLocation = By.xpath(".//div[contains(@class,'position-location')]");
    /** 'View Role' button for a filtered job item. */
    public By viewRoleButton = By.xpath(".//a[contains(text(),'View Role')]");
    /** Job title element used to hover the first filtered role. */
    public By filtredListItemJobTitle = By.xpath("//div[contains(@class,'position-list-item-wrapper')]//p[contains(@class,'position-title')]");

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