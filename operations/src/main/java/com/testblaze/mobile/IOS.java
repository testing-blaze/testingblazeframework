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
package com.testblaze.mobile;

import com.testblaze.controller.DeviceBucket;
import com.testblaze.exception.TestBlazeRunTimeException;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.register.EnvironmentFetcher;
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
        if (!"ios".equalsIgnoreCase(EnvironmentFetcher.getDevice())) {
            throw new TestBlazeRunTimeException("In order to use 'IOS' library, System parameter 'device' must be set to 'ios'.\n" +
                    "Parameter 'device' was found to be '" + EnvironmentFetcher.getDevice() + "'");
        }
        return (IOSDriver<WebElement>) InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }
}
