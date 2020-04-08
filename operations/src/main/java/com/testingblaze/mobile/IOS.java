/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Solution.
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
import com.testingblaze.exception.TestBlazeRunTimeException;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.EnvironmentFactory;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Handles IOS specific special methods and IOS driver instance
 */
public final class IOS {

    /**
     * returns IOS specific driver instance
     *
     * @return
     */
    @SuppressWarnings("unchecked") // If statement ensures unchecked cast is safe
    public IOSDriver<WebElement> getIOSMobileDriver() {
        if (!"ios".equalsIgnoreCase(EnvironmentFactory.getDevice())) {
            throw new TestBlazeRunTimeException("In order to use 'IOS' library, System parameter 'device' must be set to 'ios'.\n" +
                    "Parameter 'device' was found to be '" + EnvironmentFactory.getDevice() + "'");
        }
        return (IOSDriver<WebElement>) InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }
}
