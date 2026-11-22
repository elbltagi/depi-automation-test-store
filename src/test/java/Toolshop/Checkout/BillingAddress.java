package Toolshop.Checkout;

import Toolshop.Checkout.Data.BillingData;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class BillingAddress extends Base {
    private static final By PROCEED_3 = By.cssSelector("[data-test=\"proceed-3\"]");

    private boolean proceedIsDisabled() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.not(
                            ExpectedConditions.elementToBeClickable(PROCEED_3)));
            return true;
        } catch (TimeoutException e) {
            return false;   // stayed clickable for 2s → genuinely enabled
        }
    }

    private WebElement waitForNonEmptyValue(By by) {
        return wait.until(d -> {
            WebElement el = d.findElement(by);
            String v = d.findElement(by).getDomProperty("value").trim();
            return v.isEmpty() ? null : el;   // null/false → keep polling
        });
    }

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
        find_element(By.cssSelector("[data-test=\"proceed-3\"]")).click();
    }

    @BeforeMethod
    void goToBillingAddress(){
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test=\"proceed-1\"]"))).click();

        find_element(By.cssSelector("[data-test='email']"));
//        login("youssefelbltag@gmail.com","Aa@123654@#");

        find_element(By.cssSelector("[href=\"#guest-tab\"]")).click();

        find_element(By.id("guest-email")).sendKeys("test@gmail.com");
        find_element(By.id("guest-first-name")).sendKeys("Youssef");
        find_element(By.id("guest-last-name")).sendKeys("Elbltagi");

        find_element(By.cssSelector("[data-test=\"guest-submit\"]")).click();
        find_element(By.cssSelector("[data-test=\"proceed-2-guest\"]")).click();
    }

    @Test
    public void ensure_data_auto(){

        BillingData data = new BillingData("Egypt","11511","15","","","");
        fillBilling(data);

        SoftAssert soft =new SoftAssert();

        soft.assertEquals(waitForNonEmptyValue(By.id("street")).getDomProperty("value").trim(),"Tahrir Street");
        soft.assertEquals(waitForNonEmptyValue(By.id("city")).getDomProperty("value").trim(),"Cairo");
        soft.assertEquals(waitForNonEmptyValue(By.id("state")).getDomProperty("value").trim(),"Cairo");

        soft.assertAll();

        proceed();

    }

    @Test(dataProvider = "fieldsMissing")
    public void proceedDisabledWhenFiledMissing(String fieldId){
        BillingData data = new BillingData("Egypt","11511","15","","","");
        fillBilling(data);

        WebElement target =  waitForNonEmptyValue(By.id(fieldId));
        target.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        target.sendKeys(Keys.TAB);

        Assert.assertTrue(proceedIsDisabled(), "Proceed enabled with " + fieldId + " cleared");
    }


    @Test
    public void manualAddressOverridesLookup(){
        fillBilling(new BillingData("Egypt","11511","15","My Street","My City","My State"));

        Assert.assertEquals(find_element(By.id("street")).getDomProperty("value").trim(), "My Street", "Lookup overwrote the manually entered street");
    }

    @Test(dataProvider = "maxLength")
    public void fieldEnforcesMaxLength(String fieldId,int limit){
        BillingData data = new BillingData("Egypt","11511","15","","","");
        fillBilling(data);

        WebElement target =  waitForNonEmptyValue(By.id(fieldId));
        target.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        target.sendKeys("a".repeat(limit + 1));

        Assert.assertTrue(proceedIsDisabled(), "Proceed enabled with " + fieldId + " has more than max length");

    }

}
