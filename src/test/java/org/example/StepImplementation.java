package org.example;

import com.thoughtworks.gauge.Step;
import driver.DriverFactory;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CareerQualityAssurancePage;
import pages.MainPage;
import pages.CareersPage;
import java.time.Duration;
import pages.OpenPositionsPage;
import pages.ViewJobPage;
import pages.ApplicationFormPage;
import utils.ElementUtils;
import utils.ScrollUtils;
import com.thoughtworks.gauge.Logger;

/**
 * Gauge step definitions that drive the Insider careers QA scenario.
 *
 * The steps use Page Object classes in the `pages` package and utility helpers
 * in the `utils` package to perform high-level user actions:
 * - Navigate to Insider homepage and validate key UI elements
 * - Handle cookie consent once per run
 * - Navigate to Careers and verify content blocks
 * - Open QA careers, filter by location/department, and validate listings
 * - Attempt to open a role and verify redirection behavior
 */
public class StepImplementation {
    private final WebDriver driver;
    private MainPage mainPage;
    private CareersPage careersPage;
    private CareerQualityAssurancePage careerQualityAssurancePage;
    private OpenPositionsPage openPositionsPage;
    private ViewJobPage viewJobPage;
    private ApplicationFormPage applicationFormPage;
    public StepImplementation (){
        this.driver = DriverFactory.getDriver();
        this.mainPage = new MainPage();
        this.careersPage = new CareersPage();
        this.careerQualityAssurancePage = new CareerQualityAssurancePage();
        this.openPositionsPage = new OpenPositionsPage();
        this.viewJobPage = new ViewJobPage();
        this.applicationFormPage = new ApplicationFormPage();
    }
    private static boolean cookieHandled = false;

    /** Open Insider homepage and verify basic URL contains 'useinsider'. */
    @Step("Go to Insider Website")
    public void goToInsiderWebsite() {
        Logger.info("Go to Insider Home Page");
        mainPage.openPageByUrl(mainPage.insiderHomePageUrl);
        String currentUrl = mainPage.getCurrentUrl();
        Assertions.assertThat(currentUrl)
                .as("Navigation failed! URL could be corrupt or Website isn't up!" + currentUrl)
                .contains("useinsider");
    }

    /** Validate homepage is rendered by checking logo and footer label. */
    @Step("Confirm the Website is correct")
    public void confirmTheWebsiteCorrectAndUp() {
        WebElement checkLogoExist = mainPage.waitVisible(mainPage.insiderLogo);
        Assertions.assertThat(checkLogoExist.isDisplayed())
                .as("Failed about verifying the Insider Website").isTrue();
        
        ElementUtils.scrollToBottom(driver);
        ElementUtils.waitFor(2000);
        
        WebElement insiderRightsLabelExist = mainPage.waitVisible(mainPage.aiNativeLabel);
        Assertions.assertThat(insiderRightsLabelExist.isDisplayed())
                .as("Insider Ai Native Label Doesn't Exist on Footer").isTrue();
    }

    /** If cookie banner is present, accept it. Runs once per execution. */
    @Step("Check the Cookie is exist, if yes Click Accept Button, if isn't continue to the next step")
    public void clickCookieAcceptButtonIfItsDisplayed() {
        Logger.info("Cookie step triggered @clickCookieAcceptButtonIfItsDisplayed");
        if (cookieHandled) {
            Logger.info("Cookie was already handled earlier. Skipping this step.");
            return;
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            java.util.List<WebElement> cookies = driver.findElements(mainPage.cookieAcceptButton);
            if (cookies.isEmpty()) {
                Logger.info("No cookie popup found on this page.");
                cookieHandled = true;
                return;
            }

            WebElement cookieAcceptButton = mainPage.waitClickable(mainPage.cookieAcceptButton);

            js.executeScript("arguments[0].scrollIntoView({block:'center'})", cookieAcceptButton);
            js.executeScript("arguments[0].click();", cookieAcceptButton);
            Logger.info("Cookie popup accepted successfully.");

            ElementUtils.waitFor(500);
            cookieHandled = true;

        } catch (Exception e) {
            Logger.info("Cookie popup could not be handled safely: " + e.getMessage());
        }
    }

