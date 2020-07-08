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

import com.testingblaze.controller.qrYoTsOWwA;
import com.testingblaze.register.EnvironmentFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
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

public final class EdgeManager implements qrYoTsOWwA {
    RemoteWebDriver driver;

    @Override
    public void setupController() {
        if ("edge-32".equalsIgnoreCase(EnvironmentFactory.getDevice())) {
            WebDriverManager.edgedriver().useBetaVersions().arch32().setup();
        } else {
            WebDriverManager.edgedriver().useBetaVersions().arch64().setup();
        }

        System.setProperty("webdriver.chrome.silentOutput", "true");

        if ("local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            driver = new EdgeDriver(CapabilitiesManager.getEdgeCapabilities());
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(500, TimeUnit.SECONDS);
        } else {
            try {
                driver = new RemoteWebDriver(new URL(EnvironmentFactory.getHub() + "/wd/hub"),
                        CapabilitiesManager.getEdgeCapabilities());
                driver.setFileDetector(new LocalFileDetector());
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
