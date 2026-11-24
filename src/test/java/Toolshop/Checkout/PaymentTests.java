package Toolshop.Checkout;

import Toolshop.Checkout.Data.BillingData;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PaymentTests extends Base {
    By payment = By.id("payment-method");

    private void fillBilling(BillingData d) {
        if (!d.Country.isEmpty())
            new Select(find_element(By.id("country"))).selectByContainsVisibleText(d.Country);
        fill(find_element(By.id("postal_code")),  d.postal_code);
        fill(find_element(By.id("house_number")), d.house_number);
        fill(find_element(By.id("street")),       d.street);
        fill(find_element(By.id("city")),         d.city);
        fill(find_element(By.id("state")),        d.state);
    }

    private void proceed() {
        find_element(By.cssSelector("[data-test=\"finish\"]")).click();
        Assert.assertTrue(find_element(By.cssSelector("[data-test=\"payment-success-message\"]")).isDisplayed(), "Payment success message after finishing the order");
    }

    private void select(String method){
        Select payMethod = new Select(find_element(payment));
        payMethod.selectByValue(method);
    }

    @BeforeMethod
    public void goToPayment(){
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test=\"proceed-1\"]"))).click();

        find_element(By.cssSelector("[data-test='email']"));

        find_element(By.cssSelector("[href=\"#guest-tab\"]")).click();

        find_element(By.id("guest-email")).sendKeys("test@gmail.com");
        find_element(By.id("guest-first-name")).sendKeys("Youssef");
        find_element(By.id("guest-last-name")).sendKeys("Elbltagi");

        find_element(By.cssSelector("[data-test=\"guest-submit\"]")).click();
        find_element(By.cssSelector("[data-test=\"proceed-2-guest\"]")).click();

        BillingData data = new BillingData("Egypt","11511","15","","","");
        fillBilling(data);

        find_element(By.cssSelector("[data-test=\"proceed-3\"]")).click();

    }

    @Test
    void payWithBank(){

        select("bank-transfer");

        find_element(By.id("bank_name")).sendKeys("CIB");
        find_element(By.id("account_name")).sendKeys("Youssef Salah");
        find_element(By.id("account_number")).sendKeys("123456789");

        proceed();
    }

    @Test
    void payWithCash(){
        select("cash-on-delivery");
        proceed();
    }

    @Test
    void payWithCredit(){

        select("credit-card");

        find_element(By.id("credit_card_number")).sendKeys("0000-0000-0000-0000");
        find_element(By.id("expiration_date")).sendKeys("06/2029");
        find_element(By.id("card_holder_name")).sendKeys("Youssef Salah");

        proceed();
    }

    @Test
    void payWithPayLater(){
        select("buy-now-pay-later");

        Select mon_inst = new Select(find_element(By.id("monthly_installments")));
        mon_inst.selectByIndex(1);

        proceed();
    }



}
