package Toolshop.bugreport;

/**
 * One defect as it appears in the HTML bug report. Everything here is captured at
 * the moment the test failed: once the @AfterTest hook quits the driver, the page,
 * the URL and the screenshot are gone for good.
 */
public record Bug(
        String id,
        String title,
        String severity,
        String type,
        String module,
        String testClass,
        String testMethod,
        String parameters,
        String message,
        String expected,
        String actual,
        String stackTrace,
        String screenshotBase64,
        String url,
        String browser,
        String timestamp,
        long durationMs,
        String reproCommand) {
}
