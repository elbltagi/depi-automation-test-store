package Toolshop.Checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Locale;

public class CartTests extends Base{

    private void setQuantity(String value) {
        find_element(quantityBy).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, value, Keys.ENTER);
    }

    @Test
    void validate_correct_product(){
        SoftAssert soft = new SoftAssert();

        String title = find_element(By.cssSelector("span.product-title")).getText().trim();
        String price = find_element(By.cssSelector("span[data-test='product-price']")).getText().trim().replace("$", "");

        soft.assertEquals(title, product_test.title, "Title mismatch");
        soft.assertEquals(price, product_test.price, "Price mismatch");
        soft.assertAll();
    }

    @Test
    void enter_valid_number_quantity(){
        setQuantity("4");

        wait.until(ExpectedConditions.attributeToBe(quantityBy,"value","4"));
        Assert.assertEquals(find_element(quantityBy).getDomProperty("value"), "4");
    }

    @Test
    void enter_zero_quantity(){
        WebElement quantity = find_element(quantityBy);

        setQuantity("0");

        wait.until(ExpectedConditions.attributeToBe(quantityBy,"value","1"));
        Assert.assertEquals(find_element(quantityBy).getDomProperty("value"),"1");
    }

    @Test
    void enter_more_than_99(){
        setQuantity("120");

        WebElement toast = find_element(By.cssSelector("div.ngx-toastr.toast-warning"));
        Assert.assertEquals(toast.getText().trim(),"You can order at most 99 of this product.");
    }

    @Test
    void check_total(){
        setQuantity("3");

        double total = Double.parseDouble(product_test.price) * 3;
        String expected = String.format(Locale.US, "%.2f", total);

        By linePriceBy = By.cssSelector("span[data-test='line-price']");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(linePriceBy, expected));

        String total_line = find_element(By.cssSelector("span[data-test=\"line-price\"]")).getText().trim().replace("$","");
        Assert.assertEquals(Double.parseDouble(total_line),total,0.01);
    }

    @Test
    void enter_99_quantity(){
        WebElement quantity = find_element(quantityBy);

        setQuantity("99");

        wait.until(ExpectedConditions.attributeToBe(quantityBy,"value","99"));
        Assert.assertEquals(find_element(quantityBy).getDomProperty("value"),"99");
    }

    @Test
    void enter_negative_quantity(){
        WebElement quantity = find_element(quantityBy);

        setQuantity("-1");

        wait.until(ExpectedConditions.attributeToBe(quantityBy,"value","1"));
        Assert.assertEquals(find_element(quantityBy).getDomProperty("value"),"1");
    }

    @Test
    void removing_product(){
        WebElement remove_btn = find_element(By.cssSelector("a.btn.btn-danger"));

        remove_btn.click();

        String toast_msg = find_element(By.cssSelector("#toast-container div.ngx-toastr")).getText().trim();

        Assert.assertEquals(toast_msg,"Product deleted.");
    }

    @Test
    void adding_multi_add_to_cart (){
        driver.navigate().back();

        WebElement add_btn = find_element(By.id("btn-add-to-cart"));

        WebElement input_quantity = find_element(By.id("quantity-input"));

        input_quantity.clear();
        input_quantity.sendKeys("99");

        add_btn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.ngx-toastr.toast-success")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.ngx-toastr.toast-success")));

        add_btn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.ngx-toastr.toast-success")));


        driver.navigate().forward();

        Assert.assertEquals(find_element(quantityBy).getAttribute("value"),"99","maximum value is 99 and there more than 99 in cart");
    }
    @Test
    void adding_another_product(){
        driver.navigate().to("https://practicesoftwaretesting.com/");

        List<WebElement> productCards = find_elements(By.cssSelector("a.card"));
        productCards.get(1).click();
         WebElement add_btn = find_element(By.id("btn-add-to-cart"));
        add_btn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.ngx-toastr.toast-success")));

        driver.navigate().to("https://practicesoftwaretesting.com/checkout");

        List<WebElement> products_cart = find_elements(By.cssSelector("span[data-test=\"line-price\"]"));

        Assert.assertEquals(products_cart.size(),2);
        double total = 0;
        for (WebElement el : products_cart) {
            String price = el.getText().replace("$","").trim();
            total+=Double.parseDouble(price);
        }

        String cart_total = find_element(By.cssSelector("td[data-test=\"cart-total\"]")).getText().replace("$","").trim();

        Assert.assertEquals(Double.parseDouble(cart_total),total,0.01);
    }

    @Test
    void proceedToSignIn(){
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-1']"))).click();
        Assert.assertTrue(find_element(By.cssSelector("[data-test='email']")).isDisplayed(), "Sign-in step did not open");
    }
       
}
