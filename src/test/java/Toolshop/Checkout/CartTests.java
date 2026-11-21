package Toolshop.Checkout;

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
import java.util.Locale;

public class CartTests {

    WebDriver driver;
    WebDriverWait wait;
    Product product_test;
    SoftAssert soft = new SoftAssert();
    By quantityBy = By.cssSelector("input.form-control.quantity");
    
    private WebElement find_element(By by){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    private List<WebElement> find_elements(By by){
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    private void setQuantity(String value) {
        find_element(quantityBy)
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, value, Keys.ENTER);
    }

    @BeforeMethod
    void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("https://practicesoftwaretesting.com/");


        wait = new WebDriverWait(driver,Duration.ofSeconds(10));

        List<WebElement> productCards = find_elements(By.cssSelector("a.card"));
        productCards.get(0).click();

        String title = find_element(By.cssSelector("h1[data-test=\"product-name\"]")).getText().trim();
        String price = find_element(By.cssSelector("span[aria-label=\"unit-price\"]")).getText().trim().replace("$", "");
        product_test = new Product(title,price);

        WebElement add_btn = find_element(By.id("btn-add-to-cart"));
        add_btn.click();

        WebElement toast_msg = find_element(By.cssSelector("#toast-container div.ngx-toastr"));
        soft.assertEquals(toast_msg.getText().trim() ,"Product added to shopping cart.");

        driver.navigate().to("https://practicesoftwaretesting.com/checkout");
    }
    @AfterMethod
    void after(){
        driver.quit();
    }


    @Test
    void validate_correct_product(){

        String title = find_element(By.cssSelector("span.product-title")).getText().trim();
        String price = find_element(By.cssSelector("span[data-test=\"product-price\"]")).getText().trim().replace("$", "");

        soft.assertEquals(title,product_test.title);
        soft.assertEquals(price,product_test.price);
        soft.assertAll();
    }

    @Test
    void enter_valid_number_quantity(){
        setQuantity("4");

        wait.until(ExpectedConditions.attributeToBe(quantityBy,"value","4"));
        Assert.assertEquals(find_element(quantityBy).getDomProperty("value"), "4");
    }

    @Test
    void enter_zero_quantity(){
        WebElement quantity = find_element(quantityBy);

        setQuantity("0");

        wait.until(ExpectedConditions.attributeToBe(quantityBy,"value","1"));
        Assert.assertEquals(quantity.getAttribute("value"),"1");
    }

    @Test
    void enter_more_than_99(){
        setQuantity("120");

        WebElement toast = find_element(By.cssSelector("div.ngx-toastr.toast-warning"));
        Assert.assertEquals(toast.getText().trim(),"You can order at most 99 of this product.");
    }

    @Test
    void check_total(){
        setQuantity("3");

        double total = Double.parseDouble(product_test.price) * 3;
        String expected = String.format(Locale.US, "%.2f", total);

        By linePriceBy = By.cssSelector("span[data-test='line-price']");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(linePriceBy, expected));

        String total_line = find_element(By.cssSelector("span[data-test=\"line-price\"]")).getText().trim().replace("$","");
        Assert.assertEquals(Double.parseDouble(total_line),total,0.01);
    }

}
