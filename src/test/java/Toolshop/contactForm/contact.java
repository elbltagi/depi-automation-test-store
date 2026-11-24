package Toolshop.contactForm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class contact {
    WebDriver driver ;
    WebDriverWait wait;
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
//    @Test
//    public void navigatingTocontact (){
//
//    }

}
