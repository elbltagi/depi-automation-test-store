package Toolshop.bugreport;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.IConfigurationListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Turns every failure in a run into a defect entry and writes them to one
 * self-contained HTML page.
 *
 *   test-output/bug-reports/bug-report-&lt;timestamp&gt;.html   one per run, kept as history
 *   test-output/bug-reports/bug-report-latest.html         stable name
 *
 * Registered through META-INF/services/org.testng.ITestNGListener rather than
 * testng.xml, so a single test run from the IDE produces a report too. Declaring
 * it in both places would double every entry.
 *
 * It implements IConfigurationListener as well as ITestListener because every base
 * class in this suite does its real work in @BeforeTest / @BeforeMethod: without
 * the configuration hook a broken setup would reach the report only as a run of
 * unexplained skips, with no screenshot and no stack trace.
 */
public class BugReportListener implements ITestListener, IConfigurationListener, ISuiteListener {

    private static final List<Bug> BUGS = new CopyOnWriteArrayList<>();
    private static final AtomicInteger SEQUENCE = new AtomicInteger();
    private static final AtomicInteger PASSED = new AtomicInteger();
    private static final AtomicInteger FAILED = new AtomicInteger();
    private static final AtomicInteger SKIPPED = new AtomicInteger();

    private static final Path OUT_DIR = Path.of("test-output", "bug-reports");
    private static final DateTimeFormatter FILE_STAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter HUMAN_STAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** TestNG and JUnit both render assertion mismatches this way. */
    private static final Pattern EXPECTED_ACTUAL =
            Pattern.compile("expected \\[(.*?)\\] but found \\[(.*?)\\]", Pattern.DOTALL);

    @Override
    public void onTestSuccess(ITestResult result) {
        PASSED.incrementAndGet();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        SKIPPED.incrementAndGet();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        FAILED.incrementAndGet();
        BUGS.add(toBug(result, false));
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        BUGS.add(toBug(result, true));
    }

    @Override
    public void onFinish(ISuite suite) {
        if (BUGS.isEmpty() && PASSED.get() == 0 && FAILED.get() == 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String html = BugReportHtml.render(
                suite.getName(), now.format(HUMAN_STAMP), List.copyOf(BUGS),
                PASSED.get(), FAILED.get(), SKIPPED.get());
        try {
            Files.createDirectories(OUT_DIR);
            Path stamped = OUT_DIR.resolve("bug-report-" + now.format(FILE_STAMP) + ".html");
            Files.writeString(stamped, html, StandardCharsets.UTF_8);
            Files.writeString(OUT_DIR.resolve("bug-report-latest.html"), html, StandardCharsets.UTF_8);
            System.out.println("[bug-report] " + BUGS.size() + " defect(s) -> "
                    + stamped.toAbsolutePath());
        } catch (Exception e) {
            // A reporting failure must never be mistaken for a test failure.
            System.err.println("[bug-report] could not write the report: " + e);
        }
    }

    private Bug toBug(ITestResult result, boolean configuration) {
        Throwable error = result.getThrowable();
        WebDriver driver = DriverProbe.find(result.getInstance());

        String simpleClass = result.getTestClass().getRealClass().getSimpleName();
        String method = result.getMethod().getMethodName();
        String message = error == null ? "" : String.valueOf(error.getMessage()).trim();

        String expected = "";
        String actual = "";
        Matcher m = EXPECTED_ACTUAL.matcher(message);
        if (m.find()) {
            expected = m.group(1);
            actual = m.group(2);
        }

        return new Bug(
                String.format("BUG-%03d", SEQUENCE.incrementAndGet()),
                title(simpleClass, method, message, configuration),
                severity(error, configuration),
                type(error, configuration),
                module(result.getTestClass().getName()),
                result.getTestClass().getName(),
                method,
                parameters(result),
                message,
                expected,
                actual,
                stackTrace(error),
                DriverProbe.screenshot(driver),
                DriverProbe.url(driver),
                DriverProbe.browser(driver),
                HUMAN_STAMP.format(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(result.getEndMillis()), ZoneId.systemDefault())),
                result.getEndMillis() - result.getStartMillis(),
                "mvn test -Dtest=" + simpleClass + "#" + method);
    }

    /**
     * "validateProfileData" -> "Validate profile data", plus the first line of the failure.
     * Configuration methods are named after the class instead, because every base class
     * calls its hook "setup" and "Setup failed: Setup" tells nobody which suite broke.
     */
    private static String title(String simpleClass, String method, String message, boolean configuration) {
        String subject;
        if (configuration) {
            subject = "Setup failed: " + simpleClass + "." + method;
        } else {
            String words = method.replaceAll("([a-z0-9])([A-Z])", "$1 $2").replace('_', ' ').toLowerCase();
            subject = words.isEmpty() ? method
                    : Character.toUpperCase(words.charAt(0)) + words.substring(1);
        }
        String firstLine = message.isBlank() ? "" : message.lines().findFirst().orElse("").trim();
        // The values get their own columns, so the title keeps only the assertion message.
        int valuesStart = firstLine.indexOf("expected [");
        if (valuesStart >= 0) {
            firstLine = firstLine.substring(0, valuesStart).trim();
        }
        if (firstLine.length() > 90) {
            firstLine = firstLine.substring(0, 89) + "…";
        }
        return firstLine.isEmpty() ? subject : subject + " - " + firstLine;
    }

    /**
     * A starting point, not a verdict: a failure in setup blocks everything behind it,
     * an assertion mismatch is a functional defect, and a stale element is usually the
     * test racing the page rather than a bug in the application.
     */
    private static String severity(Throwable error, boolean configuration) {
        if (configuration) {
            return "Blocker";
        }
        if (error instanceof StaleElementReferenceException) {
            return "Minor";
        }
        if (error instanceof AssertionError
                || error instanceof TimeoutException
                || error instanceof NoSuchElementException) {
            return "Major";
        }
        return "Critical";
    }

    private static String type(Throwable error, boolean configuration) {
        if (configuration) {
            return "Setup failure";
        }
        if (error instanceof AssertionError) {
            return "Assertion failed";
        }
        if (error instanceof TimeoutException) {
            return "Timed out waiting for the page";
        }
        if (error instanceof NoSuchElementException) {
            return "Element not found";
        }
        if (error instanceof StaleElementReferenceException) {
            return "Stale element (page re-rendered)";
        }
        if (error instanceof WebDriverException) {
            return "WebDriver error";
        }
        return error == null ? "Unknown" : error.getClass().getSimpleName();
    }

    /** Toolshop.Checkout.CartTests -> Checkout. */
    private static String module(String fqcn) {
        String[] parts = fqcn.split("\\.");
        return parts.length >= 3 ? parts[1] : (parts.length > 0 ? parts[0] : "");
    }

    /** @DataProvider arguments, so a defect names the row that triggered it. */
    private static String parameters(ITestResult result) {
        Object[] args = result.getParameters();
        if (args == null || args.length == 0) {
            return "";
        }
        return Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", "));
    }

    private static String stackTrace(Throwable error) {
        if (error == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        error.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
