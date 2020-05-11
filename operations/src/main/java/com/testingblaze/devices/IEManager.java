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

import com.testingblaze.controller.DesiredCapabilitiesManagement;
import com.testingblaze.controller.Device;
import com.testingblaze.register.EnvironmentFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author nauman.shahid
 * @category Handle browser specific settings and create driver instance
 * @returns IE webdriver instance
 */

public final class IEManager implements Device {
    WebDriver driver;

    @Override
    public void setupController() {
        if ("ie-32".equalsIgnoreCase(EnvironmentFactory.getDevice())) {
            WebDriverManager.iedriver().arch32().forceCache().setup();
        } else {
            WebDriverManager.iedriver().arch64().forceCache().setup();
        }

        DesiredCapabilities ieCapabilities = new DesiredCapabilities();
        ieCapabilities.setBrowserName("internet explorer");
        ieCapabilities.acceptInsecureCerts();
        ieCapabilities.setJavascriptEnabled(true);
        ieCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
        ieCapabilities.setCapability(InternetExplorerDriver.
                INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        ieCapabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        ieCapabilities.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, true);
        ieCapabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions().merge(ieCapabilities);

        if ("local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            this.driver = new InternetExplorerDriver(internetExplorerOptions);
            this.driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(500, TimeUnit.SECONDS);
        } else {
            try {
                this.driver = new RemoteWebDriver(new URL(EnvironmentFactory.getHub() + "/wd/hub"),
                        new DesiredCapabilitiesManagement().getBrowserCapabilities(ieCapabilities));
                ((RemoteWebDriver) this.driver).setFileDetector(new LocalFileDetector());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }

    @Override
    public void stopServiceProvider() {
        // To be implemented
    }
}
