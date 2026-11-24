package Toolshop.Integration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.time.Duration;
import java.util.List;

public class Base {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setup(){

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }
    @AfterTest
    public void closeWeb (){
        driver.quit();
    }

    //functions
    protected WebElement find_element(By by){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    protected List<WebElement> find_elements(By by){
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

}
