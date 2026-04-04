import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class ProfileSession {

    // A driver is created that will be shared across all test classes
    // Purpose is to for these classes to extend this one and inherit the session so repeated login does not happen and prevents any flagging from Google's end
    // Protected so that any child class can access it
    protected static WebDriver driver;

    // This is the path that stores the session data for the Chrome profile
    protected static final String session_path = "D:/selenium-youtube-profile";

    // This method runs once before any tests in the suite
    // Done this way to ensure the session is established before any test
    @BeforeSuite
    public void setUpSuite() {
        ChromeOptions options = new ChromeOptions();

        // User data directory is where Chrome stores profile data like cookies and bookmarks. This is where the logged in session will be saved and loaded from
        options.addArguments("--user-data-dir=" + session_path);
        // Since Chrome can have mulitiple profiles this specifies to the default
        options.addArguments("--profile-directory=Default");

        // Disable blink features turns off various automation flags for testing purposes 
        // Important as it prevents Google's bot detection from flagging our session
        // https://stackoverflow.com/questions/76789600/is-disable-blink-features-automationcontrolled-supposed-to-set-navigator-web
        options.addArguments("--disable-blink-features=AutomationControlled");
        // Finds the enable-automation flag and passes this list to exclude switches. Important for reason above
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        // Sets the useAutomationExtension to false to prevent the automation extension from being loaded. Important for reason above
        options.setExperimentalOption("useAutomationExtension", false);

        // Blocks the browser notification popups 
        options.addArguments("--disable-notifications");

        // Blocks Chrome popups
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        login();
    }

    // This runs once the test classes have finished 
    // Stops the driver and closes the browser
    @AfterSuite
    public void tearDownSuite() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void login() {
        driver.get("https://www.youtube.com");

        // This is a simple check to see if a session is already created
        boolean isLoggedIn = !driver.findElements(By.cssSelector("button#avatar-btn")).isEmpty();
        
        if (isLoggedIn) {
            System.out.println("Already signed in.");
            return;
        }

        // Loads credentials from the properties file. Think of this as a environment variable but for Java
        Properties credentials = loadCredentials();
        String email = credentials.getProperty("youtube.email");
        String password = credentials.getProperty("youtube.password");

        driver.get("https://accounts.google.com/signin");

        // Sleeps are needed to delay instant actions. Tries to emulate human behavior
        try { 
            Thread.sleep(2000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Sends email
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email']"));
        emailField.sendKeys(email);
        driver.findElement(By.id("identifierNext")).click();

        try { 
            Thread.sleep(3000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Sends password
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
        passwordField.sendKeys(password);
        driver.findElement(By.id("passwordNext")).click();

        System.out.println("Waiting for login");

        try { 
            Thread.sleep(60000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt();
        }

        // After logging in switch browser to youtube to confirm login with same check above
        driver.get("https://www.youtube.com");

        try { 
            Thread.sleep(60000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt();
        }

        boolean loggedInAfterWait = !driver.findElements(By.cssSelector("button#avatar-btn")).isEmpty();

        if (loggedInAfterWait) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Could not log in.");
        }
    }

    private Properties loadCredentials() {
        // This properties object holds any key value pairs from the environment file that
        Properties props = new Properties(); 
        // Input is used to read the file and get the credentials
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("credentials.properties")) {

            // Debug if file was not found
            if (input == null) {
                throw new RuntimeException("credentials.properties not found.");
            }

            // loads the properties into this object 
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load credentials: " + e.getMessage());
        }

        return props;
    }
}
