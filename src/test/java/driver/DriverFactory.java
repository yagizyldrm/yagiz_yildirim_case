package driver;

import com.thoughtworks.gauge.AfterScenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** WebDriver factory resolving browser from env/system and managing lifecycle. */
public class DriverFactory {
    private static WebDriver driver;

    public static String browser = System.getenv("BROWSER") != null
            ? System.getenv("BROWSER")
            : System.getProperty("browser", "chrome"); // default: chrome

    public static WebDriver getDriver() {
        if (driver == null) {
            switch (browser.toLowerCase()) {
                case "firefox":
                    System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
                    firefoxOptions.addArguments("--kiosk");
                    firefoxOptions.addArguments("--disable-notifications");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                // Edge WebDriver version does not match current Edge; keep disabled until updated.
                    /*case "edge":
                    System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.setBinary("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
                    edgeOptions.addArguments("--start-maximized");
                    edgeOptions.addArguments("--disable-notifications");
                    driver = new EdgeDriver(edgeOptions);
                    break;*/

                case "chrome":
                default:
                    System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");
                    driver = new ChromeDriver(chromeOptions);
                    break;
            }
        }
        return driver;
    }
    // After each scenario, quit driver to free CPU and RAM.
    @AfterScenario
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
