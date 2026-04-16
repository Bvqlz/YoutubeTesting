import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FilterTests extends ProfileSession { // parallel: extends ProfileSessionParallel

    // Go directly to search results instead of using the search bar
    private static final String SEARCH_URL = "https://www.youtube.com/results?search_query=intro+to+python";

    @BeforeClass
    public void navigateToSearch() {
        // Load the search results page so the suite starts from a known state
        driver.get(SEARCH_URL); // parallel: getDriver().get(...)

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Search results page loaded: " + SEARCH_URL);
    }

    // Opens the filter panel by clicking the filter icon button.
    // YouTube wraps the button inside a yt-icon-button with id="filter-button"
    // Targeting by that ID is more reliable than aria-label which can vary
    private void openFilterPanel() {
        WebElement filtersBtn = driver.findElement(By.cssSelector("#filter-button button"));
        filtersBtn.click();

        System.out.println("Filter panel opened.");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Clicks a filter option by matching the visible text inside the filter panel.
    // Each option is an <a> link inside ytd-search-filter-renderer.
    // normalize-space handles any extra whitespace around the label text
    private void clickFilterOption(String optionText) {
        WebElement option = driver.findElement(By.xpath(
            "//ytd-search-filter-renderer//a[.//yt-formatted-string[normalize-space(.)='" + optionText + "']]"
        ));

        System.out.println("Clicking filter option: " + optionText);
        option.click();

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Verifies the filter was applied by checking the URL.
    // When any filter is selected YouTube adds a "sp=" parameter to the URL.
    // This is more reliable than checking chip aria-selected which YouTube does not set consistently
    private void verifyFilterApplied(String filterName) {
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after selecting '" + filterName + "': " + currentUrl);

        Assert.assertTrue(
            currentUrl.contains("sp="),
            "Filter '" + filterName + "' was NOT applied. URL has no 'sp=' parameter: " + currentUrl
        );

        System.out.println("PASS: Filter '" + filterName + "' applied successfully.");
    }

    @Test(priority = 1)
    public void selectTypeShorts() {
        // This is for checking that the TYPE filter "Shorts" can be selected
        driver.get(SEARCH_URL);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        openFilterPanel();
        clickFilterOption("Shorts");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verifyFilterApplied("Shorts");
    }

    @Test(priority = 2)
    public void selectDurationOver20Minutes() {
        // This is for checking that the DURATION filter "Over 20 minutes" can be selected
        driver.get(SEARCH_URL);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        openFilterPanel();
        clickFilterOption("Over 20 minutes");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verifyFilterApplied("Over 20 minutes");
    }

    @Test(priority = 3)
    public void selectUploadDateThisMonth() {
        // This is for checking that the UPLOAD DATE filter "This month" can be selected
        // YouTube displays this as "This month" with a lowercase m
        driver.get(SEARCH_URL);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        openFilterPanel();
        clickFilterOption("This month");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verifyFilterApplied("This month");
    }

    @Test(priority = 4)
    public void selectFeatures4K() {
        // This is for checking that the FEATURES filter "4K" can be selected
        driver.get(SEARCH_URL);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        openFilterPanel();
        clickFilterOption("4K");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verifyFilterApplied("4K");
    }

    @Test(priority = 5)
    public void selectPrioritizePopularity() {
        // This is for checking that the PRIORITIZE filter "Popularity" can be selected
        driver.get(SEARCH_URL);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        openFilterPanel();
        clickFilterOption("Popularity");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        verifyFilterApplied("Popularity");
    }
}
