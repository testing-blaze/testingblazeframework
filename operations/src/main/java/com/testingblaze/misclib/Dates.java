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

public class Dates {

    private WebDriver driver;
    private JavaScript javaScript;
    private KeysHandler key;
    private Element elementApi;

    public Dates() {
        elementApi = InstanceRecording.getInstance(Element.class);
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
        this.javaScript = InstanceRecording.getInstance(JavaScript.class);
        this.key = new KeysHandler();
    }

    /**
     * get current date in format "MM/dd/yyyy"
     *
     * @author nauman.shahid
     */
    public String getCurrentDate() {
        return currentDateWithOffsetInDesiredFormat(0, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    /**
     * get current date in desired format
     *
     * @param formatter
     * @author john.philips
     */
    public String getCurrentDateInDesiredFormat(DateTimeFormatter formatter) {
        return currentDateWithOffsetInDesiredFormat(0, formatter);
    }

    /**
     * get current date in desired format
     *
     * @param formatter
     * @author nauman.shahid
     */
    public String getCurrentDateTimeInDesiredFormat(DateTimeFormatter formatter) {
        return currentDateTimeWithOffsetInDesiredFormat(0, formatter);
    }

    /**
     * get current date in standard format with offset
     *
     * @param offset
     * @author john.philips
     */
    public String currentDateWithOffset(int offset) {
        return currentDateWithOffsetInDesiredFormat(offset, DateTimeFormatter.ofPattern("MM/dd/YYYY"));
    }

    /**
     * get current date in desired format with offset
     *
     * @param offset    The number of days since the current day (Negative number for days in the past)
     * @param formatter DateTimeFormatter in the desired format (created with DateTimeFormatter.ofPattern(String))
     * @return current date after off set
     * @author john.philips
     */
    public String currentDateWithOffsetInDesiredFormat(int offset, DateTimeFormatter formatter) {
        return dateWithOffsetFromGivenDate(LocalDate.now().format(formatter), offset, formatter);
    }

    /**
     * get current date in desired format with offset
     *
     * @param date      The date from which the offset should come
     * @param offset    The number of days since the current day (Negative number for days in the past)
     * @param formatter DateTimeFormatter in the desired format (created with DateTimeFormatter.ofPattern(String))
     * @return date after off set
     * @author john.philips
     */
    public String dateWithOffsetFromGivenDate(String date, int offset, DateTimeFormatter formatter) {
        String receivedDate = LocalDate.parse(date, formatter).plusDays(offset).format(formatter);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fetched date is : " + receivedDate);
        return receivedDate;
    }

    /**
     * get current date in desired format with offset
     *
     * @param offset    The number of days since the current day (Negative number for days in the past)
     * @param formatter DateTimeFormatter in the desired format (created with DateTimeFormatter.ofPattern(String))
     * @return current date after off set
     * @author john.philips
     */
    public String currentDateTimeWithOffsetInDesiredFormat(int offset, DateTimeFormatter formatter) {
        return dateTimeWithOffsetFromGivenDate(LocalDateTime.now().format(formatter), offset, formatter);
    }

    /**
     * get current date in desired format with offset
     *
     * @param date      The date from which the offset should come
     * @param offset    The number of days since the current day (Negative number for days in the past)
     * @param formatter DateTimeFormatter in the desired format (created with DateTimeFormatter.ofPattern(String))
     * @return date after off set
     * @author john.philips
     */
    public String dateTimeWithOffsetFromGivenDate(String date, int offset, DateTimeFormatter formatter) {
        String receivedDate = LocalDateTime.parse(date, formatter).plusDays(offset).format(formatter);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fetched date is : " + receivedDate);
        return receivedDate;
    }

}
