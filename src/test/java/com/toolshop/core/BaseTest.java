package com.toolshop.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    protected void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    protected void after(){
        if(driver !=null){
            driver.close();
        }
    }
}
