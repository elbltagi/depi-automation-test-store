package Toolshop.Checkout;

import Toolshop.Checkout.Data.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.time.Duration;
import java.util.List;

public class Base {
    public WebDriver driver;
    public WebDriverWait wait;
    public Product product_test;
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
org.testng.Assert.assertEquals(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container div.ngx-toastr"))).getText().trim(), "Product added to shopping cart.");

        driver.navigate().to("https://practicesoftwaretesting.com/checkout");
    }
    @AfterMethod
    void after(){
        driver.quit();
    }

    protected void login(String email,String pass){
        WebElement email_el  = find_element(By.id("email"));
        WebElement pass_el  = find_element(By.id("password"));

        email_el.clear();
        email_el.sendKeys(email);

        pass_el.clear();
        pass_el.sendKeys(pass);

        find_element(By.cssSelector("[data-test=\"login-submit\"]")).click();

    }
    protected void fill(WebElement field, String input) {
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        if (!input.isEmpty()) field.sendKeys(input);
    }

    @DataProvider(name = "fieldsMissing")
    public Object[][] fieldMissing(){
        return new Object[][]{{"street"},{"city"},{"state"}};
    }

    @DataProvider(name = "maxLength")
    public Object[][] maxLength() {
        return new Object[][]{
                {"street",      70},
                {"city",        40},
                {"state",       40},
                {"postal_code", 10},
        };
    }
}
