package com.toolshop.tests;

import com.toolshop.core.BaseTest;
import com.toolshop.core.data;
import com.toolshop.pages.Login;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test
    protected void login_with_valid_credentials(){
        Login loginPage = new Login(driver);
        loginPage.open().login(data.l_valid_email,data.l_valid_pass);
    }
}
