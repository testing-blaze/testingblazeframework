/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Framework [BDD] .
 *
 * Testing Blaze Automation Framework is licensed under the Apache License, Version
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

import com.testingblaze.controller.DesiredCapabilitiesManagement;
import com.testingblaze.controller.Device;
import com.testingblaze.register.EnvironmentFactory;
import com.testingblaze.register.I;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author nauman.shahid
 * @category Initialize Android
 */

public final class AndroidManager implements Device {
    private WebDriver driver;

    @Override
    public void setupController() {
        DesiredCapabilities androidCapabilities = new DesiredCapabilities();
        if (EnvironmentFactory.getAppName() != null) {
            androidCapabilities.setCapability(MobileCapabilityType.APP,
                    System.getProperty("user.dir") + "\\mobileapp" + EnvironmentFactory.getAppName());

            try {
                androidCapabilities.setCapability("appPackage", I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile("appConfig", "appPackage"));
                androidCapabilities.setCapability("appActivity", I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile("appConfig", "appActivity"));
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }

        } else {
            androidCapabilities.setCapability("chromedriverExecutable", WebDriverManager.chromedriver().getBinaryPath());
            androidCapabilities.setCapability("w3c", false);
            androidCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        }
        try {
            this.driver = new AndroidDriver<>(new URL(EnvironmentFactory.getHub() + "/wd/hub"),
                    new DesiredCapabilitiesManagement().getAndroidCapabilities(androidCapabilities));
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
