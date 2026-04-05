import org.testng.Assert;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Livestream extends ProfileSession {

    @BeforeClass
    public void getLivestream() {
        driver.get("https://www.youtube.com/");

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click show more to reveal livestream button
        WebElement searchbar = driver.findElement(By.cssSelector("input[name='search_query']"));
        String livestreamName = "Lofi Girl Livestream";
        searchbar.sendKeys(livestreamName);

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement searchIcon = driver.findElement(By.cssSelector("button[title='Search']"));
        searchIcon.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Select the livestream off the title
        WebElement livestream = driver.findElement(By.cssSelector("a[title='lofi hip hop radio 📚 beats to relax/study to']"));
        livestream.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Wait for page to load then check for an ad/popup, max 30 seconds
        handleAd(45);
    }

    @Test (priority = 1)
    public void liveChat() {

        // Wait for page to load
        try { 
            Thread.sleep(3000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Need to get messages from this user
        String username = "@ZontaTest";
        String message = "this might be my new favorite";

        // Chat runs inside in an iframe need to switch to intreact with its elements
        WebElement chatFrame = driver.findElement(By.cssSelector("iframe[id='chatframe']"));
        driver.switchTo().frame(chatFrame);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Send a message in live chat
        WebElement chatInput = driver.findElement(By.cssSelector("div#input[contenteditable]"));
        chatInput.sendKeys(message);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement sendBtn = driver.findElement(By.cssSelector("button[aria-label='Send']"));
        sendBtn.click();

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify message appears in the chat
        WebElement userMessages = driver.findElement(By.xpath("//yt-live-chat-text-message-renderer[.//span[@id='author-name' and contains(text(),'" + username + "')]]"));

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String actualMessage = userMessages.findElement(By.cssSelector("span[id='message']")).getText();

        // Switch back to the main page once done
        driver.switchTo().defaultContent();
        Assert.assertEquals(message, actualMessage);
    }

    @Test (priority = 2)
    public void toggleFeatures() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement chatFrame = driver.findElement(By.cssSelector("iframe[id='chatframe']"));
        driver.switchTo().frame(chatFrame);

        // Toggle the "Show timestamps" feature in live chat settings
        WebElement optionsBtn = driver.findElement(By.cssSelector("button[aria-label='More options']"));
        optionsBtn.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //Turns on timestamps
        WebElement timestampToggle = driver.findElement(By.cssSelector("tp-yt-paper-toggle-button[aria-label='Timestamps']"));
        timestampToggle.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //Click options again
        optionsBtn.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //Turns off reactions
        WebElement reactionsToggle = driver.findElement(By.cssSelector("tp-yt-paper-toggle-button[aria-label='Reactions']"));
        reactionsToggle.click();

        // Finds state of the toggles. Have to recheck due to stale errors
        String timeState = driver.findElement(By.cssSelector("tp-yt-paper-toggle-button[aria-label='Timestamps']")).getAttribute("aria-pressed");
        String reactionState = driver.findElement(By.cssSelector("tp-yt-paper-toggle-button[aria-label='Reactions']")).getAttribute("aria-pressed");

        driver.switchTo().defaultContent();

        // Verify the toggles are in the expected state
        Assert.assertEquals("true", timeState);
        Assert.assertEquals("false", reactionState);
    }

    @Test (priority = 3)
    public void popChat() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement chatFrame = driver.findElement(By.cssSelector("iframe[id='chatframe']"));
        driver.switchTo().frame(chatFrame);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement optionsBtn = driver.findElement(By.cssSelector("button[aria-label='More options']"));
        optionsBtn.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement popOutBtn = driver.findElement(By.xpath("/html/body/yt-live-chat-app/tp-yt-iron-dropdown/div/ytd-menu-popup-renderer/tp-yt-paper-listbox/ytd-menu-service-item-renderer[2]/tp-yt-paper-item/yt-formatted-string"));
        popOutBtn.click();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }   
        
        // Create a list to hold windows handles
        List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());

        // Switch to the popout window and verify its URL
        driver.switchTo().window(windowHandles.get(1));
        String urlTitle = driver.getCurrentUrl();

         // Close the popout and switch back to the main window
        driver.close();
        driver.switchTo().window(windowHandles.get(0));

        Assert.assertEquals(true, urlTitle.contains("is_popout"));
    }

    @Test (priority = 4)
    public void createClip() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement clipBtn = driver.findElement(By.xpath("//button[@aria-label='Clip']"));
        clipBtn.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement clipTitleInput = driver.findElement(By.cssSelector("textarea[id='textarea']"));
        String title = "Test clip";
        clipTitleInput.sendKeys(title);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement shareBtn = driver.findElement(By.cssSelector("button[aria-label='Share clip']"));
        shareBtn.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Copy the share link and open it to verify the clip title
        String shareLink = driver.findElement(By.cssSelector("input[id='share-url']")).getAttribute("value");

        // Switch driver to the new tab and navigate to the share link
        driver.switchTo().newWindow(org.openqa.selenium.WindowType.TAB);
        driver.get(shareLink);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String clipTitle = driver.findElement(By.cssSelector("span[id='title']")).getText();
        Assert.assertEquals(title, clipTitle);

    }

    @Test (priority = 5)
    public void toggleChat() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement chatFrame = driver.findElement(By.cssSelector("iframe[id='chatframe']"));
        driver.switchTo().frame(chatFrame);

        // Closes the chat bax
        WebElement closeBtn = driver.findElement(By.cssSelector("button[aria-label='Close']"));
        closeBtn.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Switch back to main page
        driver.switchTo().defaultContent();

        // Verify the "Open Panel" button state becomes enabled after closing the chat
        String panelState = driver.findElement(By.xpath("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-watch-flexy/div[4]/div[1]/div/div[2]/ytd-watch-metadata/div/div[4]/div[4]/yt-video-metadata-carousel-view-model/div[2]/div/yt-carousel-item-view-model/yt-text-carousel-item-view-model/button-view-model/button")).getAttribute("aria-disabled");
        Assert.assertEquals("false", panelState);
    }
}