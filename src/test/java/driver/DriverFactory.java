package driver;

import com.thoughtworks.gauge.AfterScenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.ConfigReader;

/** WebDriver factory resolving browser from config/env/system and managing lifecycle. */
public class DriverFactory {
    private static WebDriver driver;

    public static String browser = System.getenv("BROWSER") != null
            ? System.getenv("BROWSER")
            : System.getProperty("browser", ConfigReader.getProperty("default.browser", "chrome"));

    public static WebDriver getDriver() {
        if (driver == null) {
            switch (browser.toLowerCase()) {
                case "firefox":
                    String geckoDriverPath = ConfigReader.getProperty("webdriver.firefox.driver", "drivers/geckodriver.exe");
                    System.setProperty("webdriver.gecko.driver", geckoDriverPath);
                    
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    
                    String firefoxBinary = ConfigReader.getProperty("browser.firefox.binary");
                    if (firefoxBinary != null && !firefoxBinary.isEmpty()) {
                        firefoxOptions.setBinary(firefoxBinary);
                    }
                    
                    firefoxOptions.addArguments("--kiosk");
                    firefoxOptions.addArguments("--disable-notifications");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case "chrome":
                default:
                    String chromeDriverPath = ConfigReader.getProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
                    System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                    
                    ChromeOptions chromeOptions = new ChromeOptions();
                    
                    String chromeBinary = ConfigReader.getProperty("browser.chrome.binary");
                    if (chromeBinary != null && !chromeBinary.isEmpty()) {
                        chromeOptions.setBinary(chromeBinary);
                    }
                    
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
