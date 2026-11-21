package Toolshop.Data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
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
        WebElement noResults = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='no-results']")));
        String actual_result = driver.findElement(By.cssSelector("[data-test='no-results']")).getText();
        String expected_result = "There are no products found.";
        Assert.assertEquals(actual_result,expected_result);
    }




}

