package com.testingblaze.misclib;

import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.WebDriver;

public final class Browser {

    private final WebDriver driver;
    private final JavaScript javaScript;

    public Browser() {
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
        this.javaScript = InstanceRecording.getInstance(JavaScript.class);
    }

    /**
     * close the current tab
     *
     * @author nauman.shahid
     */
    public void closeBrowserOrTab() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Closed Tab " + driver.getTitle());
        driver.close();
    }

    /**
     * navigate back from current page
     *
     * @author nauman.shahid
     */
    public void navigateBack() {
        driver.navigate().back();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Navigated Back");
    }

    /**
     * navigate forward from current page
     *
     * @author nauman.shahid
     */
    public void navigateForward() {
        driver.navigate().forward();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Navigated Forward");
    }

    /**
     * navigate to a specific url
     *
     * @param url
     * @author nauman.shahid
     */
    public void navigateToUrl(String url) {
        driver.navigate().to(url);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Navigated to URL " + url);
    }

    /**
     * refresh page
     *
     * @author nauman.shahid
     */
    public void refreshPage() {
        driver.navigate().refresh();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Refreshed page");
    }

    /**
     * get the current url of web page
     *
     * @author nauman.shahid
     */
    public String getCurrentUrl() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Current url is " + driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    /**
     * @return status of the page load like Completed , loading , Interactive
     */
    public String getJQueryStatus() {
        return javaScript.getJQueryStatus();
    }

    /**
     * @return status of the page load like Completed , loading , Interactive
     */
    public String getPageLoadStatus() {
        return javaScript.getPageLoadStatus();
    }

    /**
     * get complete current page source
     *
     * @author nauman.shahid
     */
    public String getPageSource() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fetching complete page source");
        return driver.getPageSource();
    }

    /**
     * get title of current web page
     *
     * @author nauman.shahid
     */
    public String getPageTitle() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fetched title " + driver.getTitle());
        return driver.getTitle();
    }

    /**
     * Get current double value of current off set or movement of the page from original position
     *
     * @return double x-axis
     * @author nauman.shahid
     */
    public double getPageXOffSet() {
        return javaScript.getPageOffSetXAxis();
    }

    /**
     * Get current double value of current off set or movement of the page from original position
     *
     * @return double y-axis
     * @author nauman.shahid
     */
    public double getPageYOffSet() {
        return javaScript.getPageOffSetYAxis();
    }

}
