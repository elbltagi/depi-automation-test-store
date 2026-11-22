package Toolshop.end;

import Toolshop.Checkout.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class end_to_end extends Base {
    WebDriver driver ;
    WebDriverWait wait;
    @BeforeTest
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practicesoftwaretesting.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // Wait for the product list to load before starting search/filter tests.
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("a.card")));
    }

    @Test(priority = 0)
    public void Search_with_a_valid_term (){

        driver.findElement(By.id("search-query")).sendKeys("Slip Joint Pliers");
        driver.findElement(By.cssSelector("[data-test='search-submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("a.card")));
        String actual_result = driver.findElement(By.cssSelector("[data-test='search-caption']")).getText();
        String expected_result = "Searched for: Slip Joint Pliers";
        Assert.assertEquals(actual_result,expected_result);
    }
    @Test (priority = 1)
    public void navigate_to_product (){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By product = (By.cssSelector("[alt='Slip Joint Pliers']"));
        wait.until(ExpectedConditions.elementToBeClickable(product)).click();
    }
    @Test (priority = 2)
    public void add_to_cart (){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By btn = (By.id("btn-add-to-cart"));
        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
    }
    @Test(priority = 3)
    public void navigate_to_cart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By cart_btn = By.cssSelector("[data-test='nav-cart']");
        wait.until(ExpectedConditions.elementToBeClickable(cart_btn)).click();
    }
    @Test(priority = 4)
    public void process_to_checkout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By cart_btn = By.cssSelector("[data-test='proceed-1']");
        wait.until(ExpectedConditions.elementToBeClickable(cart_btn)).click();
    }
    @Test (priority = 5)
    void signing_in_and_navigating_to_checkout (){
     driver.findElement(By.id("email")).sendKeys("customer3@practicesoftwaretesting.com");
     driver.findElement(By.id("password")).sendKeys("pass123");
     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
     By login_btn = By.cssSelector("[data-test='login-submit']");
     wait.until(ExpectedConditions.elementToBeClickable(login_btn)).click();
     By process_to_checkout_btn = By.cssSelector("[data-test='proceed-2']");
     wait.until(ExpectedConditions.elementToBeClickable(process_to_checkout_btn)).click();


        // customer@practicesoftwaretesting.com
        // welcome01
    }
    @Test (priority = 6)
    public void billing (){
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> !d.findElement(By.id("street")).getAttribute("value").isEmpty());
        WebElement countryDrobdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("country")));
        driver.findElement(By.cssSelector("[value='EG']")).click();
        Select selcet_country = new Select(countryDrobdown);
        selcet_country.selectByValue("EG");
        driver.findElement(By.cssSelector("[data-test='postal_code']")).sendKeys("123");
        driver.findElement(By.cssSelector("[data-test='house_number']")).sendKeys("2");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-3']"))).click();

    }
    @Test (priority = 7)
    public void payment (){
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(d -> !d.findElement(By.id("street")).getAttribute("value").isEmpty());
        WebElement paymentDrobdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("payment-method")));
        Select payment_method = new Select(paymentDrobdown);
        payment_method.selectByValue("cash-on-delivery");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']"))).click();
        String confirm_payment = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='payment-success-message']"))).getText();
        String expected ="Payment was successful";
        Assert.assertEquals(confirm_payment,expected);


    }
    @Test (priority = 8)
    public void returnToMainPage(){
        driver.findElement(By.cssSelector("[id='Layer_1']")).click();
    }
    @AfterTest
    public void THE_END(){driver.close();}
}

