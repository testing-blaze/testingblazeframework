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
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author nauman.shahid
 * @category Handle browser specific settings and create driver instance
 * @returns chrome webdriver instance
 */

public final class ChromeManager implements Device {
    private RemoteWebDriver driver;
    private ChromeDriverService service;
    private static Boolean disableDriverEnforcedDownloadActivity = true;

    @Override
    public void setupController() {
        System.setProperty("webdriver.chrome.logfile", System.getProperty("user.dir")+ "/target/chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        //System.setProperty("webdriver.chrome.silentOutput", "true");

        // Need to make it thread safe somehow
        if (disableDriverEnforcedDownloadActivity) {
            WebDriverManager.chromedriver().clearCache();
            disableDriverEnforcedDownloadActivity = false;
        }

        if (!"default".equals(System.getProperty("eDriverVersion"))) {
            WebDriverManager.chromedriver().version(System.getProperty("eDriverVersion")).forceCache().setup();
        } else {
            WebDriverManager.chromedriver().forceCache().setup();
        }

        service = new ChromeDriverService.Builder().usingDriverExecutable(new File(WebDriverManager.chromedriver().getBinaryPath())).usingAnyFreePort().build();
        try {
            service.start();
        } catch (IOException e) {

        }

        if ("local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            driver = new RemoteWebDriver(service.getUrl(), CapabilitiesManager.getChromeCapabilities());
            if(!EnvironmentFactory.isHeadless()) {
                driver.manage().window().maximize();
            }
            driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.SECONDS);
        } else {
            try {
                driver = new RemoteWebDriver(new URL(EnvironmentFactory.getHub() + "/wd/hub"),
                        CapabilitiesManager.getChromeCapabilities());
                driver.setFileDetector(new LocalFileDetector());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public void stopServiceProvider() {
        service.stop();
    }
}
