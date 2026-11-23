# Test summary reporting

Every run writes an ExtentReports HTML summary. No extra command, no flag, and no
test class has to know about it.

```
test-output/reports/test-summary-<yyyy-MM-dd_HH-mm-ss>.html   one per run, kept as history
test-output/reports/test-summary-latest.html                  same run, stable name for CI links
```

The run prints both paths when it finishes. `test-output/` is gitignored - the
report is a build artifact, not a committed document.

## Running

```
mvn test                                        # surefire default: *Tests / *Test classes
mvn test -Dtest=PaymentTests                    # one class
mvn test -Psuite                                # full suite, all 10 classes, in order
```

In PowerShell, the equivalent long form needs the quotes - `mvn test "-Dsurefire.suiteXmlFiles=testng.xml"` -
because PowerShell splits the bare argument at the dot and Maven then reads
`.suiteXmlFiles=testng.xml` as a lifecycle phase. The `-Psuite` profile exists to
sidestep that.

`testng.xml` exists because surefire's default pattern skips `Login`, `Rigster`,
`filters`, `BillingAddress` and `end_to_end` - their names do not end in `Test`
or `Tests`. The suite file is the only way to run everything in one pass without
renaming those classes.

## What the report contains

- pass / fail / skip counts and total duration
- one entry per test method, tagged with its package (`Auth`, `Checkout`, `product`, …)
- `@DataProvider` parameters for each invocation
- full stack trace of every failure
- **a screenshot taken at the moment of failure**, embedded as base64, so the HTML
  is a single self-contained file you can attach to a bug report
- setup failures: a `@BeforeMethod` blowup gets its own red entry with a screenshot,
  instead of only showing up as a row of unexplained skips

## How it is wired

| File | Role |
|---|---|
| `reporting/ExtentManager.java` | the single `ExtentReports` instance, report paths, system-info block |
| `reporting/ExtentReportListener.java` | `ITestListener` + `ISuiteListener` + `IConfigurationListener` |
| `reporting/ScreenshotUtil.java` | finds the `WebDriver` on the failing test instance by reflection |
| `src/test/resources/META-INF/services/org.testng.ITestNGListener` | auto-registers the listener |

The listener is registered through TestNG's ServiceLoader file, not in `testng.xml`.
That means a single test launched from IntelliJ produces a report too. Do not also
declare it in `testng.xml` - it would be registered twice and every entry would appear
twice.

`ScreenshotUtil` uses reflection on purpose: the suite has several independent base
classes (`Checkout.Base`, `product.ProductBase`, the `Auth` classes) that each declare
their own driver field. Walking the class hierarchy for a `WebDriver`-typed field meant
no existing test or base class had to change to get failure screenshots.

## Adding a step line

Optional, for tests where the report should read as a narrative:

```java
ExtentReportListener.log("Selecting bank transfer");
```
