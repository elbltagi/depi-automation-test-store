package Toolshop;

import Toolshop.constants.ValidationMessages;
import Toolshop.utils.TestDataBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class Rigster {

    WebDriver b;
    WebDriverWait wait;

    @BeforeMethod
    public void openBrowser() {
        b = new ChromeDriver();
        b.manage().window().maximize();
        b.get("https://practicesoftwaretesting.com/auth/register");
        wait = new WebDriverWait(b, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void closeBrowser() {
        if (b != null) {
            b.quit();
        }
    }

    @Test(priority = 1)
    public void ValidTesting() {
        String uniqueEmail = TestDataBuilder.generateUniqueEmail();

//              frist name
        WebElement FirstName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("first_name")
                )
        );
        FirstName.sendKeys("mostafa");
        System.out.println("Name entered successfully | Email: " + uniqueEmail);
//             last name
        WebElement lastName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        (By.id("last_name"))
                )
        );
        lastName.sendKeys("elgohary");
//         brithday
        WebElement BirthDay = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("dob")
                )
        );
        BirthDay.sendKeys("2007-06-01");
//             country
        WebElement country = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("country")
                )
        );
        Select country1 = new Select(country);
        country1.selectByValue("EG");         //elhag youssef beltagi number 1
//        postalcode
        WebElement PostalCode = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("postal_code")
                )
        );
        PostalCode.sendKeys("3651");
//          housenumber
        WebElement HouseNumber = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("house_number")
                )
        );
        HouseNumber.sendKeys("32");
        WebElement Street = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("street")
                )
        );
        Street.sendKeys("elbahgi street");
//           city
        WebElement City = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("city")
                )
        );
        City.sendKeys("berket elsabaa");
//           state
        WebElement State = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("state")
                )
        );
        State.sendKeys("elgarb");
//           phone
        WebElement Phone = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("phone")
                )
        );
        Phone.sendKeys("01558799766");
//           email adress
        WebElement EmailAdress = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("email")
                )
        );
        EmailAdress.sendKeys(uniqueEmail);
//                 password
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("password")
                )
        );
        password.sendKeys("Elgohary@123");
//             rigister button
        WebElement Rigister = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        Rigister.click();

        wait.until(ExpectedConditions.urlContains(ValidationMessages.LOGIN_URL_PART));
        Assert.assertTrue(
                b.getCurrentUrl().contains(ValidationMessages.LOGIN_URL_PART),
                "Registration failed: User was not redirected to login page"
        );
    }

    @Test(priority = 2)
    public void InvalidEmptyFieldsTesting() {
//              frist name
        WebElement FirstName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("first_name")
                )
        );
        FirstName.sendKeys("");
//             last name
        WebElement lastName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        (By.id("last_name"))
                )
        );
        lastName.sendKeys("");
//         brithday
        WebElement BirthDay = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("dob")
                )
        );
        BirthDay.sendKeys("");
//             country
        WebElement country = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("country")
                )
        );
//        postalcode
        WebElement PostalCode = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("postal_code")
                )
        );
        PostalCode.sendKeys("");
//          housenumber
        WebElement HouseNumber = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("house_number")
                )
        );
        HouseNumber.sendKeys("");
        WebElement Street = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("street")
                )
        );
        Street.sendKeys("");
//           city
        WebElement City = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("city")
                )
        );
        City.sendKeys("");
//           state
        WebElement State = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("state")
                )
        );
        State.sendKeys("");
//           phone
        WebElement Phone = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("phone")
                )
        );
        Phone.sendKeys("");
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
//             rigister button
        WebElement Rigister = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        Rigister.click();

        SoftAssert softAssert = new SoftAssert();

        WebElement firstNameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='first-name-error']")));
        WebElement lastNameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='last-name-error']")));
        WebElement dobError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='dob-error']")));
        WebElement streetError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='street-error']")));
        WebElement cityError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='city-error']")));
        WebElement stateError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='state-error']")));
        WebElement countryError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='country-error']")));
        WebElement phoneError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='phone-error']")));
        WebElement emailError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email-error']")));
        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='password-error']")));

        softAssert.assertTrue(firstNameError.getText().contains(ValidationMessages.REQUIRED_FIELD), "First name error missing");
        softAssert.assertTrue(lastNameError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Last name error missing");
        softAssert.assertTrue(dobError.getText().contains(ValidationMessages.REQUIRED_FIELD), "DOB error missing");
        softAssert.assertTrue(streetError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Street error missing");
        softAssert.assertTrue(cityError.getText().contains(ValidationMessages.REQUIRED_FIELD), "City error missing");
        softAssert.assertTrue(stateError.getText().contains(ValidationMessages.REQUIRED_FIELD), "State error missing");
        softAssert.assertTrue(countryError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Country error missing");
        softAssert.assertTrue(phoneError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Phone error missing");
        softAssert.assertTrue(emailError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Email error missing");
        softAssert.assertTrue(passwordError.getText().contains(ValidationMessages.REQUIRED_FIELD), "Password error missing");
        softAssert.assertTrue(b.getCurrentUrl().contains(ValidationMessages.REGISTER_URL_PART), "Should stay on register page");

        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void InvalidEmailFormatTesting() {
//              frist name
        WebElement FirstName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("first_name")
                )
        );
        FirstName.sendKeys("mostafa");
        System.out.println("Name entered successfully");
//             last name
        WebElement lastName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        (By.id("last_name"))
                )
        );
        lastName.sendKeys("elgohary");
//         brithday
        WebElement BirthDay = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("dob")
                )
        );
        BirthDay.sendKeys("2007-06-01");
