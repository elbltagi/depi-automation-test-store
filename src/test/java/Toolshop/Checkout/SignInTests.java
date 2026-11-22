package Toolshop.Checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignInTests extends Base{

    @BeforeMethod
    public void goToSignInPage(){

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test=\"proceed-1\"]"))).click();
        find_element(By.cssSelector("[data-test='email']"));

    }


    @Test
    public void SignInWithCorrectCredentials (){
        login("customer@practicesoftwaretesting.com","welcome01");

        By alreadyLoggedIn = By.xpath("//*[contains(normalize-space(text()), 'you are already logged in')]");

        WebElement msg = find_element(alreadyLoggedIn);
        Assert.assertTrue(msg.isDisplayed(),"'Already logged in' message not shown");
    }

    @Test
    void SignInWithWrongPassword (){
        login("customer@practicesoftwaretesting.com","w1515611");

        WebElement err_msg = find_element(By.cssSelector("[data-test=\"login-error\"]"));
        Assert.assertTrue(err_msg.isDisplayed(),"error message doesn't displayed");

    }
    @Test
    void SignInWithWrongEmail (){
        login("customer@praicesoftwaretesting.com","welcome01");

        WebElement err_msg = find_element(By.cssSelector("[data-test=\"login-error\"]"));
        Assert.assertTrue(err_msg.isDisplayed(),"error message doesn't displayed");

    }
    @Test
    void SignInWithEmptyEmail(){
        login("","welcome01");

        WebElement email_msg = find_element(By.id("email-error"));
        Assert.assertTrue(email_msg.isDisplayed(),"Email required message doesn't displayed");

    }
    @Test
    void SignInWithEmptyPassword(){
        login("customer@practicesoftwaretesting.com","");

        WebElement email_msg = find_element(By.id("password-error"));
        Assert.assertTrue(email_msg.isDisplayed(),"Password required message doesn't displayed");
    }


}
