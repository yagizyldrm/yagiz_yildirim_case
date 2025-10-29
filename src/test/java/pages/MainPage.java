package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Page Object for Insider main page: core URLs and key locators. */
public class MainPage extends BasePage {
    /** Insider homepage URL. */
    public String insiderHomePageUrl = "https://useinsider.com/";
    /** Insider logo image in navbar (homepage link). */
    public By insiderLogo = By.xpath("//a[contains(@class,'navbar-brand')]/img");
    /** Footer AI-native tagline label. */
    public By aiNativeLabel = By.xpath("//*[@id='footer']//*[contains(text(),'AI-native')]");
    /** Cookie accept button in consent banner. */
    public By cookieAcceptButton = By.id("wt-cli-accept-all-btn");
    /** Top navigation 'Company' menu link. */
    public By companyLabel = By.xpath("//a[contains(@class,'nav-link') and contains(text(),'Company')]");
    /** 'Careers' submenu link under Company. */
    public By careersLabel = By.xpath("//a[@class='dropdown-sub' and contains(text(),'Careers')]");

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
}

