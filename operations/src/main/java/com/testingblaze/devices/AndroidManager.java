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
package com.testingblaze.devices;

import com.testingblaze.controller.Device;
import com.testingblaze.register.EnvironmentFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author nauman.shahid
 * @category Initialize Android
 */

public final class AndroidManager implements Device {
    private RemoteWebDriver driver;

    @Override
    public void setupController() {
        try {
            driver = new AndroidDriver<>(new URL(EnvironmentFactory.getHub() + "/wd/hub"),
                    CapabilitiesManager.getAndroidCapabilities());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void stopServiceProvider() {
        // To be implemented
    }


    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
