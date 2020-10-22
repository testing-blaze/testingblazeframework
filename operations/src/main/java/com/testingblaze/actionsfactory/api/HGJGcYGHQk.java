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
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HGJGcYGHQk {
    private final By IFRAME = By.xpath("//iframe[contains(@ng-sc,'/') or contains(@src,'/') or @src='']");
    private By locator;
    private final DeviceBucket device;
    public static boolean setFlagForFrameSwitch = false;
    public String switchedFrameInfo = "No information available";
    private static String lastSuccessInfo = "Default Content";
    private static Boolean changeInLastSuccessInfo = false;


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
        } else {
            switchToDefaultContent();
            lastSuccessInfo = "Default Content";
            changeInLastSuccessInfo = true;
        }
        if (!changeInLastSuccessInfo) {
            if (StringUtils.containsIgnoreCase(lastSuccessInfo, "Default Content")) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Switching to the last alive Active Content");
                switchToDefaultContent();
            } else {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Switching to the last alive Frame");
                switchToFrameId(lastSuccessInfo);
            }
        }
        changeInLastSuccessInfo = false;
    }

    private void manageSwitching() {
        for (WebElement iframes : device.getDriver().findElements(IFRAME)) {
            switchedFrameInfo = getFrameId(iframes, "id");
            switchToFrame(iframes);
            if (device.getDriver().findElements(locator).size() > 0) {
                lastSuccessInfo = switchedFrameInfo;
                changeInLastSuccessInfo = true;
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("Successfully Switched to iframe with id '%s'", switchedFrameInfo));
                setFlagForFrameSwitch = true;
                break;
            } else if (device.getDriver().findElements(IFRAME).size() > 0) {
                manageInternalSwitching(switchedFrameInfo);
            } else {
                switchToDefaultContent();
                if (device.getDriver().findElements(locator).size() > 0) {
                    lastSuccessInfo = "Default Content";
                    changeInLastSuccessInfo = true;
                    break;
                }
            }
        }
    }

    private void manageInternalSwitching(String mainIframe) {
        for (WebElement iframes : device.getDriver().findElements(IFRAME)) {
            switchedFrameInfo = getFrameId(iframes, "id");
            switchToFrame(iframes);
            if (device.getDriver().findElements(locator).size() > 0) {
                lastSuccessInfo = switchedFrameInfo;
                changeInLastSuccessInfo = true;
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("Successfully Switched to nested iframe with id '%s'", switchedFrameInfo));
                setFlagForFrameSwitch = true;
                break;
            } else if (device.getDriver().findElements(IFRAME).size() > 0) {
                manageInternalSwitching(mainIframe);
            } else {
                switchToFrameId(mainIframe);
                if (device.getDriver().findElements(locator).size() > 0) {
                    lastSuccessInfo = mainIframe;
                    changeInLastSuccessInfo = true;
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
    private void switchToDefaultContent() {
        try {
            device.getDriver().switchTo().defaultContent();
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Default Context Enabled");
        } catch (Exception e) {
        }

    }

    private String getFrameId(WebElement element, String attribute) {
        String getAttribute = "Information not available";
        try {
            getAttribute = element.getAttribute(attribute);
        } catch (Exception e) {

        }
        return getAttribute;
    }
}
