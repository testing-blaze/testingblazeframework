/*
 * Copyright 2020
 *
 * This file is part of  Test Blaze Bdd Framework [Test Blaze Automation Solution].
 *
 * Test Blaze Bdd Framework is licensed under the Apache License, Version
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
package com.testblaze.actionsfactory.elementfunctions;

import com.testblaze.actionsfactory.abstracts.LocatorProcessing;
import com.testblaze.controller.DeviceBucket;
import com.testblaze.misclib.ConsoleFormatter;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.register.i;
import com.testblaze.report.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;


/**
 * @author nauman.shahid
 * @REI-Systems
 * @category javascript functions library
 */

public final class JavaScript {
    JavascriptExecutor js;
    LocatorProcessing getLocator;

    public JavaScript() {
        js = (JavascriptExecutor) InstanceRecording.getInstance(DeviceBucket.class).getDriver();
        getLocator = InstanceRecording.getInstance(LocatorProcessing.class);
    }

    /**
     * clicks on specific webElement using java script
     */
    public void clickByJSWebElement(WebElement element) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Clicking with java script");
        try {
            scrollElementToPageDetailCenter(element);
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, ConsoleFormatter.setTextColor(ConsoleFormatter.COLOR.RED,"X: ")+"Unable to use JavaScript due to : "+e.getMessage());
        }
    }

    /**
     * To blink color on specifc element using java script
     */
    public void flashColor(WebElement element, String color, int BlinkNumber) {
        String oldColor = element.getCssValue("color");
        for (int i = 0; i < BlinkNumber; i++) {
            js.executeScript("arguments[0].style.color='green'",element);
            js.executeScript("arguments[0].style.color='" + color + "'",element);
        }
        js.executeScript("arguments[0].style.color='" + oldColor + "'",element);
    }

    /**
     * scroll page up,down or to any specific x-axis and y-axis using java script
     */
    public void scrollPageWindowJS(String scrolltype, int xAxis, int Yaxis) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Scrolling " + scrolltype + " on page");
        switch (scrolltype.toUpperCase()) {
            case "PAGEDOWN":
                js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
                break;
            case "PAGEUP":
                js.executeScript("window.scrollTo(0,-document.body.scrollHeight)");
                break;
            case "MYLOCATION":
                js.executeScript("window.scrollBy(\"" + xAxis + "\"", "" + Yaxis + "\")");
                break;
        }
    }

    /**
     * scroll page up,down or to any specific x-axis and y-axis using java script
     */
    public void scrollPageDocumentJS(String scrolltype, int xAxis, int Yaxis) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Scrolling " + scrolltype + " on page");
        try {
            switch (scrolltype.toUpperCase()) {
                case "PAGEDOWN":
                    js.executeScript("document.body.scrollTo(0,document.body.scrollHeight)");
                    break;
                case "PAGEUP":
                    js.executeScript("document.body.scrollTo(0,-document.body.scrollHeight)");
                    break;
                case "MYLOCATION":
                    js.executeScript("document.body.scrollBy(\"" + xAxis + "\"", "" + Yaxis + "\")");
                    break;
                default:
            }
        } catch (Exception e) {
        }
    }

    /**
     * scroll to a specific element location using java script
     */
    public void scrollpageToSpecificElement(WebElement locator) {
        js.executeScript("arguments[0].scrollIntoView(true);", locator);
    }

    /**
     * scroll a specific element to the center of the page
     * Concept for function derived from:
     * https://github.com/litera/jquery-scrollintoview
     * Copyright (c) 2011 Robert Koritnik
     * Licensed under the terms of the MIT license
     * http://www.opensource.org/licenses/mit-license.php:
     * ----------
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     * <p>
     * The above copyright notice and this permission notice shall be included in all
     * copies or substantial portions of the Software.
     * <p>
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     * SOFTWARE.
     * ----------
     * <p>
     * Modified version devised by John Phillips (12/27/2018)
     */
    public void scrollElementToPageDetailCenter(WebElement element) {
        //Console log  Attempting to scroll element to page center with JavaScript");
        String scrollElementIntoMiddle = ""
                + "function isScrollable(element) {"
                + "    var styles = ("
                + "        document.defaultView && document.defaultView.getComputedStyle ? "
                + "        document.defaultView.getComputedStyle(element, null) : element.currentStyle"
                + "    );"
                + "    var scrollValue = {"
                + "        auto: true,"
                + "        scroll: true,"
                + "        visible: false,"
                + "        hidden: false"
                + "    };"
                + "    var overflow = {"
                + "        y: scrollValue[styles.overflowY.toLowerCase()] || false,"
                + "    };"
                + "    if (!overflow.y) {"
                + "        return false;"
                + "    }"
                + "    var size = {"
                + "        height: {"
                + "            scroll: element.scrollHeight,"
                + "            client: element.clientHeight"
                + "        },"
                + "        scrollableY: function () {"
                + "            return overflow.y && this.height.scroll > this.height.client;"
                + "        }"
                + "    };"
                + "    return size.scrollableY();"
                + "}"
                + "var el = arguments[0];"
                + "if (el.tagName.toUpperCase() !== 'HTML') {"
                + "    var total = el.offsetTop;"
                + "    var parentEl = el.parentElement;"
                + "    var offsetParentEl = el.offsetParent;"
                + "    while(parentEl.tagName.toUpperCase() !== 'HTML') {"
                + "        if (isScrollable(parentEl)) {"
                + "            if (parentEl === offsetParentEl) {"
                + "                parentEl.scrollTop = total - (parentEl.clientHeight/2.0);"
                + "                offsetParentEl = parentEl.offsetParent;"
                + "            } else {"
                + "                parentEl.scrollTop = total - parentEl.offsetTop - (parentEl.clientHeight/2.0);"
                + "            }"
                + "            total = parentEl.offsetTop + (parentEl.clientHeight/2.0);"
                + "        } else if (parentEl === offsetParentEl) {"
                + "            total = total + parentEl.offsetTop;"
                + "            offsetParentEl = parentEl.offsetParent;"
                + "        }"
                + "        parentEl = parentEl.parentElement;"
                + "    }"
                + "    parentEl.scrollTop = total-(parentEl.clientHeight/2.0);"
                + "}";

        try {
            js.executeScript(scrollElementIntoMiddle, element);
        } catch (Exception e) {
        }
    }

    // only for super Element use
    public void scrollElementToPageDetailCenter(By locator) {
        By refinedFinalLocator = getLocator.get(locator);
        try {
            WebElement element = InstanceRecording.getInstance(DeviceBucket.class).getDriver().findElement(refinedFinalLocator);
            this.scrollElementToPageDetailCenter(element);
        } catch (Exception e) {
        }
    }

    /**
     * Input to any field using java script
     */
    public void InputJSByWebElement(WebElement element, String input) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Entering text with java script");
        try {
        js.executeScript("arguments[0].value=\"" + input + "\"", element);
    } catch (Exception e) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, ConsoleFormatter.setTextColor(ConsoleFormatter.COLOR.RED,"X: ")+"Unable to use JavaScript due to : "+e.getMessage());
    }
    }

    /**
     * Zoom in or out using java script
     */
    public void ZoomInOutJS(int zoom) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Console log Zooming in");
        js.executeScript("document.body.style.zoom='" + zoom + "'");
    }

    /**
     * @return status of the page load like Completed , loading , Interactive or No Status / FAILED TO GET STATUS
     * @author nauman.shahid
     */
    public String getPageLoadStatus() {
        String myPageStatus = "No status";
        try {
            myPageStatus = js.executeScript("return document.readyState").toString();
        } catch (Exception e) {
            myPageStatus = "Failed to get any status";
        }
        return myPageStatus;
    }

    /**
     * @return status of the page load like Completed , loading , Interactive
     */
    public String getJQueryStatus() {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "fetching jquery status");
        String myPageStatus = "Not available";
        try {
            myPageStatus = js.executeScript("return jQuery.active").toString();
        } catch (Exception e) {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Unable to get page status");
        }
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Page load status is " + myPageStatus.toUpperCase());
        return myPageStatus;
    }

    /**
     * move to any specific webelement
     *
     * @param element
     * @author monu.kumar
     */
    public void moveToElement(WebElement element) {
        js.executeScript("if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover')};", element);

    }

    /**
     * Get current double value of current off set or movement of the page from original position
     *
     * @return double y-axis
     * @author nauman.shahid
     */
    public long getPageOffSetYAxis() {
        return (long) js.executeScript("return window.pageYOffset");
    }

    /**
     * execute any javascript command
     *
     * @return
     */
    public JavascriptExecutor executeJSCommand() {
        return js;
    }

}
