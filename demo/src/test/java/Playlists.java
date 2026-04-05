import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Playlists extends ProfileSession {

    @BeforeClass
    public void openYoutube() {
        driver.get("https://www.youtube.com/");
    }

    @Test (priority = 1)
    public void createPlaylist() {

        // Search for a video to create the playlist
        WebElement searchbar = driver.findElement(By.cssSelector("input[name='search_query']"));
        String videoName = "Selenium WebDriver Tutorial";
        searchbar.sendKeys(videoName);

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

        // Hover over the video card to make the action button visible, matched by partial title
        WebElement videoCard = driver.findElement(By.xpath("//ytd-video-renderer[.//a[@title='Python Selenium Tutorial - Automate Websites and Create Bots']]"));
        new Actions(driver).moveToElement(videoCard).perform();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Click the action button on that specific video
        WebElement actionBtn = videoCard.findElement(By.cssSelector("button[aria-label='Action menu']"));
        actionBtn.click();

        // Click save to playlist
        WebElement saveBtn = driver.findElement(By.xpath("//yt-formatted-string[text()='Save to playlist']"));
        saveBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click new playlist
        WebElement newBtn = driver.findElement(By.cssSelector("button[aria-label='Create new playlist']"));
        newBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Name the playlist 
        WebElement nameField = driver.findElement(By.cssSelector("textarea[class='ytStandardsTextareaShapeTextarea'"));
        String playlistName = "Selenium Tutorials";
        nameField.sendKeys(playlistName);

        try { 
            Thread.sleep(3000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Made it more specific because it was clicking the wrong create button
        WebElement createBtn = driver.findElement(By.cssSelector("button[aria-label='Create'][aria-disabled='false']"));
        // Had to use javascript click because selenium threw element click intercept exception
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createBtn);

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }
        
        // Navigate to the playlist section
        WebElement playlistBtn = driver.findElement(By.cssSelector("a[title='Playlists']"));
        playlistBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Check if the playlist exists by matching the h3 title attribute
        WebElement playlist = driver.findElement(By.cssSelector("h3[title='Selenium Tutorials']"));
        String title = playlist.getAttribute("title");

        Assert.assertEquals(title, "Selenium Tutorials");
    }
    
    @Test (priority = 2)
    public void addToPlaylist() {
        WebElement searchbar = driver.findElement(By.cssSelector("input[name='search_query']"));
        searchbar.clear();
        String videoName = "Selenium WebDriver Tutorial";
        searchbar.sendKeys(videoName);

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

        // Hover over the video card to make the action button visible, matched by partial title
        WebElement videoCard = driver.findElement(By.xpath("//ytd-video-renderer[.//a[@title='Java Testing with Selenium Course']]"));
        new Actions(driver).moveToElement(videoCard).perform();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Click the action button on that specific video
        WebElement actionBtn = videoCard.findElement(By.cssSelector("button[aria-label='Action menu']"));
        actionBtn.click();

        // Click save to playlist
        WebElement saveBtn = driver.findElement(By.xpath("//yt-formatted-string[text()='Save to playlist']"));
        saveBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click our existing playlist
        WebElement playlist = driver.findElement(By.xpath("//button[.//span[text()='Selenium Tutorials']]"));
        playlist.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Navigate to the playlist section
        WebElement playlistBtn = driver.findElement(By.cssSelector("a[title='Playlists']"));
        playlistBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Check the number of videos in the playlist 
        // In this case needed to get the parent card with that title and then find the video count element within that card
        // because yt-badge-shape__text is too generic and caused errors
        WebElement playlistCard = driver.findElement(By.xpath("//ytd-rich-item-renderer[.//h3[@title='Selenium Tutorials']]"));
        String videoCount = playlistCard.findElement(By.cssSelector("div[class='yt-badge-shape__text']")).getText();
        Assert.assertEquals(videoCount, "2 videos");
    }

    @Test (priority = 3)
    public void reorderPlaylist() {

        // Navigate to the playlist section
        WebElement playlistBtn = driver.findElement(By.cssSelector("a[title='Playlists']"));
        playlistBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click on the playlist to open it
        WebElement playlist = driver.findElement(By.cssSelector("h3[title='Selenium Tutorials']"));
        playlist.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Get all the draggable video handles
        List<WebElement> dragHandles = driver.findElements(By.cssSelector("div[id='index-container']"));

        // Drag the second video to the first video's position
        WebElement source = dragHandles.get(1);
        WebElement target = dragHandles.get(0);

        try { 
            Thread.sleep(2000); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Does the drag and drop action
        new Actions(driver).dragAndDrop(source, target).perform();

        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Gets the list of video by their title element 
        List<WebElement> videos = driver.findElements(By.cssSelector("span[id='video-title']"));
        // Gets the title of the first video
        String videoTitle = videos.get(0).getAttribute("title"); 


        Assert.assertEquals(videoTitle, "Java Testing with Selenium Course");
    }

    @Test (priority = 4) 
    public void removeVideo() {

        driver.get("https://www.youtube.com");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Navigate to the playlist section
        WebElement playlistBtn = driver.findElement(By.cssSelector("a[title='Playlists']"));
        playlistBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click on the playlist to open it
        WebElement playlist = driver.findElement(By.cssSelector("h3[title='Selenium Tutorials']"));
        playlist.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Gets the first video card. Different from previous test as this the parent card that contains the action button
        WebElement videoCard = driver.findElement(By.cssSelector("ytd-playlist-panel-video-renderer"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Hover to make the action button visible
        new Actions(driver).moveToElement(videoCard).perform();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement actionBtn = videoCard.findElement(By.cssSelector("button[aria-label='Action menu']"));
        actionBtn.click();

        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement removeBtn = driver.findElement(By.xpath("//yt-formatted-string[text()='Remove from playlist']"));
        removeBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt();
        }

        // Check that only 1 video is left in the playlist
        List<WebElement> videos = driver.findElements(By.cssSelector("span[id='video-title']"));
        Assert.assertEquals(videos.size(), 1);
    }

    @Test (priority = 5)
    public void deletePlaylist() {

        driver.get("https://www.youtube.com");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Navigate to the playlist section
        WebElement playlistBtn = driver.findElement(By.cssSelector("a[title='Playlists']"));
        playlistBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Get the playlist card that contains our playlist
        WebElement playlistCard = driver.findElement(By.xpath("//ytd-rich-item-renderer[.//h3[@title='Selenium Tutorials']]"));

        // Hover to make the action button visible
        new Actions(driver).moveToElement(playlistCard).perform();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement actionBtn = playlistCard.findElement(By.cssSelector("button[aria-label='More actions']"));
        actionBtn.click();

        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement deleteBtn = driver.findElement(By.xpath("//button[.//span[text()='Delete']]"));
        deleteBtn.click();

        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Verify by checking that the playlist no longer exists with that title
        // findElements does not reutrn an error because it returns a list so if the list is empty then this playlist does not exist
        List<WebElement> deletedPlaylist = driver.findElements(By.cssSelector("h3[title='Selenium Tutorials']"));
        Assert.assertTrue(deletedPlaylist.isEmpty());
    }
}
