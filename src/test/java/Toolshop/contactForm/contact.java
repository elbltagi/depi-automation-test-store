package Toolshop.contactForm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class contact {
    WebDriver driver ;
    WebDriverWait wait;
    @BeforeMethod
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practicesoftwaretesting.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // Wait for the product list to load before starting search/filter tests.
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("a.card")));
    }
    @Test
    public void navigatingTocontact (){
        driver.findElement(By.cssSelector("[class='nav-link']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='contact-submit']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='first-name-error']")));
        boolean ErrorMessage = driver.findElement(By.cssSelector("[data-test='first-name-error']")).isDisplayed();
        Assert.assertTrue(ErrorMessage);

    }
    @Test
    public void validate_field() {
        SoftAssert softAssert = new SoftAssert();
        driver.findElement(By.cssSelector("[class='nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id='first_name']")));
        driver.findElement(By.cssSelector("[id='first_name']")).sendKeys("blorvanteximpoludrasfenixoquartaveloziuma");
        driver.findElement(By.cssSelector("[data-test='contact-submit']")).click();
        boolean isErrorDisplayed = !driver.findElements(By.cssSelector("[data-test='first-name-error']")).isEmpty();
        softAssert.assertTrue(isErrorDisplayed, "Bug: First name error message did not appear!");
        softAssert.assertAll();
    }
    @Test
    public void validate_invalid_email() {
        driver.findElement(By.cssSelector("[class='nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id='email']")));
        driver.findElement(By.cssSelector("[id='email']")).sendKeys("testemail.com");
        driver.findElement(By.cssSelector("[data-test='contact-submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email-error']")));
        WebElement ErrorMessage = driver.findElement(By.cssSelector("[data-test='email-error']"));
        Assert.assertTrue(ErrorMessage.isDisplayed());
    }

    @Test
    public void validate_short_message() {
        driver.findElement(By.cssSelector("[class='nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id='message']")));
        driver.findElement(By.cssSelector("[id='message']")).sendKeys("Hello");
        driver.findElement(By.cssSelector("[data-test='contact-submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='message-error']")));
        WebElement ErrorMessage = driver.findElement(By.cssSelector("[data-test='message-error']"));
        Assert.assertTrue(ErrorMessage.isDisplayed());
    }

    @Test
    public void submit_valid_contact_form() {
        driver.findElement(By.cssSelector("[class='nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id='first_name']")));

        driver.findElement(By.cssSelector("[id='first_name']")).sendKeys("Ahmed");
        driver.findElement(By.cssSelector("[id='last_name']")).sendKeys("Ali");
        driver.findElement(By.cssSelector("[id='email']")).sendKeys("ahmed.ali@test.com");

        Select subject = new Select(driver.findElement(By.cssSelector("[id='subject']")));
        subject.selectByIndex(1);

        driver.findElement(By.cssSelector("[id='message']")).sendKeys("This is a test message to verify that the contact form accepts more than fifty characters.");
        driver.findElement(By.cssSelector("[data-test='contact-submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        WebElement SuccessMessage = driver.findElement(By.cssSelector(".alert-success"));
        Assert.assertTrue(SuccessMessage.isDisplayed());
    }
    @AfterMethod
    public void end (){driver.close();}


}
