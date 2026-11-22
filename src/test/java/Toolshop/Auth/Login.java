package Toolshop.Auth;

import Toolshop.Auth.constants.ValidationMessages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class Login {

    WebDriver b;
    WebDriverWait wait;

    @BeforeMethod
    public void openBrowser() {
        b = new ChromeDriver();
        b.manage().window().maximize();
        b.get("https://practicesoftwaretesting.com/auth/login");
        wait = new WebDriverWait(b, Duration.ofSeconds(30));
    }

    @AfterMethod
    public void closeBrowser() {
        if (b != null) {
            b.quit();
        }
    }

    @Test(priority = 1)
    public void ValidLoginTesting() {
//           email adress
        WebElement EmailAdress = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("email")
                )
        );
        EmailAdress.sendKeys("customer@practicesoftwaretesting.com");
//                 password
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("password")
                )
        );
        password.sendKeys("welcome01");
//             login button
        WebElement LoginBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        LoginBtn.click();

        wait.until(ExpectedConditions.urlContains(ValidationMessages.ACCOUNT_URL_PART));
        System.out.println("Current URL: " + b.getCurrentUrl());
        Assert.assertTrue(
                b.getCurrentUrl().contains(ValidationMessages.ACCOUNT_URL_PART),
                "Login failed: User was not redirected to account page"
        );
    }

    @Test(priority = 2)
    public void InvalidEmptyFieldsTesting() {
//           email adress
        WebElement EmailAdress = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("email")
                )
        );
        EmailAdress.sendKeys("");
//                 password
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("password")
                )
        );
        password.sendKeys("");
//             login button
        WebElement LoginBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        LoginBtn.click();

        SoftAssert softAssert = new SoftAssert();

        WebElement emailError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='email-error']")
                )
        );
        WebElement passwordError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='password-error']")
                )
        );

        System.out.println("Email Error: " + emailError.getText());
        System.out.println("Password Error: " + passwordError.getText());

        softAssert.assertTrue(emailError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Email required error missing");
        softAssert.assertTrue(passwordError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Password required error missing");
        softAssert.assertTrue(b.getCurrentUrl().contains(ValidationMessages.LOGIN_URL_PART), "Should stay on login page");

        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void InvalidCredentialsTesting() {
//           email adress
        WebElement EmailAdress = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("email")
                )
        );
        EmailAdress.sendKeys("customer@practicesoftwaretesting.com");
//                 password
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("password")
                )
        );
        password.sendKeys("WrongPass@123");
//             login button
        WebElement LoginBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        LoginBtn.click();

        WebElement loginError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='login-error']")
                )
        );
        System.out.println("Login Error: " + loginError.getText());
        Assert.assertTrue(
                loginError.getText().contains(ValidationMessages.INVALID_CREDENTIALS),
                "Expected invalid credentials error, but got: " + loginError.getText()
        );
    }

    @Test(priority = 4)
    public void InvalidEmailFormatTesting() {
//           email adress
        WebElement EmailAdress = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("email")
                )
        );
        EmailAdress.sendKeys("mostafa.com");
//                 password
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("password")
                )
        );
        password.sendKeys("Elgohary@123");
//             login button
        WebElement LoginBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        LoginBtn.click();

        WebElement emailError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='email-error']")
                )
        );
        System.out.println("Email Error: " + emailError.getText());
        Assert.assertTrue(
                emailError.getText().contains(ValidationMessages.EMAIL_INVALID_FORMAT),
                "Expected invalid email error, but got: " + emailError.getText()
        );
    }

    @Test(priority = 5)
    public void ForgotPasswordLinkTesting() {
//             forgot password link
        WebElement ForgotPasswordLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[data-test='forgot-password-link']")
                )
        );
        ForgotPasswordLink.click();

        wait.until(ExpectedConditions.urlContains(ValidationMessages.FORGOT_PASSWORD_URL_PART));
        System.out.println("Current URL: " + b.getCurrentUrl());
        Assert.assertTrue(
                b.getCurrentUrl().contains(ValidationMessages.FORGOT_PASSWORD_URL_PART),
                "Failed to redirect to Forgot Password page"
        );
    }
}