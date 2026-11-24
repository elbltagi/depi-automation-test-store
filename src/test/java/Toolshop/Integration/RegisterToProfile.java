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
import org.testng.asserts.SoftAssert;

public class RegisterToProfile extends Base  {

    String uniqueEmail;

    @BeforeTest
    public void register(){
        driver.navigate().to("https://practicesoftwaretesting.com/auth/register");
    }

    private String getValue(By by){
        return find_element(by).getDomProperty("value").trim();
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

        WebElement City = find_element(By.id("city"));

        City.sendKeys("berket elsabaa");

        WebElement State = find_element(By.id("state"));

        State.sendKeys("elgarb");

        WebElement Phone = find_element(By.id("phone"));

        Phone.sendKeys("01558799766");

        WebElement EmailAdress = find_element(By.id("email"));

        EmailAdress.sendKeys(uniqueEmail);

        WebElement password =find_element(By.id("password"));
        password.sendKeys("Elgohary@123");

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

        find_element(By.id("email")).sendKeys(uniqueEmail);
        find_element(By.id("password")).sendKeys("Elgohary@123");

        find_element(By.cssSelector("[data-test=\"login-submit\"]")).click();

        wait.until(ExpectedConditions.urlContains(ValidationMessages.ACCOUNT_URL_PART));
        System.out.println("Current URL: " + driver.getCurrentUrl());
        Assert.assertTrue(
                driver.getCurrentUrl().contains(ValidationMessages.ACCOUNT_URL_PART),
                "Login failed: User was not redirected to account page"
        );

    }

    @Test(priority = 2)
    public void validateProfileData(){
        driver.navigate().to("https://practicesoftwaretesting.com/account/profile");

        wait.until(ExpectedConditions.attributeToBe(By.id("first_name"),"value","mostafa"));

        SoftAssert soft = new SoftAssert();

        soft.assertEquals(getValue(By.id("first_name")),"mostafa", "First name on the profile");
        soft.assertEquals(getValue(By.id("last_name")),"elgohary", "Last name on the profile");
        soft.assertEquals(getValue(By.id("street")),"elbahgi street", "Street on the profile");
        soft.assertEquals(getValue(By.id("postal_code")),"3651", "Postcode on the profile");
        soft.assertEquals(getValue(By.id("city")),"berket elsabaa", "City on the profile");
        soft.assertEquals(getValue(By.id("phone")),"01558799766", "Phone on the profile");
        soft.assertEquals(getValue(By.id("state")),"elgarb", "State on the profile");
        soft.assertEquals(getValue(By.id("country")),"EG", "Country on the profile");

        soft.assertAll();

    }

}
