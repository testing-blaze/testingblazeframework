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
     * get current date in standard format
     *
     * @author nauman.shahid
     */
    public String currentDate() {
        return currentDateWithOffsetInDesiredFormat(0, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    /**
     * get current date in desired format
     *
     * @param formatter
     * @author john.philips
     */
    public String currentDateInDesiredFormat(DateTimeFormatter formatter) {
        return currentDateWithOffsetInDesiredFormat(0, formatter);
    }

    /**
     * get current date with given offset in format "MM/dd/yyyy"
     *
     * @param offset
     * @author john.philips
     */
    public String currentDateWithOffset(int offset) {
        return currentDateWithOffsetInDesiredFormat(offset, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    /**
     * get current date in desired format with offset
     *
     * @param offset    The number of days since the current day (Negative number for days in the past)
     * @param formatter DateTimeFormatter in the desired format (created with DateTimeFormatter.ofPattern(String))
     * @return current date after offset
     * @author john.philips
     */
    public String currentDateWithOffsetInDesiredFormat(int offset, DateTimeFormatter formatter) {
        return givenDateWithOffsetFromGivenDate(LocalDateTime.now().format(formatter), offset, formatter);
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
    public String givenDateWithOffsetFromGivenDate(String date, int offset, DateTimeFormatter formatter) {
        String receivedDate = "";
        try {
            receivedDate = LocalDate.parse(date, formatter).plusDays(offset).format(formatter);
        } catch (Exception e) {
            try {
                receivedDate = LocalDateTime.parse(date, formatter).plusDays(offset).format(formatter);
            } catch (Exception f) {
                 I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR,
                         "Could not get date for the following reasons: \n" + e.getMessage() + "\n" + f.getMessage());
            }
        }
        return receivedDate;
    }
}
