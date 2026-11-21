package toolshop.pages;

import toolshop.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Login extends BasePage {

    public Login(WebDriver driver){
        super(driver);
    }

    public Login open(){
        driver.get("https://practicesoftwaretesting.com/auth/login");
        return this;
    }

    public void login(String email , String pass){

        type(By.id("email"),email);
        type(By.id("password"),email);

        click(test("login-submit"));

    }

}