    /** Hover 'Company' menu, click 'Careers', and assert we land on careers URL. */
    @Step("Hover Company Menu and Click Careers and Verify Navigation to Careers Page")
    public void hoverCompanyMenuAndClickCareers() {
        WebElement companyLabelItem = mainPage.waitVisible(mainPage.companyLabel);
        ElementUtils.hoverElement(driver, companyLabelItem);
        ElementUtils.waitFor(1000);
        
        mainPage.waitClickable(mainPage.careersLabel);
        mainPage.click(mainPage.careersLabel);
        ElementUtils.waitFor(2000);
        
        String currentUrl = mainPage.getCurrentUrl();
        Assertions.assertThat(currentUrl)
                .as("Clicking 'Careers' did NOT redirect to the Careers page!")
                .contains(careersPage.careersUrl);
    }

    /** Scroll to and verify 'Team' block is visible on Careers page. */
    @Step("Verify Team Block is Open")
    public void verifyTeamBlockIsOpen() {
        WebElement teamBlock = ScrollUtils.smoothScrollToAndCenter(
                driver,
                careersPage.teamBlockElement,
                900,
                10
        );

        Assertions.assertThat(teamBlock.isDisplayed())
                .as("Team Block is not visible after smooth scroll").isTrue();
    }

    /** Scroll to and verify 'Location' block is visible on Careers page. */
    @Step("Verify Location Block is Open")
    public void verifyLocationBlockIsOpen() {
        WebElement locationBlock = ScrollUtils.smoothScrollToAndCenter(
                driver,
                careersPage.locationBlockElement,
                900,
                10
        );

        Assertions.assertThat(locationBlock.isDisplayed())
                .as("Location Block is not visible after smooth scroll").isTrue();
    }

    /** Scroll to and verify 'Life at Insider' block is visible on Careers page. */
    @Step("Verify Life At Insider Block is Open")
    public void verifyLifeAtInsiderBlockIsOpen() {
        WebElement lifeAtInsiderBlock = ScrollUtils.smoothScrollToAndCenter(
                driver,
                careersPage.lifeAtInsiderBlockElement,
                900,
                10
        );

        Assertions.assertThat(lifeAtInsiderBlock.isDisplayed())
                .as("Life At Insider is not visible after smooth scroll").isTrue();
    }

    /** Utility wait step for a given number of seconds. */
    @Step("Wait <seconds> seconds")
    public void waitTwoSeconds(int seconds) {
        Logger.info(seconds + " seconds waiting");
        ElementUtils.waitFor(seconds * 1000);
    }

    /** Navigate directly to the QA careers landing page and verify URL. */
    @Step("Go to Career Quality Assurance Page")
    public void goToCareerQualityAssurancePage() {
        Logger.info("Opening Career Quality Assurance Page...");
        careerQualityAssurancePage.openPageByUrl(careerQualityAssurancePage.careerQualityAssurancePageUrl);
        String currentUrl = careerQualityAssurancePage.getCurrentUrl();
        Assertions.assertThat(currentUrl)
                .as("Navigation failed! URL could be corrupt or Website isn't up!" + currentUrl)
                .contains("quality-assurance");
    }

    /** Click 'See all QA jobs' and verify we land on Open Positions page. */
    @Step("Click See All Jobs Button and Verify Navigation")
    public void clickSeeAllJobsButton() {
        Logger.info("Clicking See All Jobs Button...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            careerQualityAssurancePage.click(careerQualityAssurancePage.seeAllJobsButton);
        } catch (Exception e) {
            org.assertj.core.api.Assertions.fail(
                    "'See All Jobs' button could not be clicked.", e);
        }
        ElementUtils.waitFor(1000);
        wait.until(ExpectedConditions.urlContains("qualityassurance"));
        String currentUrl = openPositionsPage.getCurrentUrl();
        Assertions.assertThat(currentUrl)
                .as("URLs are not matched").contains(openPositionsPage.openPositionsPageUrl);
        ElementUtils.waitFor(1000);
        Logger.info("STEP: See All Jobs end");
    }

    /** Filter Open Positions by location: Istanbul, Turkiye. */
    @Step("Select Istanbul, Turkiye on Location Dropdown")
    public void selectIstanbulOnLocationDropdown() {
        Logger.info("STEP: Location start");
        openPositionsPage.waitForAjax(15);
        openPositionsPage.waitForPageReady(15);
        By ddlButton = openPositionsPage.filterByLocationDropdown;
        By ddlPanel  = openPositionsPage.locationDropdownContainer;
        By ddlItems  = openPositionsPage.istanbulValue;

        ElementUtils.selectFromDropdown(driver, ddlButton, ddlPanel, ddlItems, "Istanbul, Turkiye", 12);
        Logger.info("STEP: Location end");
    }

