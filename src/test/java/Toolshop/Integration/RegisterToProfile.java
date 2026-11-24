package Toolshop.Integration;

import Toolshop.Auth.constants.ValidationMessages;
import Toolshop.Auth.utils.TestDataBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RegisterToProfile extends Base  {

    String uniqueEmail;

    @BeforeTest
    public void register(){
        driver.navigate().to("https://practicesoftwaretesting.com/auth/register");
    }

    @Test(priority = 0)
    public void ValidTesting() {

        uniqueEmail = TestDataBuilder.generateUniqueEmail();

//              frist name
        WebElement FirstName = find_element(By.id("first_name"));

        FirstName.sendKeys("mostafa");
        System.out.println("Name entered successfully | Email: " + uniqueEmail);
//             last name
        WebElement lastName = find_element(By.id("last_name"));

        lastName.sendKeys("elgohary");
//         brithday
        WebElement BirthDay = find_element(By.id("dob"));
        BirthDay.sendKeys("2007-06-01");
//             country
        WebElement country = find_element(By.id("country"));
        Select country1 = new Select(country);
        country1.selectByValue("EG");         //elhag youssef beltagi number 1
//        postalcode
        WebElement PostalCode = find_element(By.id("postal_code"));
        PostalCode.sendKeys("3651");
//          housenumber
        WebElement HouseNumber = find_element(By.id("house_number"));

        HouseNumber.sendKeys("32");
        WebElement Street = find_element(By.id("street"));
        Street.sendKeys("elbahgi street");
//           city
        WebElement City = find_element(By.id("city"));

        City.sendKeys("berket elsabaa");
//           state
        WebElement State = find_element(By.id("state"));

        State.sendKeys("elgarb");
//           phone
        WebElement Phone = find_element(By.id("phone"));

        Phone.sendKeys("01558799766");
//           email adress
        WebElement EmailAdress = find_element(By.id("email"));

        EmailAdress.sendKeys(uniqueEmail);
//                 password
        WebElement password =find_element(By.id("password"));
        password.sendKeys("Elgohary@123");
//             rigister button
        WebElement Rigister =find_element(By.cssSelector("[type=\"submit\"]"));

        Rigister.click();

        wait.until(ExpectedConditions.urlContains(ValidationMessages.LOGIN_URL_PART));
        Assert.assertTrue(
                driver.getCurrentUrl().contains(ValidationMessages.LOGIN_URL_PART),
                "Registration failed: User was not redirected to login page"
        );
    }

    @Test(priority = 1)
    public void loginWithExistingAccount(){
        driver.navigate().to("https://practicesoftwaretesting.com/auth/login");

        find_element(By.id("email")).sendKeys(uniqueEmail);
        find_element(By.id("password")).sendKeys("Elgohary@123");

        find_element(By.cssSelector("[type=\"submit\"]")).click();

    }

    @Test(priority = 2)
    public void viewProfile(){
        driver.navigate().to("https://practicesoftwaretesting.com/account/profile");

    }

}
