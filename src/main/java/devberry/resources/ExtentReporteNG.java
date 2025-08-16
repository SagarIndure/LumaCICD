package devberry.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporteNG {

    public static ExtentReports getReportObject() {

    String path = System.getProperty("user.dir") + "//reports//index.html";
    ExtentSparkReporter reporter = new ExtentSparkReporter(path);
    reporter.config().setReportName("DevBerry Automation Results");
    reporter.config().setDocumentTitle("Devberry Test Results");
    ExtentReports extent = new ExtentReports();
    extent.attachReporter(reporter);
    extent.setSystemInfo("Tester", "DevBerry Team");
    return extent;
}
}
