package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Page Object for Insider main page: core URLs and key locators. */
public class MainPage extends BasePage {
    /** Insider homepage URL. */
    public String insiderHomePageUrl = "https://useinsider.com/";
    /** Insider logo image in navbar (homepage link). */
    public By insiderLogo = By.xpath("//*[@class='navbar-brand d-flex flex-row align-items-center' and @aria-label='Home']/img");
    /** Footer AI-native tagline label. */
    public By aiNativeLabel = By.xpath("//*[@class='col-8 col-md-8 footer_company_tagline' and contains(normalize-space(.),'AI-native')]");
    /** Cookie accept button in consent banner. */
    public By cookieAcceptButton = By.xpath("//*[@id='wt-cli-accept-all-btn']");
    /** Top navigation 'Company' menu link. */
    public By companyLabel = By.xpath("//li[@class='nav-item dropdown']//a[@class='nav-link dropdown-toggle hide-after' and normalize-space(text()) = 'Company']");
    /** 'Careers' submenu link under Company. */
    public By careersLabel = By.xpath("//*[@class='new-menu-dropdown-layout-6-mid-container']//a[@class='dropdown-sub' and contains(normalize-space(.),'Careers')]");

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

//*[@id="navbarNavDropdown"]//li[@class='nav-item dropdown']//a[@id='navbarDropdownMenuLink' and contains(normalize-space(.),'Company')]//div[@class='dropdown-menu new-menu-dropdown-layout-6']//div[@class='new-menu-dropdown-layout-6-mid-container']
