package Toolshop.bugreport;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Field;

/**
 * Finds the WebDriver on a failing test instance by walking its class hierarchy.
 *
 * Reflection rather than a shared interface on purpose: the suite has several
 * independent base classes (Checkout.Base, Integration.Base, product.ProductBase),
 * each with its own driver field and its own visibility, and none of them should
 * have to change to get a screenshot into the report.
 *
 * Every method here is best-effort. A dead or quit driver must never turn one
 * failing test into two.
 */
final class DriverProbe {

    private DriverProbe() {
    }

    static WebDriver find(Object testInstance) {
        if (testInstance == null) {
            return null;
        }
        for (Class<?> c = testInstance.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
            for (Field f : c.getDeclaredFields()) {
                if (!WebDriver.class.isAssignableFrom(f.getType())) {
                    continue;
                }
                try {
                    f.setAccessible(true);
                    if (f.get(testInstance) instanceof WebDriver d) {
                        return d;
                    }
                } catch (Exception ignored) {
                    // inaccessible or already torn down - keep looking
                }
            }
        }
        return null;
    }

    static String screenshot(WebDriver driver) {
        if (!(driver instanceof TakesScreenshot shooter)) {
            return null;
        }
        try {
            return shooter.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            return null;
        }
    }

    static String url(WebDriver driver) {
        try {
            return driver == null ? null : driver.getCurrentUrl();
        } catch (Exception e) {
            return null;
        }
    }

    static String browser(WebDriver driver) {
        try {
            if (driver instanceof RemoteWebDriver remote) {
                var caps = remote.getCapabilities();
                return caps.getBrowserName() + " " + caps.getBrowserVersion()
                        + " on " + caps.getPlatformName();
            }
        } catch (Exception ignored) {
            // capabilities are unavailable once the session is closed
        }
        return null;
    }
}
