package Toolshop.product;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class ProductBase {

    protected static final String HOME = "https://practicesoftwaretesting.com/";

    protected static final By PRODUCT_CARD = By.cssSelector("a.card");
    protected static final By PRODUCT_NAME = By.cssSelector("[data-test='product-name']");
    protected static final By UNIT_PRICE = By.cssSelector("[data-test='unit-price']");
    protected static final By ADD_TO_CART = By.cssSelector("[data-test='add-to-cart']");
    protected static final By CART_BADGE = By.cssSelector("[data-test='cart-quantity']");
    protected static final By QUANTITY = By.cssSelector("[data-test='quantity']");

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected WebElement find_element(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    // Product ids are regenerated whenever the demo data is reseeded, so we reach
    // a product by clicking the first card instead of hard-coding a URL.
    protected void openFirstProduct() {
        driver.get(HOME);
        wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_CARD)).click();
        find_element(PRODUCT_NAME);
    }

    protected void setQuantity(String value) {
        find_element(QUANTITY).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, value, Keys.TAB);
    }

    protected void waitForValue(By by, String expected) {
        wait.until(d -> expected.equals(d.findElement(by).getDomProperty("value")));
    }

    protected String valueOf(By by) {
        return find_element(by).getDomProperty("value");
    }
}