//             country
        WebElement country = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("country")
                )
        );
        Select country1 = new Select(country);
        country1.selectByValue("EG");         //elhag youssef beltagi number 1
//        postalcode
        WebElement PostalCode = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("postal_code")
                )
        );
        PostalCode.sendKeys("3651");
//          housenumber
        WebElement HouseNumber = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("house_number")
                )
        );
        HouseNumber.sendKeys("32");
        WebElement Street = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("street")
                )
        );
        Street.sendKeys("elbahgi street");
//           city
        WebElement City = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("city")
                )
        );
        City.sendKeys("berket elsabaa");
//           state
        WebElement State = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("state")
                )
        );
        State.sendKeys("elgarb");
//           phone
        WebElement Phone = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("phone")
                )
        );
        Phone.sendKeys("01558799766");
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
//             rigister button
        WebElement Rigister = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        Rigister.click();

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

    @Test(priority = 4)
    public void InvalidPasswordTesting() {
//              frist name
        WebElement FirstName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("first_name")
                )
        );
        FirstName.sendKeys("mostafa");
        System.out.println("Name entered successfully");
//             last name
        WebElement lastName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        (By.id("last_name"))
                )
        );
        lastName.sendKeys("elgohary");
//         brithday
        WebElement BirthDay = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("dob")
                )
        );
        BirthDay.sendKeys("2007-06-01");
//             country
        WebElement country = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("country")
                )
        );
        Select country1 = new Select(country);
        country1.selectByValue("EG");         //elhag youssef beltagi number 1
//        postalcode
        WebElement PostalCode = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("postal_code")
                )
        );
        PostalCode.sendKeys("3651");
//          housenumber
        WebElement HouseNumber = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("house_number")
                )
        );
        HouseNumber.sendKeys("32");
        WebElement Street = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("street")
                )
        );
        Street.sendKeys("elbahgi street");
//           city
        WebElement City = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("city")
                )
        );
        City.sendKeys("berket elsabaa");
//           state
        WebElement State = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("state")
                )
        );
        State.sendKeys("elgarb");
//           phone
        WebElement Phone = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("phone")
                )
        );
        Phone.sendKeys("01558799766");
//           email adress
        WebElement EmailAdress = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("email")
                )
        );
        EmailAdress.sendKeys(TestDataBuilder.generateUniqueEmail());
//                 password
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("password")
                )
        );
        password.sendKeys("Ab1@");
//             rigister button
        WebElement Rigister = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        Rigister.click();

        WebElement passwordError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='password-error']")
                )
        );
        System.out.println("Password Error: " + passwordError.getText());
        Assert.assertTrue(
                passwordError.getText().contains(ValidationMessages.PASSWORD_MIN_LENGTH),
                "Expected password length error, but got: " + passwordError.getText()
        );
    }

    @Test(priority = 5)
    public void InvalidPhoneTesting() {
//              frist name
        WebElement FirstName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("first_name")
                )
        );
        FirstName.sendKeys("mostafa");
        System.out.println("Name entered successfully");
//             last name
        WebElement lastName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        (By.id("last_name"))
                )
        );
        lastName.sendKeys("elgohary");
//         brithday
        WebElement BirthDay = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("dob")
                )
        );
        BirthDay.sendKeys("2007-06-01");
//             country
        WebElement country = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("country")
                )
        );
        Select country1 = new Select(country);
        country1.selectByValue("EG");         //elhag youssef beltagi number 1
//        postalcode
        WebElement PostalCode = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("postal_code")
                )
        );
        PostalCode.sendKeys("3651");
//          housenumber
        WebElement HouseNumber = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("house_number")
                )
        );
        HouseNumber.sendKeys("32");
        WebElement Street = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("street")
                )
        );
        Street.sendKeys("elbahgi street");
//           city
        WebElement City = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("city")
                )
        );
        City.sendKeys("berket elsabaa");
//           state
        WebElement State = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("state")
                )
        );
        State.sendKeys("elgarb");
//           phone
        WebElement Phone = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("phone")
                )
        );
        Phone.sendKeys("abcde@#$%");
//           email adress
        WebElement EmailAdress = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("email")
                )
        );
        EmailAdress.sendKeys(TestDataBuilder.generateUniqueEmail());
//                 password
        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("password")
                )
        );
        password.sendKeys("Elgohary@123");
//             rigister button
        WebElement Rigister = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[type=\"submit\"]")
                )
        );
        Rigister.click();

        WebElement phoneError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='phone-error']")
                )
        );
        System.out.println("Phone Error: " + phoneError.getText());
        Assert.assertTrue(
                phoneError.getText().contains(ValidationMessages.PHONE_ONLY_NUMBERS),
                "Expected numbers-only phone error, but got: " + phoneError.getText()
        );
    }
}