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
package com.testingblaze.mobile;

import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.exception.TestingBlazeRunTimeException;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.EnvironmentFactory;
import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.WebElement;

/**
 * @author nauman.shahid

 * Handles Android specific special methods and android driver instance
 */
public final class Android {
    private static final String UI_SCROLLABLE_SCROLL_INTO_VIEW = "new UiScrollable(new UiSelector()).scrollIntoView";
    private static final String END_UI_SCROLLABLE = "\"));";

    /**
     * scroll to element using resource id.
     *
     * @param resourceID
     */
    public void toScrollUsingResourceID(String resourceID) {
        driver().findElementByAndroidUIAutomator(
                UI_SCROLLABLE_SCROLL_INTO_VIEW + "(new UiSelector().resourceId(\"" + resourceID + END_UI_SCROLLABLE);
    }

    /**
     * scroll to specific text
     *
     * @param elementText
     */
    public void toScrollUsingText(String elementText) {
        driver().findElementByAndroidUIAutomator(
                UI_SCROLLABLE_SCROLL_INTO_VIEW + "(new UiSelector().text(\"" + elementText + END_UI_SCROLLABLE);
    }

    /**
     * scroll to description containing
     *
     * @param elementContent
     */
    public void toSpecialScrollToContentContains(String elementContent) {
        driver().findElementByAndroidUIAutomator(UI_SCROLLABLE_SCROLL_INTO_VIEW
                + "(new UiSelector().descriptionContains(\"" + elementContent + END_UI_SCROLLABLE);
    }

    /**
     * press a native key to perform any action
     *
     * @param androidKey
     */
    public void toPressNativeKey(AndroidKey androidKey) {
        driver().pressKey(new KeyEvent(androidKey));
        driver().getBatteryInfo();
    }

    /**
     * get battery information
     *
     * @return
     */
    public AndroidBatteryInfo toGetBatteryInfo() {
        return driver().getBatteryInfo();
    }

    public void openNotifications() {
        driver().openNotifications();
    }

    @SuppressWarnings("unchecked") // If statement ensures unchecked cast is safe
    private AndroidDriver<WebElement> driver() {
        if (!"android".equalsIgnoreCase(EnvironmentFactory.getDevice())) {
            throw new TestingBlazeRunTimeException("In order to use 'Android' library, System parameter 'device' must be set to 'android'.\n" +
                    "Parameter 'device' was found to be '" + EnvironmentFactory.getDevice() + "'");
        }
        return (AndroidDriver<WebElement>) InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }
}