    /** Filter Open Positions by department: Quality Assurance. */
    @Step("Select Quality Assurance on Department Dropdown")
    public void selectQualityAssuranceOnDepartmentDropdown() {
        Logger.info("STEP: Department start");
        openPositionsPage.waitForAjax(15);
        openPositionsPage.waitForPageReady(15);
        try {
            String dept = driver.findElement(openPositionsPage.filtredItemDepartment).getText().trim();
            if (dept.toLowerCase(java.util.Locale.ROOT).contains("quality assurance")) {
                Logger.info("Department already 'Quality Assurance' (pre-applied by page). Skipping re-select.");
                return;
            }
        } catch (Exception ignored) {
            // If pill/label doesn't exist, we'll make the selection
        }

        // 1) Normal selection
        By ddlButton = openPositionsPage.filterByDepartmentDropdown;
        By ddlPanel  = openPositionsPage.departmentDropdownContainer;
        By ddlItems  = openPositionsPage.departmentValue;
        ElementUtils.selectFromDropdown(driver, ddlButton, ddlPanel, ddlItems, "Quality Assurance", 12);
        Logger.info("STEP: Department end");
    }

    /** Assert each listed position includes 'Quality Assurance' and 'Istanbul, Turkiye'. */
    @Step("Verify the List items contain Quality Assurance and Istanbul, Turkiye values")
    public void verifyListItemContainSelectedValues() {
        By listItem = openPositionsPage.filtredListItem;
        java.util.List<WebElement> items = driver.findElements(listItem);
        if (items.isEmpty()) {
            Logger.info("List is created according to selections but no data is available at this moment.");
            return;
        }

        org.assertj.core.api.SoftAssertions softly = new org.assertj.core.api.SoftAssertions();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait smallWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));

        for (int i = 0; i < items.size(); i++) {

            // 1) Get fresh list each iteration (stale solution)
            java.util.List<WebElement> fresh = driver.findElements(listItem);
            if (i >= fresh.size()) break; // safety check
            WebElement list = fresh.get(i);

            // Center item and hover
            try {
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", list);
            } catch (Exception ignored) {}
            ElementUtils.hoverElement(driver, list, 200);
            ElementUtils.waitFor(300);

            // Retry once on stale for relative locators inside item
            WebElement department;
            WebElement location;
            try {
                department = list.findElement(openPositionsPage.filtredItemDepartment);
                location   = list.findElement(openPositionsPage.filtredItemLocation);

                smallWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(department));
                smallWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(location));
            } catch (org.openqa.selenium.StaleElementReferenceException se) {
                fresh = driver.findElements(listItem);
                if (i >= fresh.size()) break;
                list = fresh.get(i);

                try {
                    js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", list);
                } catch (Exception ignored) {}
                ElementUtils.hoverElement(driver, list, 200);
                ElementUtils.waitFor(300);

                department = list.findElement(openPositionsPage.filtredItemDepartment);
                location   = list.findElement(openPositionsPage.filtredItemLocation);
                smallWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(department));
                smallWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(location));
            }

            String departmentText = department.getText().trim();
            String locationText   = location.getText().trim();

            softly.assertThat(departmentText)
                    .as("Department is not correct in list #" + (i + 1))
                    .containsIgnoringCase("Quality Assurance");

            softly.assertThat(locationText)
                    .as("Location is not correct in list #" + (i + 1))
                    .containsIgnoringCase("Istanbul, Turkiye");
        }
        softly.assertAll();
    }

    /** Click 'View Role' and verify redirection to Application Form Page. */
    @Step("Click View Role Button and Verify redirection to Application Form Page")
    public void clickViewRoleButtonAndVerifyRedirection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement ListItemTitle = wait.until(ExpectedConditions.presenceOfElementLocated(openPositionsPage.filtredListItemJobTitle));
        ElementUtils.hoverElement(driver, ListItemTitle);
        ElementUtils.waitFor(1000);
        
        String beforeClickUrl = driver.getCurrentUrl();
        java.util.Set<String> beforeHandles = driver.getWindowHandles();
        WebElement viewRoleButton = wait.until(ExpectedConditions.elementToBeClickable(openPositionsPage.viewRoleButton));
        ElementUtils.clickWithActions(driver, viewRoleButton);

        ElementUtils.waitForNewWindowOrUrlChange(driver, beforeHandles, beforeClickUrl, 10);

        java.util.Set<String> afterHandles = driver.getWindowHandles();
        if (afterHandles.size() > beforeHandles.size()) {
            ElementUtils.switchToNewWindow(driver, beforeHandles);
        }
        
        ElementUtils.waitForPageToBeReady(driver, 15);
        ElementUtils.waitForAjaxToFinish(driver, 15);
        
        try {
            WebElement submitButton = ScrollUtils.findElementByScrolling(
                driver,
                applicationFormPage.submitButton,
                500,
                25
            );
            
            Assertions.assertThat(submitButton.isDisplayed())
                .as("Submit button should be visible to verify the navigation is correct")
                .isTrue();
                
        } catch (NoSuchElementException e) {
            Assertions.fail("Application Form Page navigation is not correct: " + e.getMessage());
        }
    }

    @Step("BONUS Click View Role Button and Verify redirection to View Role Page")
    public void clickViewRoleButtonAndVerifyRedirectionViewRolePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement ListItemTitle = wait.until(ExpectedConditions.presenceOfElementLocated(openPositionsPage.filtredListItemJobTitle));
        ElementUtils.hoverElement(driver, ListItemTitle);
        ElementUtils.waitFor(1000);
        
        String beforeClickUrl = driver.getCurrentUrl();
        java.util.Set<String> beforeHandles = driver.getWindowHandles();
        WebElement viewRoleButton = wait.until(ExpectedConditions.elementToBeClickable(openPositionsPage.viewRoleButton));
        ElementUtils.clickWithActions(driver, viewRoleButton);

        ElementUtils.waitForNewWindowOrUrlChange(driver, beforeHandles, beforeClickUrl, 10);

        java.util.Set<String> afterHandles = driver.getWindowHandles();
        if (afterHandles.size() > beforeHandles.size()) {
            ElementUtils.switchToNewWindow(driver, beforeHandles);
        }

        ElementUtils.waitForPageToBeReady(driver, 15);
        ElementUtils.waitForAjaxToFinish(driver, 15);

        WebElement applyForThisJobButton = wait.until(ExpectedConditions.visibilityOfElementLocated(viewJobPage.applyForThisJobButton));
        Assertions.assertThat(applyForThisJobButton.isDisplayed())
                .as("View Role Page navigation is not correct").isTrue();
    }

    @Step("BONUS Verify Application Form Page Navigation and Fill the Form")
    public void verifyApplicationFormPageNavigationAndFillTheForm() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement applyForThisJobButton = wait.until(ExpectedConditions.visibilityOfElementLocated(viewJobPage.applyForThisJobButton));
        ElementUtils.hoverElement(driver, applyForThisJobButton);
        ElementUtils.clickWithActions(driver, applyForThisJobButton);

        ElementUtils.waitForPageToBeReady(driver, 15);
        ElementUtils.waitForAjaxToFinish(driver, 15);

        try {
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(applicationFormPage.nameInput));
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(applicationFormPage.emailInput));
            
            Assertions.assertThat(nameInput.isDisplayed())
                .as("Application Form Page navigation failed - required form elements not visible")
                .isTrue();
            Assertions.assertThat(emailInput.isDisplayed())
                .as("Application Form Page navigation failed - required form elements not visible")
                .isTrue();
                
        } catch (Exception e) {
            Assertions.fail("Application Form Page navigation failed - required form elements not visible: " + e.getMessage());
        }

        ElementUtils.waitFor(3000);
        
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(applicationFormPage.nameInput));
        ElementUtils.sendKeysWithActions(driver, nameInput, "Yağız YILDIRIM");
        ElementUtils.waitFor(1000);
        
        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(applicationFormPage.emailInput));
        ElementUtils.sendKeysWithActions(driver, emailInput, "yagizyildirim@yandex.com");
        ElementUtils.waitFor(1000);
        
        WebElement linkedinInput = wait.until(ExpectedConditions.elementToBeClickable(applicationFormPage.linkedinInput));
        ElementUtils.sendKeysWithActions(driver, linkedinInput, "@https://www.linkedin.com/in/yagizyildirim/");
        ElementUtils.waitFor(1000);
        
        WebElement coverLetterTextArea = wait.until(ExpectedConditions.presenceOfElementLocated(applicationFormPage.coverLetterTextArea));
        ElementUtils.clearAndSendKeys(driver, coverLetterTextArea, "HIRE ME! =)");
        ElementUtils.waitFor(3000);
    }
}