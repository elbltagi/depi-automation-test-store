package Toolshop.product;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ProductDetails extends ProductBase {

    @Test(description = "TS-PRD-01 Product information renders")
    public void productInformationIsDisplayed() {
        openFirstProduct();

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(find_element(PRODUCT_NAME).isDisplayed(), "Product name is missing");
        soft.assertTrue(find_element(UNIT_PRICE).isDisplayed(), "Unit price is missing");
        soft.assertTrue(find_element(By.cssSelector("[data-test='product-description']")).isDisplayed(),
                "Description is missing");
        soft.assertTrue(find_element(By.cssSelector("img.figure-img")).isDisplayed(), "Product image is missing");
        soft.assertAll();
    }

    @Test(description = "TS-PRD-02 Details match the catalog card")
    public void productMatchesCatalogCard() {
        driver.get(HOME);

        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_CARD));
        String nameOnCard = card.findElement(PRODUCT_NAME).getText().trim();
        String priceOnCard = card.findElement(By.cssSelector("[data-test='product-price']")).getText().trim();
        card.click();

        Assert.assertEquals(find_element(PRODUCT_NAME).getText().trim(), nameOnCard,
                "Name on the details page differs from the card");
        Assert.assertEquals(find_element(UNIT_PRICE).getText().trim(), priceOnCard,
                "Price on the details page differs from the card");
    }

    @Test(description = "TS-PRD-03 Add to cart, single item")
    public void addToCartShowsOneItem() {
        openFirstProduct();

        find_element(ADD_TO_CART).click();

        wait.until(ExpectedConditions.textToBe(CART_BADGE, "1"));
        Assert.assertEquals(find_element(CART_BADGE).getText().trim(), "1", "Cart badge did not reach 1");
    }

    @Test(description = "TS-PRD-07 Add to cart with a quantity above one")
    public void addToCartKeepsChosenQuantity() {
        openFirstProduct();

        setQuantity("3");
        find_element(ADD_TO_CART).click();

        wait.until(ExpectedConditions.textToBe(CART_BADGE, "3"));
        Assert.assertEquals(find_element(CART_BADGE).getText().trim(), "3", "Cart badge ignored the chosen quantity");
    }

    @Test(description = "TS-PRD-08 Adding the same product twice merges into one line")
    public void addingSameProductTwiceMergesLines() {
        openFirstProduct();

        find_element(ADD_TO_CART).click();
        wait.until(ExpectedConditions.textToBe(CART_BADGE, "1"));

        find_element(ADD_TO_CART).click();
        wait.until(ExpectedConditions.textToBe(CART_BADGE, "2"));

        find_element(By.cssSelector("[data-test='nav-cart']")).click();
        find_element(By.cssSelector("[data-test='product-title']"));

        Assert.assertEquals(driver.findElements(By.cssSelector("[data-test='product-title']")).size(), 1, "The same product was added as a second cart line");
        Assert.assertEquals(valueOf(By.cssSelector("[data-test='product-quantity']")), "2", "Cart line quantity did not merge to 2");
    }
}
