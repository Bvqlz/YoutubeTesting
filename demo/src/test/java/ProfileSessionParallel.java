import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

// Uses ThreadLocal so each test class running in parallel gets its own WebDriver instance.
// @BeforeClass/@AfterClass replace the @BeforeSuite/@AfterSuite so that every class
public class ProfileSessionParallel {

    // ThreadLocal gives every thread its own driver
    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();

    // Helper used by subclasses to access the driver for the current thread
    protected WebDriver getDriver() {
        return driverHolder.get();
    }

    @BeforeClass
    public void setUpClass() {
        ChromeOptions options = new ChromeOptions();

        // Profile args are intentionally removed for parallel mode.
        // Chrome locks the profile directory to one process at a time — using the same
        // path across threads means only the first browser loads the session and the rest
        // start blank. Each parallel thread gets its own fresh Chrome instance instead.
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-notifications");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driverHolder.set(driver);

        // Navigate to YouTube so every parallel browser opens on the same starting page
        driver.get("https://www.youtube.com");
    }

    @AfterClass
    public void tearDownClass() {
        WebDriver driver = driverHolder.get();
        if (driver != null) {
            driver.quit();
            driverHolder.remove();
        }
    }

    protected void handleAd(int maxWaitSeconds) {
        WebDriver driver = getDriver();

        List<WebElement> popup = driver.findElements(By.cssSelector("button[aria-label='No thanks']"));
        if (!popup.isEmpty()) {
            try {
                popup.get(0).click();
            } catch (ElementNotInteractableException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", popup.get(0));
            }
            System.out.println("Dismissed popup.");
        }

        boolean adPlaying = !driver.findElements(By.cssSelector("div.ad-showing")).isEmpty();

        if (!adPlaying) {
            System.out.println("No ad detected.");
            return;
        }

        System.out.println("Ad detected. Waiting for skip");

        int intervals = maxWaitSeconds / 5;

        for (int i = 0; i < intervals; i++) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            List<WebElement> skipButton = driver.findElements(By.cssSelector("button.ytp-skip-ad-button"));
            if (!skipButton.isEmpty()) {
                try {
                    skipButton.get(0).click();
                } catch (ElementNotInteractableException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", skipButton.get(0));
                }
                System.out.println("Ad skipped.");
                return;
            }

            boolean stillPlaying = !driver.findElements(By.cssSelector("div.ad-showing")).isEmpty();
            if (!stillPlaying) {
                System.out.println("Ad finished. Checking for another ad");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (!driver.findElements(By.cssSelector("div.ad-showing")).isEmpty()) {
                    System.out.println("Another ad detected.");
                    handleAd(maxWaitSeconds);
                }
                return;
            }
        }
        System.out.println("Ad wait time exceeded.");
    }

}