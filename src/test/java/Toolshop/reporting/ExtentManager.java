package Toolshop.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Owns the single ExtentReports instance for a run and decides where the HTML
 * summary is written.
 *
 * Output: test-output/reports/test-summary-TIMESTAMP.html  (one per run, history)
 *         test-output/reports/test-summary-latest.html     (stable name for CI)
 */
public final class ExtentManager {

    private static final String REPORT_DIR = "test-output" + File.separator + "reports";
    private static ExtentReports extent;
    private static String reportPath;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = create();
        }
        return extent;
    }

    public static String getReportPath() {
        return reportPath;
    }

    private static ExtentReports create() {
        new File(REPORT_DIR).mkdirs();

        String stamp = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
        reportPath = REPORT_DIR + File.separator + "test-summary-" + stamp + ".html";

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(
                spark(reportPath),
                spark(REPORT_DIR + File.separator + "test-summary-latest.html"));

        reports.setSystemInfo("Project", "Toolshop");
        reports.setSystemInfo("Environment", "https://practicesoftwaretesting.com");
        reports.setSystemInfo("Browser", "Chrome");
        reports.setSystemInfo("OS", System.getProperty("os.name"));
        reports.setSystemInfo("Java", System.getProperty("java.version"));
        return reports;
    }

    private static ExtentSparkReporter spark(String path) {
        ExtentSparkReporter spark = new ExtentSparkReporter(path);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Toolshop - Automation Test Summary");
        spark.config().setReportName("Toolshop UI Suite");
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        return spark;
    }
}
