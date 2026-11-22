package Toolshop.Checkout;

import Toolshop.Data.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.List;

public class Base {
    WebDriver driver;
    WebDriverWait wait;
    Product product_test;
    By quantityBy = By.cssSelector("input.form-control.quantity");

    protected WebElement find_element(By by){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    protected List<WebElement> find_elements(By by){
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    @BeforeMethod
    void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("https://practicesoftwaretesting.com/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> productCards = find_elements(By.cssSelector("a.card"));
        productCards.get(0).click();

        String title = find_element(By.cssSelector("h1[data-test=\"product-name\"]")).getText().trim();
        String price = find_element(By.cssSelector("span[aria-label=\"unit-price\"]")).getText().trim().replace("$", "");
        product_test = new Product(title,price);

        WebElement add_btn = find_element(By.id("btn-add-to-cart"));
        add_btn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container div.ngx-toastr")));

        driver.navigate().to("https://practicesoftwaretesting.com/checkout");
    }
    @AfterMethod
    void after(){
        driver.quit();
    }
}
