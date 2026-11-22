package Toolshop.catalog;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.testng.annotations.Test;

public class filters {
    WebDriver driver ;
    @BeforeMethod
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practicesoftwaretesting.com/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Wait for the product list to load before starting search/filter tests.
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("a.card")));
    }
    @Test
    public void Search_with_a_valid_term (){
        driver.findElement(By.id("search-query")).sendKeys("Long Nose Pliers");
        driver.findElement(By.cssSelector("[data-test='search-submit']")).click();
        String actual_result = driver.findElement(By.cssSelector("[data-test='search-caption']")).getText();
        String expected_result = "Searched for: Long Nose Pliers";
        Assert.assertEquals(actual_result,expected_result);
    }
    @Test
    public void Search_with_a_partial_term(){
        driver.findElement(By.id("search-query")).sendKeys("Pli");
        driver.findElement(By.cssSelector("[data-test='search-submit']")).click();
        String actual_result = driver.findElement(By.cssSelector("[data-test='search-caption']")).getText();
        String expected_result = "Searched for: Pli";
        Assert.assertEquals(actual_result,expected_result);
    }
    @Test
    public void Search_with_no_results(){
        driver.findElement(By.id("search-query")).sendKeys("abdo");
        driver.findElement(By.cssSelector("[data-test='search-submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By productCards = By.cssSelector("a.card");
        wait.until(ExpectedConditions.numberOfElementsToBe(productCards, 0));
        Assert.assertTrue(driver.findElements(productCards).isEmpty(), "Search returned products for a term with no matches");
    }
    @Test
    public void Sort_by_name_A_Z(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldTitle = driver.findElement(By.cssSelector(".card-title")).getText();
        driver.findElement(By.cssSelector("[data-test='sort']")).click();
        driver.findElement(By.cssSelector("[value='name,asc']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector(".card-title"), oldTitle)));
        String first_sort_element = driver.findElement(By.cssSelector("[data-test='product-name']")).getText();
    String expected_element = "Adjustable Wrench";
    Assert.assertEquals(first_sort_element,expected_element);
    }
    @Test
    public void Sort_by_name_Z_A(){
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String oldTitle = driver.findElement(By.cssSelector(".card-title")).getText();
            driver.findElement(By.cssSelector("[data-test='sort']")).click();
            driver.findElement(By.cssSelector("[value='name,desc']")).click();
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector(".card-title"), oldTitle)));
            String first_sort_element = driver.findElement(By.cssSelector("[data-test='product-name']")).getText();
            String expected_element = "Wood Saw";
            Assert.assertEquals(first_sort_element, expected_element);
    }
    @Test
    public void Sort_by_price_ascending(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldTitle = driver.findElement(By.cssSelector(".card-title")).getText();
        driver.findElement(By.cssSelector("[data-test='sort']")).click();
        driver.findElement(By.cssSelector("[value='price,asc']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector(".card-title"), oldTitle)));
        String first_sort_element = driver.findElement(By.cssSelector("[data-test='product-name']")).getText();
        String expected_element = "Washers";
        Assert.assertEquals(first_sort_element,expected_element);

    }
    @Test
    public void Sort_by_price_descending(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldTitle = driver.findElement(By.cssSelector(".card-title")).getText();
        driver.findElement(By.cssSelector("[data-test='sort']")).click();
        driver.findElement(By.cssSelector("[value='price,desc']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector(".card-title"), oldTitle)));
        String first_sort_element = driver.findElement(By.cssSelector("[data-test='product-name']")).getText();
        String expected_element = "Drawer Tool Cabinet";
        Assert.assertEquals(first_sort_element,expected_element);

    }
    @Test
    public void Filter_by_category(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldTitle = driver.findElement(By.cssSelector(".card-title")).getText();
        driver.findElement(By.cssSelector("[data-test='category-01M81B64F0Q4TNYEA7HR2Z9XVH']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector(".card-title"), oldTitle)));
        String first_sort_element = driver.findElement(By.cssSelector("[data-test='product-name']")).getText();
        String expected_element = "Claw Hammer with Shock Reduction Grip";
        Assert.assertEquals(first_sort_element,expected_element);

    }
    @Test
    public void Filter_by_brand(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldTitle = driver.findElement(By.cssSelector(".card-title")).getText();
        driver.findElement(By.cssSelector("[data-test='brand-01M81B643H949N1W0NY7V58ZFH']")).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector(".card-title"), oldTitle)));
        String first_sort_element = driver.findElement(By.cssSelector("[data-test='product-name']")).getText();
        String expected_element = "Claw Hammer with Shock Reduction Grip";
        Assert.assertEquals(first_sort_element,expected_element);
    }
    @AfterMethod
    public void end (){
        driver.quit();
    }

}

