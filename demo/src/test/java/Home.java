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

public class Home extends ProfileSession {

    private WebDriverWait wait;

    @BeforeMethod
    public void resetHome() throws InterruptedException {
        driver.get("https://www.youtube.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Wait until videos are actually loaded before each test
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector("ytd-rich-item-renderer")));
    }

    @Test(priority = 1)
    public void quickPreview() {
        List<WebElement> videos = driver.findElements(By.cssSelector("ytd-rich-item-renderer"));
        WebElement firstVideo = videos.get(0);

        new Actions(driver).moveToElement(firstVideo).perform();

        // YouTube renders the preview video in a global overlay, not inside the card.
        // Search from document root, not firstVideo.
        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> preview = driver.findElements(By.cssSelector("video"));
        Assert.assertTrue(preview.size() > 0, "No video preview element found after hover");
    }

    @Test(priority = 2)
    public void logoNavigation() {
        // Click the YouTube logo
        WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a#logo")));
        logo.click();

        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Verify we are on the home page
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.equals("https://www.youtube.com/") || url.contains("youtube.com/?"),
            "Logo did not navigate to YouTube home page. URL was: " + url);

        // Verify videos are still present
        List<WebElement> videos = driver.findElements(By.cssSelector("ytd-rich-item-renderer"));
        Assert.assertTrue(videos.size() > 0, "No videos found after navigating via logo");
    }

    @Test(priority = 3)
    public void sidebarToggle() {
        WebElement menuBtn = driver.findElement(By.cssSelector("button[aria-label='Guide']"));

        // Store the initial sidebar state before toggling
        List<WebElement> before = driver.findElements(By.cssSelector("ytd-guide-renderer"));
        boolean sidebarWasOpen = !before.isEmpty() && before.get(0).isDisplayed();

        // First toggle — state should flip
        menuBtn.click();
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> afterFirst = driver.findElements(By.cssSelector("ytd-guide-renderer"));
        boolean afterFirstOpen = !afterFirst.isEmpty() && afterFirst.get(0).isDisplayed();
        Assert.assertNotEquals(sidebarWasOpen, afterFirstOpen, "Sidebar state did not change after first toggle");

        // Second toggle — state should return to original
        menuBtn.click();
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> afterSecond = driver.findElements(By.cssSelector("ytd-guide-renderer"));
        boolean afterSecondOpen = !afterSecond.isEmpty() && afterSecond.get(0).isDisplayed();
        Assert.assertEquals(sidebarWasOpen, afterSecondOpen, "Sidebar did not restore to original state");
    }

    @Test(priority = 4)
    public void categoryChips() {
        List<WebElement> chips = driver.findElements(By.cssSelector("yt-chip-cloud-chip-renderer"));
        Assert.assertTrue(chips.size() > 0, "No category chips found");

        WebElement firstChip = chips.get(0);
        firstChip.click();

        try { Thread.sleep(2500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<WebElement> videos = driver.findElements(By.cssSelector("ytd-rich-item-renderer"));
        Assert.assertTrue(videos.size() > 0, "No videos loaded after chip click");
    }

    @Test(priority = 5)
    public void infiniteScroll() {
        List<WebElement> beforeScroll = driver.findElements(By.cssSelector("ytd-rich-item-renderer"));
        int initialCount = beforeScroll.size();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll incrementally rather than jumping to bottom — triggers YouTube's scroll listener
        for (int i = 0; i < 8; i++) {
            js.executeScript("window.scrollBy(0, window.innerHeight);");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            List<WebElement> current = driver.findElements(By.cssSelector("ytd-rich-item-renderer"));
            if (current.size() > initialCount) break;
        }

        List<WebElement> afterScroll = driver.findElements(By.cssSelector("ytd-rich-item-renderer"));
        int newCount = afterScroll.size();

        Assert.assertTrue(newCount > initialCount,
            "Expected more videos after scroll. Before: " + initialCount + ", After: " + newCount);
    }
}
