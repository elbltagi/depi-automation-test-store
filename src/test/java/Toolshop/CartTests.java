package Toolshop;

import Toolshop.Data.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class CartTests {

    WebDriver driver;
    WebDriverWait Wait;
    Product product_test;
    SoftAssert soft = new SoftAssert();

    @BeforeTest
    void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("https://practicesoftwaretesting.com/");


        Wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        List<WebElement> productCards = Wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("a.card")));
        productCards.get(0).click();

        WebElement add_btn = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-to-cart")));
        add_btn.click();

        String title = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[data-test=\"product-name\"]"))).getText().trim().replace("$", "");;
        String price = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[aria-label=\"unit-price\"]"))).getText().trim().replace("$", "");;
        product_test = new Product(title,price);

        WebElement toast_msg = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container div.ngx-toastr")));
        soft.assertEquals(toast_msg.getText().trim() ,"Product added to shopping cart.");

        driver.navigate().to("https://practicesoftwaretesting.com/checkout");

    }
    @AfterTest
    void after(){
        driver.close();
    }

    @Test
    void validate_correct_product(){


        String title = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.product-title"))).getText().trim().replace("$", "");;
        String price = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[data-test=\"product-price\"]"))).getText().trim().replace("$", "");;

        soft.assertEquals(title,product_test.title);
        soft.assertEquals(price,product_test.price);
        soft.assertAll();
    }

    @Test
    void enter_valid_number_quantity(){
        WebElement quantity = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.form-control.quantity")));

        quantity.clear();
        quantity.sendKeys("4");
        quantity.sendKeys(Keys.ENTER);

        Assert.assertEquals(quantity.getAttribute("value"),"4");
    }

    @Test
    void enter_zero_quantity(){
        WebElement quantity = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.form-control.quantity")));

        quantity.clear();
        quantity.sendKeys("0");
        quantity.sendKeys(Keys.ENTER);

        Assert.assertEquals(quantity.getAttribute("value"),"1");
    }

    @Test
    void enter_more_than_99(){
        WebElement quantity = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.form-control.quantity")));

        quantity.clear();
        quantity.sendKeys("120");
        quantity.sendKeys(Keys.ENTER);

        WebElement toast = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.ngx-toastr.toast-warning")));

        Assert.assertEquals(toast.getText().trim(),"You can order at most 99 of this product.");
    }

    @Test
    void check_total(){

        WebElement quantity = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.form-control.quantity")));

        quantity.clear();
        quantity.sendKeys("3");
        quantity.sendKeys(Keys.ENTER);

        double total = Double.parseDouble(product_test.price) * 3;

        String total_line = Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[data-test=\"line-price\"]"))).getText().trim().replace("$","");
        Assert.assertEquals(total_line, Double.toString(total));

    }

}
