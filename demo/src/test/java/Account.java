import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Account extends ProfileSession { // parallel: extends ProfileSessionParallel

    @BeforeClass
    public void openYoutube() {
        driver.get("https://www.youtube.com/"); // parallel: getDriver().get(...)
    }

    @Test (priority = 1)
    public void changeTheme() {

        //Get the account button 
        WebElement accountBtn = driver.findElement(By.cssSelector("button[aria-label='Account menu']"));
        accountBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the apperance button on the list
        WebElement appearanceBtn = driver.findElement(By.xpath("//ytd-toggle-theme-compact-link-renderer[.//div[text()='Appearance: Device theme']]"));
        appearanceBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the dark theme button
        WebElement darkBtn = driver.findElement(By.xpath("//ytd-compact-link-renderer[.//yt-formatted-string[text()='Dark theme']]"));
        darkBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify the theme changed
        accountBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String appearanceText = driver.findElement(By.xpath("//ytd-toggle-theme-compact-link-renderer[.//div[contains(text(),'Appearance')]]")).getText();
        Assert.assertEquals(appearanceText, "Appearance: Dark");
    }

    @Test (priority = 2)
    public void changeLanguage() {
        driver.get("https://www.youtube.com/");

        // Get the account button
        WebElement accountBtn = driver.findElement(By.cssSelector("button[aria-label='Account menu']"));
        accountBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the language button on the list 
        WebElement languageBtn = driver.findElement(By.xpath("//ytd-compact-link-renderer[.//yt-formatted-string[@id='subtitle' and text()='English']]"));
        languageBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the UK english button
        WebElement ukBtn = driver.findElement(By.xpath("//ytd-compact-link-renderer[.//yt-formatted-string[text()='English (UK)']]"));
        ukBtn.click();

        // Longer wait as youtube refreshes the page after changing the language
        try {
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify the language changed. Need to redefine due to reload
        WebElement accountBtn2 = driver.findElement(By.cssSelector("button[aria-label='Account menu']"));
        accountBtn2.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String languageText = driver.findElement(By.xpath("//ytd-compact-link-renderer//yt-formatted-string[@id='subtitle' and text()='British English']")).getText();
        Assert.assertEquals(languageText, "British English");
    }

    @Test (priority = 3)
    public void toggleCaptions() {
        driver.get("https://www.youtube.com/");

        // Get the account button
        WebElement accountBtn = driver.findElement(By.cssSelector("button[aria-label='Account menu']"));
        accountBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the settings button on the list
        WebElement settingsBtn = driver.findElement(By.xpath("//ytd-compact-link-renderer[.//yt-formatted-string[text()='Settings']]"));
        settingsBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the playback and performance button
        WebElement playbackBtn = driver.findElement(By.xpath("//ytd-compact-link-renderer[.//yt-formatted-string[text()='Playback and performance']]"));
        playbackBtn.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the captions toggle checkbox
        WebElement captionsToggle = driver.findElement(By.cssSelector("tp-yt-paper-checkbox[aria-label='Include auto-generated captions (when available)']"));
        captionsToggle.click();

        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String captionsState = captionsToggle.getAttribute("aria-checked");
        Assert.assertEquals(captionsState, "true");
    }

    @Test (priority = 4)
    public void toggleSettings() {
        // Go to the notifications tab
        WebElement notificationsTab = driver.findElement(By.xpath("//ytd-compact-link-renderer[.//yt-formatted-string[text()='Notifications']]"));
        notificationsTab.click();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the subscriptions notifications toggle checkbox
        WebElement subscriptionsToggle = driver.findElement(By.cssSelector("tp-yt-paper-toggle-button[aria-label='Subscriptions']"));
        subscriptionsToggle.click();
        String subscriptionsState = subscriptionsToggle.getAttribute("aria-pressed");

        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the activity on channel toggle
        WebElement activityToggle = driver.findElement(By.cssSelector("tp-yt-paper-toggle-button[aria-label='Activity on my channel']"));
        activityToggle.click();
        String activityState = activityToggle.getAttribute("aria-pressed");

        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on mentions toggle
        WebElement mentionsToggle = driver.findElement(By.cssSelector("tp-yt-paper-toggle-button[aria-label='Mentions']"));
        mentionsToggle.click();
        String mentionsState = mentionsToggle.getAttribute("aria-pressed");

        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify these where disabled
        Assert.assertEquals(subscriptionsState, "false");
        Assert.assertEquals(activityState, "false");
        Assert.assertEquals(mentionsState, "false");
    }

    @Test (priority = 5)
    public void pauseHistory() {
        driver.get("https://www.youtube.com/");

        WebElement historyBtn = driver.findElement(By.cssSelector("a[title='History']"));
        historyBtn.click();

        try { 
            Thread.sleep(2000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click the pause history button
        WebElement pauseBtn = driver.findElement(By.cssSelector("button[aria-label='Pause watch history']"));
        pauseBtn.click();

        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click the confirm pause button in the popup
        WebElement confirmPauseBtn = driver.findElement(By.cssSelector("button[aria-label='Pause']"));
        confirmPauseBtn.click();

        // Verify by extracting the value of the arial label
        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement resumeBtn = driver.findElement(By.cssSelector("button[aria-label='Turn on watch history']"));
        String btnText = resumeBtn.getAttribute("aria-label");
        Assert.assertEquals(btnText, "Turn on watch history");
    }
}
