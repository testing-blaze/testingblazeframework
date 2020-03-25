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
package com.testblaze.devices;

import com.testblaze.controller.DesiredCapabilitiesManagement;
import com.testblaze.controller.Device;
import com.testblaze.register.EnvironmentFetcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public final class SafariManager implements Device {
    WebDriver driver;

    @Override
    public void setupController() {
        SafariDriverService safariDriverService = new SafariDriverService.Builder().usingAnyFreePort().build();

        DesiredCapabilities safariCapabilities = new DesiredCapabilities();
        safariCapabilities.setBrowserName("safari");
        safariCapabilities.acceptInsecureCerts();
        safariCapabilities.setJavascriptEnabled(true);
        safariCapabilities.setCapability("ignoreProtectedModeSettings", true);

        SafariOptions safariOptions = new SafariOptions().merge(safariCapabilities);

        if ("local".equalsIgnoreCase(EnvironmentFetcher.getHub())) {
            this.driver = new SafariDriver(safariDriverService, safariOptions);
            this.driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(500, TimeUnit.SECONDS);
        } else {
            try {
                this.driver = new RemoteWebDriver(new URL(EnvironmentFetcher.getHub() + "/wd/hub"),
                        new DesiredCapabilitiesManagement().getBrowserCapabilities(safariCapabilities));
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
