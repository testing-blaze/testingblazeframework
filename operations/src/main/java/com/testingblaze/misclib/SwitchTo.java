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
package com.testingblaze.misclib;

import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.controller.TestingBlazeGlobal;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;


public final class SwitchTo {
    WebDriver driver;

    public SwitchTo() {
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

    /**
     * switch between different frame
     */
    public WebDriver frame(By locator) {
        WebElement frameElement = driver.findElement(locator);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "  Switched to new frame");
        return driver.switchTo().frame(frameElement);
    }

    /**
     * switch between different frame
     */
    public WebDriver frame(WebElement element) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, " Switched to new frame");
        return driver.switchTo().frame(element);
    }

    /**
     * switch back to parent frame
     */
    public WebDriver parentFrame() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Switched to parent frame");
        return driver.switchTo().parentFrame();
    }

    /**
     * switch back default content
     */
    public WebDriver defaultContent() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Switched to default content");
        return driver.switchTo().defaultContent();
    }

    /**
     * Switching and accepting alert
     */
    public void acceptAlert() {
        try {
            if (I.amPerforming().checkToSee().popupPresent()) {
                if(TestingBlazeGlobal.getVariable("acceptAlert") != null && ((String) TestingBlazeGlobal.getVariable("acceptAlert")).equalsIgnoreCase("off")) return;
                else {
                    driver.switchTo().alert().accept();
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Alert accepted");
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Switching and rejecting alert
     */
    public void rejectAlert() {
        try {
            driver.switchTo().alert().dismiss();
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Alert accepted");

        } catch (Exception e) {

        }
    }

    /**
     * Windows handlers
     */
    public ArrayList<String> getWindowsHandlers() {
        return new ArrayList<String>(driver.getWindowHandles());
    }

    /**
     * switch to window handler
     *
     * @param handlerNumber : Usually child number is "1" and parent number is "0". However,
     *                      method is dyanmic to handle any child number
     */
    public void windowHandler(int handlerNumber) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Ready to Switch to window " + handlerNumber);
        driver.switchTo().window(getWindowsHandlers().get(handlerNumber));
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Switch to new tab ");
    }

    /**
     * get text of the alert
     *
     * @return
     */
    public String getAlertText() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Fectching text from alert ");
        return driver.switchTo().alert().getText();
    }

    /**
     * switch to new context or active frame
     *
     * @return
     */
    public WebElement activeContext() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Switching to current active element context ");
        return driver.switchTo().activeElement();
    }
}
