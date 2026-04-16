import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Channel  extends ProfileSession { // parallel: extends ProfileSessionParallel

    @BeforeClass
    public void openYoutube() {
        driver.get("https://www.youtube.com/"); // parallel: getDriver().get(...)
    }


    @Test (priority = 1)
    public void subscribe() {
        
        WebElement searchbar = driver.findElement(By.cssSelector("input[name='search_query']"));
        String channelName = "FreeCodeCamp";
        searchbar.sendKeys(channelName);

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

        // Click on the channel card
        WebElement channelCard = driver.findElement(By.cssSelector("a[class='channel-link yt-simple-endpoint style-scope ytd-channel-renderer']"));
        channelCard.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click on the subscribe button
        WebElement subscribeBtn = driver.findElement(By.xpath("//button[.//div[text()='Subscribe']]"));
        subscribeBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Verify subscription
        WebElement subscribedBtn = driver.findElement(By.xpath("//button[.//div[text()='Subscribed']]"));
        String btnText = subscribedBtn.getText();

        Assert.assertEquals("Subscribed", btnText);
    }

    @Test (priority = 2)
    public void subscriptionFeed() {

        // Although this test may appear redundant to the previous one we need to verify if after subscribing to a channel it appears in a users feed
        // Navigate to the subscription feed
        WebElement subscriptionFeedBtn = driver.findElement(By.cssSelector("a[title='Subscriptions']"));
        subscriptionFeedBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Verify that the channel appears in the subscription feed
        // If it can select a video from the channel that was just subscribed to then it means that the channel appears in the subscription feed
        String channelName = driver.findElement(By.cssSelector("a[href='/@freecodecamp']")).getText();
        Assert.assertEquals(channelName, "freeCodeCamp.org");
    }

    @Test (priority = 3)
    public void toggleNotifications() {
        WebElement searchbar = driver.findElement(By.cssSelector("input[name='search_query']"));
        searchbar.clear();
        String channelName = "FreeCodeCamp";
        searchbar.sendKeys(channelName);

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

        // Click on the channel card
        WebElement channelCard = driver.findElement(By.cssSelector("a[class='channel-link yt-simple-endpoint style-scope ytd-channel-renderer']"));
        channelCard.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement notificationsBell = driver.findElement(By.xpath("//button[.//div[text()='Subscribed']]"));
        notificationsBell.click();

        try { 
            Thread.sleep(1500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click All
        WebElement allOption = driver.findElement(By.xpath("//yt-list-item-view-model[@role='menuitemradio'][.//span[text()='All']]"));
        allOption.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Reopen menu to read the state after clicking All
        notificationsBell.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String allState = driver.findElement(By.xpath("//yt-list-item-view-model[@role='menuitemradio'][.//span[text()='All']]")).getAttribute("aria-checked");

        // Click None
        WebElement noneOption = driver.findElement(By.xpath("//yt-list-item-view-model[@role='menuitemradio'][.//span[text()='None']]"));
        noneOption.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Reopen menu to read the state after clicking None
        notificationsBell.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String noneState = driver.findElement(By.xpath("//yt-list-item-view-model[@role='menuitemradio'][.//span[text()='None']]")).getAttribute("aria-checked");

        // Close the menu
        notificationsBell.click();

        Assert.assertEquals("true", allState);
        Assert.assertEquals("true", noneState);
    }

    @Test (priority = 4)
    void communityPosts() {
        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement postsTab = driver.findElement(By.cssSelector("yt-tab-shape[tab-title='Posts']"));
        postsTab.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement postCard = driver.findElement(By.cssSelector("ytd-backstage-post-renderer"));
        WebElement likeBtn = postCard.findElement(By.cssSelector("button[aria-label*='Like this post']"));
        
        likeBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }


        // Verify that the like button was clicked
        String ariaPressed = likeBtn.getAttribute("aria-pressed");
        Assert.assertEquals(ariaPressed, "true");
    }

    @Test (priority = 5)
    void unsubscribe() {
        WebElement homeTab = driver.findElement(By.cssSelector("yt-tab-shape[tab-title='Home']"));
        homeTab.click();
        
        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        WebElement subscribedBtn = driver.findElement(By.xpath("//button[.//div[text()='Subscribed']]"));
        subscribedBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Click unsubscribe from the list
        WebElement unsubscribeBtn = driver.findElement(By.xpath("//yt-list-item-view-model[@role='menuitem'][.//span[text()='Unsubscribe']]"));
        unsubscribeBtn.click();
        
        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Clicks the unsubscribe button in the confirmation popup
        WebElement confirmUnsubscribeBtn = driver.findElement(By.cssSelector("button[aria-label='Unsubscribe']"));
        confirmUnsubscribeBtn.click();

        try { 
            Thread.sleep(2500); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }

        // Verify user is unsubscribed
        WebElement subscribeBtn = driver.findElement(By.xpath("//button[.//div[text()='Subscribe']]"));
        String btnText = subscribeBtn.getText();

        Assert.assertEquals(btnText, "Subscribe");
    }
}