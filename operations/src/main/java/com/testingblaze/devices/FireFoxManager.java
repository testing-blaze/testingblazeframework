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
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author nauman.shahid
 * @category Handle browser specific settings and create driver instance
 * @returns firfox webdriver instance
 */

public final class FireFoxManager implements Device {
    WebDriver driver;

    @Override
    public void setupController() {

        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"null");

        if(System.getProperty("eDriverVersion") != null && !("default".equals(System.getProperty("eDriverVersion"))))
            WebDriverManager.firefoxdriver().version(System.getProperty("eDriverVersion")).forceCache().setup();
        else
            WebDriverManager.firefoxdriver().forceCache().setup();

        FirefoxProfile profile = new FirefoxProfile();
        profile.setAssumeUntrustedCertificateIssuer(true);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", System.getProperty("user.dir") + File.separator + "target");
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/plain,application/octet-stream,application/pdf,application/x-pdf,application/vnd.pdf,text/csv,application/java-archive,application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,application/msword,application/xml,application/vnd.microsoft.portable-executable");
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.helperApps.neverAsk.openFile", "");
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.closeWhenDone", true);
        profile.setPreference("pdfjs.disabled", true);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setProfile(profile);
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        DesiredCapabilities firefoxCapabilities= new DesiredCapabilities();
        firefoxCapabilities.setBrowserName("firefox");
        firefoxCapabilities.setCapability(FirefoxDriver.PROFILE, profile);

        if ("Headless".equalsIgnoreCase(EnvironmentFactory.getExecutionMode())) {
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            firefoxOptions.setBinary(firefoxBinary);
            firefoxCapabilities.setCapability(FirefoxDriver.BINARY, firefoxBinary);
        }
        if ("local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            this.driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(500, TimeUnit.SECONDS);
        } else {
            try {
                this.driver = new RemoteWebDriver(new URL(EnvironmentFactory.getHub() + "/wd/hub"),
                        new DesiredCapabilitiesManagement().getBrowserCapabilities(firefoxCapabilities));
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
