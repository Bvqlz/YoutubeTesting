import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ShortsTests extends ProfileSession { // parallel: extends ProfileSessionParallel

    private static final String SHORTS_URL = "https://www.youtube.com/shorts/_3Evgblmfwo";
    private WebDriverWait wait;

    @BeforeClass
    public void openShort() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // parallel: getDriver()

        // Load the Short once — all tests run continuously from this single load
        driver.get(SHORTS_URL);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Handle any ad or YouTube Premium popup before tests run
        handleAd(45);
    }

    @Test(priority = 1)
    public void pauseAndResume() {
        // This is for checking that the user can pause and resume the Short

        // Click the player as a required user gesture so Chrome allows JS playback control
        WebElement player = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shorts-player")));
        new Actions(driver).click(player).perform();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement video = driver.findElement(By.cssSelector("video"));

        // Pause the video
        ((JavascriptExecutor) driver).executeScript("arguments[0].pause();", video);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Boolean isPaused = (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].paused", video);
        System.out.println("Video paused: " + isPaused);
        Assert.assertTrue(isPaused, "Video should be paused");

        // Resume the video
        ((JavascriptExecutor) driver).executeScript("arguments[0].play();", video);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Boolean isPlaying = (Boolean) ((JavascriptExecutor) driver).executeScript("return !arguments[0].paused", video);
        System.out.println("Video playing: " + isPlaying);
        Assert.assertTrue(isPlaying, "Video should be playing after resuming");
    }

    @Test(priority = 2)
    public void likeShort() {
        // This is for checking that the user can like a Short and the button state changes
        WebElement likeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//like-button-view-model//button[@aria-label]")));

        String stateBefore = likeBtn.getAttribute("aria-pressed");
        likeBtn.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String stateAfter = likeBtn.getAttribute("aria-pressed");
        System.out.println("Like state changed from '" + stateBefore + "' to '" + stateAfter + "'");

        Assert.assertNotEquals(stateAfter, stateBefore, "Like button state should change after clicking");
    }

    @Test(priority = 3)
    public void muteAndUnmute() {
        // This is for checking that clicking the volume icon at the top of the Short mutes and unmutes it

        // Hover over the player to reveal controls, move directly to the mute button and click
        // all in one chained action so the mouse never leaves and controls don't fade
        WebElement player = driver.findElement(By.id("shorts-player"));
        WebElement muteBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@aria-label, 'Mute') or contains(@aria-label, 'Unmute')]")));

        String labelBefore = muteBtn.getAttribute("aria-label");
        System.out.println("Volume button state before click: " + labelBefore);

        new Actions(driver).moveToElement(player).moveToElement(muteBtn).click().perform();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String labelAfterMute = muteBtn.getAttribute("aria-label");
        System.out.println("Volume button state after mute click: " + labelAfterMute);
        Assert.assertNotEquals(labelAfterMute, labelBefore, "Volume button label should change after clicking to mute");

        // Click again to unmute — hover chain again so controls are visible
        new Actions(driver).moveToElement(player).moveToElement(muteBtn).click().perform();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String labelAfterUnmute = muteBtn.getAttribute("aria-label");
        System.out.println("Volume button state after unmute click: " + labelAfterUnmute);
        Assert.assertEquals(labelAfterUnmute, labelBefore, "Volume button label should return to original state after unmuting");

        System.out.println("PASS: Mute and unmute via volume icon button.");
    }

    @Test(priority = 4)
    public void openCommentSection() {
        // This is for checking that clicking the Comments button (speech bubble, right panel) opens the comments
        // The aria-label includes the count e.g. "376 comments", so we use contains()
        WebElement commentsBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label, 'omment')]")));
        commentsBtn.click();

        System.out.println("Clicked the Comments button.");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // The comments panel slides in from the right — wait for it to be visible
        WebElement commentsPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ytd-engagement-panel-section-list-renderer[target-id='engagement-panel-comments-section']")));
        Assert.assertTrue(commentsPanel.isDisplayed(), "Comments panel should be visible");

        System.out.println("PASS: Comments panel is visible.");
    }

    @Test(priority = 5)
    public void clickShareButton() {
        // This is for checking that the Share button (right panel, below Comments) opens the share dialog

        // Close the comments panel left open by openCommentSection (priority 4)
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // There can be multiple Share buttons on the page (some hidden in menus).
        // Find all of them and click the first one that is actually visible on screen.
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[@aria-label='Share']")));

        java.util.List<WebElement> shareBtns = driver.findElements(By.xpath("//button[@aria-label='Share']"));
        WebElement visibleShareBtn = null;
        for (WebElement btn : shareBtns) {
            if (btn.isDisplayed()) {
                visibleShareBtn = btn;
                break;
            }
        }

        Assert.assertNotNull(visibleShareBtn, "A visible Share button should exist on the page");

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", visibleShareBtn);
        System.out.println("Clicked the visible Share button.");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify the share dialog appeared by checking for the Copy button inside it
        WebElement copyBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[aria-label='Copy']")));
        Assert.assertTrue(copyBtn.isDisplayed(), "Share dialog should be visible with a Copy button");

        System.out.println("PASS: Share dialog opened.");

        // Close the share dialog by pressing Escape — more reliable than finding the X button
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Wait for the Copy button to disappear (confirms dialog is closed)
        wait.until(ExpectedConditions.invisibilityOf(copyBtn));

        System.out.println("Share dialog closed.");
    }
}