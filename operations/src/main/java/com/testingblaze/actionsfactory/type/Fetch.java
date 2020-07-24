/*
 * Copyright 2020
 *
 * This file is part of Testing Blaze Automation Solution.
 *
 * Testing Blaze Automation Solution is licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.testingblaze.actionsfactory.type;

import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.misclib.KeysHandler;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;


/**
 * get various functionalities for dates , urls , browser handling , navigations etc.
 *
 * @author nauman.shahid
 */
public final class Fetch {
    private WebDriver driver;
    private JavaScript javaScript;
    private KeysHandler key;
    private Element elementApi;

    public Fetch() {
        elementApi = InstanceRecording.getInstance(Element.class);
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
        this.javaScript = InstanceRecording.getInstance(JavaScript.class);
        this.key = new KeysHandler();
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().navigateToUrl(url)
     * navigate to a specific url
     *
     * @param url
     * @author nauman.shahid
     */
    public void ToUrl(String url) {
        driver.navigate().to(url);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Navigated to URL " + url);
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().getPageLoadStatus()
     * @return status of the page load like Completed , loading , Interactive
     */
    public String pageLoadStatus() {
        return javaScript.getPageLoadStatus();
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().getJQueryStatus()
     * @return status of the page load like Completed , loading , Interactive
     */
    public String jQueryStatus() {
        return javaScript.getJQueryStatus();
    }


    /**
     * get inner text on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @return inner text
     * @author nauman.shahid
     */
    public <T> String text(T locator) {
        return text(locator, true);
    }

    /**
     * get inner text on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @return inner text
     * @author nauman.shahid
     */
    public <T> String text(T locator, Boolean processing) {
        return elementApi.locator(locator, processing).getText();
    }

    /**
     * get attribute value on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @return attribute value
     * @author nauman.shahid
     */
    public <T> String attribute(T locator, String attribute) {
        return attribute(locator, attribute, true);
    }

    /**
     * get attribute value on webPage , mobilePage , AngularPage
     *
     * @param locator    -> Mobile , Ng , By :
     *                   Mobile.
     *                   Angular.
     *                   By.
     * @param processing
     * @return attribute value
     * @author nauman.shahid
     */
    public <T> String attribute(T locator, String attribute, Boolean processing) {
        return elementApi.locator(locator, processing).getAttribute(attribute);
    }

    /**
     * get css property on webPage , mobilePage , AngularPage
     *
     * @param locator  -> Mobile , Ng , By :
     *                 Mobile.
     *                 Angular.
     *                 By.
     * @param property
     * @return css value
     * @author nauman.shahid
     */
    public <T> String cssProperty(T locator, String property) {
        return cssProperty(locator, property, true);
    }

    /**
     * get css property on webPage , mobilePage , AngularPage
     *
     * @param locator    -> Mobile , Ng , By :
     *                   Mobile.
     *                   Angular.
     *                   By.
     * @param property
     * @param processing
     * @return css value
     * @author nauman.shahid
     */
    public <T> String cssProperty(T locator, String property, Boolean processing) {
        return elementApi.locator(locator, processing).getCssValue(property);
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().navigateBack()
     * navigate back from current page
     *
     * @author nauman.shahid
     */
    public void navigateBack() {
        driver.navigate().back();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Navigated Back");
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().navigateForward()
     * navigate forward from current page
     *
     * @author nauman.shahid
     */
    public void navigateForward() {
        driver.navigate().forward();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Navigated Forward");
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().refreshPage()
     * refresh page
     *
     * @author nauman.shahid
     */
    public void pageRefresh() {
        driver.navigate().refresh();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Refreshed page");
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().navigateToUrl(url)
     * open any url
     *
     * @param url
     */
    public void newURL(String url) {
        driver.get(url);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Entered URL " + url);
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().getPageTitle()
     * get title of current web page
     *
     * @author nauman.shahid
     */
    public String pageTitle() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fetched title " + driver.getTitle());
        return driver.getTitle();
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().getPageSource()
     * get complete current page source
     *
     * @author nauman.shahid
     */
    public String pageSource() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fetching complete page source");
        return driver.getPageSource();
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().getCurrentUrl()
     * get the current url of web page
     *
     * @author nauman.shahid
     */
    public String currentURL() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Current url is " + driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    /**
     * @deprecated Use I.amPerforming().dateOperationsToGet().currentDate()
     * get current date in standard format
     *
     * @author nauman.shahid
     */
    public String currentDate_MMDDYYYY() {
        return currentDateWithOffsetInDesiredFormat(0, DateTimeFormatter.ofPattern("MM/dd/YYYY"));
    }

    /**
     * @deprecated Use I.amPerforming().dateOperationsToGet().currentDateInDesiredFormat(formatter)
     *
     * get current date in desired format
     *
     * @param formatter
     * @author john.philips
     */
    public String currentDateInDesiredFormat(DateTimeFormatter formatter) {
        return currentDateWithOffsetInDesiredFormat(0, formatter);
    }

    /**
     * @deprecated Use I.amPerforming().dateOperationsToGet().currentDateInDesiredFormat(formatter)
     *
     * get current date in desired format
     *
     * @param formatter
     * @author nauman.shahid
     */
    public String currentDateTimeInDesiredFormat(DateTimeFormatter formatter) {
        return currentDateTimeWithOffsetInDesiredFormat(0, formatter);
    }

    /**
     * @deprecated Use I.amPerforming().dateOperationsToGet().currentDateWithOffset(offset)
     *
     * get current date in standard format with offset
     *
     * @param offset
     * @author john.philips
     */
    public String currentDateWithOffset(int offset) {
        return currentDateWithOffsetInDesiredFormat(offset, DateTimeFormatter.ofPattern("MM/dd/YYYY"));
    }

    /**
     * @deprecated Use I.amPerforming().dateOperationsToGet().currentDateWithOffsetInDesiredFormat(offset, formatter)
     *
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
     * @deprecated Use I.amPerforming().dateOperationsToGet().givenDateWithOffsetFromGivenDate(date, offset, formatter)
     *
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
     * @deprecated Use I.amPerforming().dateOperationsToGet().currentDateWithOffsetInDesiredFormat(offset, formatter)
     *
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
     * @deprecated Use I.amPerforming().dateOperationsToGet().givenDateWithOffsetFromGivenDate(date, offset, formatter)
     *
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

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().closeBrowserOrTab()
     * close the current tab
     *
     * @author nauman.shahid
     */
    public void closeBrowserOrTab() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Closed Tab " + driver.getTitle());
        driver.close();
    }

    /**
     * @deprecated Use I.amPerforming().browserOperationsTo().getPageYOffset()
     * Get current double value of current off set or movement of the page from original position
     *
     * @return double y-axis
     * @author nauman.shahid
     */
    public double getPageOffSetYAxis() {
        return javaScript.getPageOffSetYAxis();
    }

    /**
     * @deprecated Use I.amPerforming().addOnsTo().getRandomNumberInRange(minimum, maximum)
     * Get random number within a range.
     *
     * @param minimum
     * @param maximum
     * @param <T>     Handles Double and int at the moment
     * @return random number
     * @author nauman.shahid
     */
    public <T extends Number> Number randomNumber(T minimum, T maximum) {
        if (minimum instanceof Double) {
            return ThreadLocalRandom.current().nextDouble(minimum.doubleValue(), maximum.doubleValue());
        } else {
            return ThreadLocalRandom.current().nextInt(minimum.intValue(), maximum.intValue());
        }
    }

    /**
     * @deprecated Use I.amPerforming().addOnsTo().getKeyBoard()
     * get access to keyboard
     *
     * @return
     */
    public KeysHandler keyBoard() {
        return key;
    }

}
