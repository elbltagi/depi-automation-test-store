package Toolshop.reporting;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.lang.reflect.Field;

/**
 * Finds the WebDriver on whichever test instance just failed, without any base
 * class having to cooperate.
 *
 * The suite has several independent bases (Checkout.Base, product.ProductBase,
 * the Auth classes) that each declare their own driver field, so this walks the
 * class hierarchy looking for the first WebDriver-typed field instead of
 * requiring a shared interface. That keeps reporting additive: no existing test
 * had to change to get a failure screenshot.
 */
public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    /** base64 PNG, or null when the failure happened before a driver existed. */
    public static String captureBase64(ITestResult result) {
        WebDriver driver = findDriver(result.getInstance());
        if (!(driver instanceof TakesScreenshot shooter)) {
            return null;
        }
        try {
            return shooter.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("[report] could not capture screenshot: " + e.getMessage());
            return null;
        }
    }

    private static WebDriver findDriver(Object instance) {
        for (Class<?> type = instance == null ? null : instance.getClass();
             type != null && type != Object.class;
             type = type.getSuperclass()) {

            for (Field field : type.getDeclaredFields()) {
                if (!WebDriver.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(instance);
                    if (value != null) {
                        return (WebDriver) value;
                    }
                } catch (ReflectiveOperationException | RuntimeException ignored) {
                    // unreachable field: try the next one
                }
            }
        }
        return null;
    }
}
