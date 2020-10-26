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
package com.testingblaze.actionsfactory.api;

import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HGJGcYGHQk {
    private final By IFRAME = By.xpath("//iframe[contains(@ng-sc,'/') or contains(@src,'/') or @src='']");
    private By locator;
    private final DeviceBucket device;
    //public static boolean setFlagForFrameSwitch = false;
    private String switchedFrameInfo = "No information available";
    public static String lastSuccessInfo = "Default Content";
    public static Boolean isFrameSwitchStatusSuccess = false;
    private static int frameSwitchCount = 0;


    public HGJGcYGHQk() {
        this.device = InstanceRecording.getInstance(DeviceBucket.class);
    }

    public void setUpLocator(By locator) {
        this.locator = locator;
    }

    public void evaluatePossibleIFrameToSwitch() {
        I.amPerforming().waitFor().makeThreadSleep(1000);
        if (device.getDriver().findElements(IFRAME).size() > 0) {
            manageSwitching();
        }
        if (!isFrameSwitchStatusSuccess && (frameSwitchCount > 0)) {
            reverseFrameSwitching();
        }
    }

    private void manageSwitching() {
        for (WebElement iframes : device.getDriver().findElements(IFRAME)) {
            switchedFrameInfo = getFrameId(iframes, "id");
            switchToFrame(iframes);
            frameSwitchCount = frameSwitchCount + 1;
            if (device.getDriver().findElements(locator).size() > 0) {
                lastSuccessInfo = switchedFrameInfo;
                isFrameSwitchStatusSuccess = true;
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("Successfully Switched to iframe with id '%s'", switchedFrameInfo));
                break;
            } else if (device.getDriver().findElements(IFRAME).size() > 0) {
                manageInternalSwitching(switchedFrameInfo);
            } else {
                switchToParentFrame();
            }
        }
    }

    private void reverseFrameSwitching() {
        var localFrameSwitchCount = frameSwitchCount;
        for (int i = 0; i < frameSwitchCount; i++) {
            switchToParentFrame();
            localFrameSwitchCount = localFrameSwitchCount - 1;
            if (device.getDriver().findElements(locator).size() > 0) {
                break;
            }
        }
        frameSwitchCount = localFrameSwitchCount;
    }

    private void manageInternalSwitching(String mainIframe) {
        for (WebElement iframes : device.getDriver().findElements(IFRAME)) {
            switchedFrameInfo = getFrameId(iframes, "id");
            switchToFrame(iframes);
            frameSwitchCount = frameSwitchCount + 1;
            if (device.getDriver().findElements(locator).size() > 0) {
                lastSuccessInfo = switchedFrameInfo;
                isFrameSwitchStatusSuccess = true;
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("Successfully Switched to nested iframe with id '%s'", switchedFrameInfo));
                break;
            } else if (device.getDriver().findElements(IFRAME).size() > 0) {
                manageInternalSwitching(mainIframe);
            } else {
                switchToFrameId(mainIframe);
                if (device.getDriver().findElements(locator).size() > 0) {
                    lastSuccessInfo = mainIframe;
                    isFrameSwitchStatusSuccess = true;
                    break;
                }
            }
        }
    }

    /**
     * switch between different frame
     */
    private void switchToFrame(WebElement element) {
        try {
            device.getDriver().switchTo().frame(element);
        } catch (Exception e) {

        }
    }

    /**
     * switch between different frame
     */
    private void switchToFrameId(String id) {
        try {
            device.getDriver().switchTo().frame(id);
        } catch (Exception e) {

        }
    }

    /**
     * switch back default content
     */
    private void switchToParentFrame() {
        try {
            device.getDriver().switchTo().parentFrame();
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Parent Context Enabled");
        } catch (Exception e) {
        }
    }

    private String getFrameId(WebElement element, String attribute) {
        String getAttribute = "";
        try {
            getAttribute = element.getAttribute(attribute);
        } catch (Exception e) {

        }
        return getAttribute;
    }
}
