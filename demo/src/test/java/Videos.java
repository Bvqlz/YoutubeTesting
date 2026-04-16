import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Videos extends ProfileSession { // parallel: extends ProfileSessionParallel

    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Verified: has transcript, comments open, standard controls
    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=8iU8LPEa4o0";

    @BeforeMethod
    public void resetVideo() {
        driver.get(VIDEO_URL); // parallel: getDriver().get(...)
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("video")));
        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        handleAd(45);
    }

    @Test(priority = 1)
    public void likeVideo() {
        // Scroll down slightly to ensure the like button is in view
        js.executeScript("window.scrollBy(0, 300);");
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Find the like button
        WebElement likeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[@aria-label='like this video along with 1,234,567 other people' or contains(@aria-label,'like this video')]")));

        // Capture state before clicking
        String beforeState = likeBtn.getAttribute("aria-pressed");

        // Use JS click to bypass any visibility issues
        js.executeScript("arguments[0].click();", likeBtn);
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Re-query to get updated state
        WebElement likeAfter = driver.findElement(
            By.xpath("//button[contains(@aria-label,'like this video')]"));
        String afterState = likeAfter.getAttribute("aria-pressed");

        Assert.assertNotEquals(beforeState, afterState,
            "Like button state did not change after clicking");
    }

    @Test(priority = 2)
    public void changeResolution() {
        WebElement player = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".html5-video-player")));
        js.executeScript("arguments[0].click();", player);
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        WebElement settingsBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("button.ytp-settings-button")));
        js.executeScript("arguments[0].click();", settingsBtn);
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        WebElement qualityOption = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[contains(@class,'ytp-menuitem-label') and text()='Quality']")));
        js.executeScript("arguments[0].click();", qualityOption);
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector(".ytp-menuitem")));
        Assert.assertTrue(options.size() > 0, "No quality options found");

        js.executeScript("arguments[0].click();", options.get(options.size() - 1));
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> menu = driver.findElements(By.cssSelector(".ytp-settings-menu"));
        boolean menuClosed = menu.isEmpty() || !menu.get(0).isDisplayed();
        Assert.assertTrue(menuClosed, "Settings menu did not close after resolution selection");
    }

    @Test(priority = 3)
    public void miniPlayer() {
        // Click the player to give it focus, then send the 'i' keyboard shortcut for miniplayer
        WebElement player = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".html5-video-player")));
        js.executeScript("arguments[0].click();", player);
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        new Actions(driver).sendKeys("i").perform();
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> miniPlayer = driver.findElements(By.cssSelector("ytd-miniplayer"));
        Assert.assertTrue(miniPlayer.size() > 0, "Miniplayer did not appear after pressing 'i'");
    }

    @Test(priority = 4)
    public void transcriptAccess() {
        // Scroll the More actions button into view
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Use JS click to bypass visibility issues
        WebElement moreBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[@aria-label='More actions']")));
        js.executeScript("arguments[0].click();", moreBtn);
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        WebElement transcriptOption = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(text(),'Show transcript')]")));
        js.executeScript("arguments[0].click();", transcriptOption);
        try { Thread.sleep(4000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        js.executeScript("window.scrollBy(0, 300);");

        List<WebElement> transcript = driver.findElements(By.cssSelector("ytd-transcript-renderer"));
        if (transcript.isEmpty()) {
            transcript = driver.findElements(By.cssSelector("ytd-engagement-panel-section-list-renderer"));
        }
        Assert.assertTrue(transcript.size() > 0, "Transcript panel did not open");
    }

    @Test(priority = 5)
    public void addComment() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 600);");
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        String comment = "Love this mix" + System.currentTimeMillis();

        WebElement commentBox = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#simplebox-placeholder")));
        commentBox.click();
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#contenteditable-root")));
        input.sendKeys(comment);
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[@aria-label='Comment']")));
        submit.click();
        try { Thread.sleep(4000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> comments = driver.findElements(
            By.xpath("//*[contains(text(),'" + comment + "')]"));
        Assert.assertTrue(comments.size() > 0, "Submitted comment was not found on page");
    }
}
