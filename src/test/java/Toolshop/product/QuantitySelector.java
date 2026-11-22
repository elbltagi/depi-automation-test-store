package Toolshop.product;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class QuantitySelector extends ProductBase {

    private static final By PLUS = By.cssSelector("[data-test='increase-quantity']");
    private static final By MINUS = By.cssSelector("[data-test='decrease-quantity']");

    @BeforeMethod(dependsOnMethods = "openBrowser")
    public void openProduct() {
        openFirstProduct();
        find_element(QUANTITY);
    }

    @Test(description = "TS-PRD-04 Quantity increment")
    public void incrementIncreasesByOne() {
        wait.until(ExpectedConditions.elementToBeClickable(PLUS)).click();

        waitForValue(QUANTITY, "2");
        Assert.assertEquals(valueOf(QUANTITY), "2");
    }

    @Test(description = "TS-PRD-05 Quantity decrement")
    public void decrementDecreasesByOne() {
        wait.until(ExpectedConditions.elementToBeClickable(PLUS)).click();
        waitForValue(QUANTITY, "2");

        wait.until(ExpectedConditions.elementToBeClickable(MINUS)).click();

        waitForValue(QUANTITY, "1");
        Assert.assertEquals(valueOf(QUANTITY), "1");
    }

    @Test(description = "TS-PRD-06 Decrementing at one does not go below the minimum")
    public void decrementAtOneStaysOne() {
        wait.until(ExpectedConditions.elementToBeClickable(MINUS)).click();

        Assert.assertEquals(valueOf(QUANTITY), "1", "Quantity went below the minimum of 1");
    }

    @Test(description = "TS-PRD-06 Typing zero is rejected")
    public void typingZeroIsNotAccepted() {
        setQuantity("0");

        Assert.assertNotEquals(valueOf(QUANTITY), "0", "The quantity field accepted 0");
    }

    @Test(description = "TS-PRD-12 Typing a valid quantity is kept")
    public void typingFiveIsAccepted() {
        setQuantity("5");

        waitForValue(QUANTITY, "5");
        Assert.assertEquals(valueOf(QUANTITY), "5");
    }
}
