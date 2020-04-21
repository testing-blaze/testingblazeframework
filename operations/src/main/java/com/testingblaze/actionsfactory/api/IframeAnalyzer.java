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

public class IframeAnalyzer {
    private final By IFRAME = By.xpath("//iframe[contains(@ng-sc,'/') or contains(@src,'/') or @src='']");
    private By locator;
    private DeviceBucket device;
    public static boolean setFlagForFrameSwitch = false;
    public String switchedFrameInfo = "No information available";


    public IframeAnalyzer() {
        this.device = InstanceRecording.getInstance(DeviceBucket.class);
    }

    public void setUpLocator(By locator) {
        this.locator = locator;
    }

    public void evaluatePossibleIFrameToSwitch() {
        if (device.getDriver().findElements(IFRAME).size() > 0) {
            manageSwitching();
        } else {
            switchToDefaultContent();
        }
    }

    private void manageSwitching() {
        for (WebElement iframes : device.getDriver().findElements(IFRAME)) {
            switchedFrameInfo = getFrameId(iframes, "id");
            switchToFrame(iframes);
            if (device.getDriver().findElements(locator).size() > 0) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO,  String.format("Successfully Switched to iframe with id '%s'", switchedFrameInfo));
                setFlagForFrameSwitch = true;
                break;
            } else if (device.getDriver().findElements(IFRAME).size() > 0) {
                manageSwitching();
                break;
            } else {
                switchToDefaultContent();
                if (device.getDriver().findElements(locator).size() > 0) {
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
     * switch back default content
     */
    private void switchToDefaultContent() {
        try {
            device.getDriver().switchTo().defaultContent();
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO,  "Default Context Enabled");
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
