package com.testingblaze.misclib;

import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class Browser {

    private WebDriver driver;
    private JavaScript javaScript;
    private KeysHandler key;
    private Element elementApi;

    public Browser() {
        elementApi = InstanceRecording.getInstance(Element.class);
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
        this.javaScript = InstanceRecording.getInstance(JavaScript.class);
        if (key == null) {
            this.key = new KeysHandler();
        }
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
     * refresh page
     *
     * @author nauman.shahid
     */
    public void refreshPage() {
        driver.navigate().refresh();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Refreshed page");
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
     * @return status of the page load like Completed , loading , Interactive
     */
    public String getPageLoadStatus() {
        return javaScript.getPageLoadStatus();
    }

    /**
     * @return status of the page load like Completed , loading , Interactive
     */
    public String getJQueryStatus() {
        return javaScript.getJQueryStatus();
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
     * get complete current page source
     *
     * @author nauman.shahid
     */
    public String getPageSource() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fetching complete page source");
        return driver.getPageSource();
    }

    /**
     * get the current url of web page
     *
     * @author nauman.shahid
     */
    public String getCurrentURL() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Current url is " + driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    /**
     * Get current double value of current off set or movement of the page from original position
     *
     * @return double y-axis
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
