/*
 * Copyright 2020
 *
 * This file is part of Fregata Automated Testing Solution [FAST].
 *
 * FAST is licensed under the Apache License, Version
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
package com.fast.devices;

import com.fast.controller.DesiredCapabilitiesManagement;
import com.fast.controller.Device;
import com.fast.register.EnvironmentFetcher;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Handles launching IOS Apps
 *
 * @author nauman.shahid
 * @category Implementation yet to be tested
 */

public final class IOSManager implements Device {
    private WebDriver driver;

    @Override
    public void setupController() {
        DesiredCapabilities iosCapabilities = new DesiredCapabilities();
        if (EnvironmentFetcher.getAppName() != null) {
            iosCapabilities.setCapability(MobileCapabilityType.APP,
                    System.getProperty("user.dir") + "\\mobileapp" + EnvironmentFetcher.getAppName());
        } else {
            iosCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "safari");
        }
        try {
            this.driver = new IOSDriver<>(new URL(EnvironmentFetcher.getHub() + "/wd/hub"),
                    new DesiredCapabilitiesManagement().getIOSCapabilities(iosCapabilities));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public void stopServiceProvider() {
        // To be implemented
    }

}
