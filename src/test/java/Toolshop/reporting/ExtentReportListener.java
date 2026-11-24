package Toolshop.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.IConfigurationListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class ExtentReportListener
        implements ITestListener, ISuiteListener, IConfigurationListener {

    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    /** Optional: add a step line to the report from inside a test. */
    public static void log(String message) {
        ExtentTest test = CURRENT.get();
        if (test != null) {
            test.info(message);
        }
    }

    @Override
    public void onStart(ISuite suite) {
        ExtentManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance().createTest(testName(result), description(result));
        test.assignCategory(result.getTestClass().getRealClass()
                .getPackageName().replace("Toolshop.", ""));
        for (String group : result.getMethod().getGroups()) {
            test.assignCategory(group);
        }
        if (result.getParameters().length > 0) {
            test.info("Parameters: " + Arrays.toString(result.getParameters()));
        }
        CURRENT.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        entry(result).pass("Passed in " + durationSeconds(result) + "s");
        CURRENT.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = entry(result);
        test.fail(result.getThrowable());

        String screenshot = ScreenshotUtil.captureBase64(result);
        if (screenshot != null) {
            test.fail("Screenshot at failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
        }
        CURRENT.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = entry(result);
        if (result.getThrowable() == null) {
            test.log(Status.SKIP, "Skipped");
        } else {
            test.log(Status.SKIP, result.getThrowable());
        }
        CURRENT.remove();
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        // every base class does its real work in @BeforeMethod, so a setup blowup
        // would otherwise reach the report only as a run of unexplained skips
        ExtentTest test = ExtentManager.getInstance()
                .createTest(testName(result), "setup failed");
        test.fail(result.getThrowable());

        String screenshot = ScreenshotUtil.captureBase64(result);
        if (screenshot != null) {
            test.fail("Screenshot at setup failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.getInstance().flush();
        System.out.println("[report] test summary : " + ExtentManager.getReportPath());
        System.out.println("[report] latest       : test-output/reports/test-summary-latest.html");
    }

    /**
     * A test skipped because its setup or a dependency failed never got an
     * onTestStart, so no entry is waiting for it - create one.
     */
    private ExtentTest entry(ITestResult result) {
        ExtentTest test = CURRENT.get();
        if (test == null) {
            test = ExtentManager.getInstance().createTest(testName(result), description(result));
            CURRENT.set(test);
        }
        return test;
    }

    private String testName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
                + "." + result.getMethod().getMethodName();
    }

    private String description(ITestResult result) {
        String d = result.getMethod().getDescription();
        return (d == null || d.isBlank()) ? "" : d;
    }

    private String durationSeconds(ITestResult result) {
        return String.format("%.1f", (result.getEndMillis() - result.getStartMillis()) / 1000.0);
    }
}
