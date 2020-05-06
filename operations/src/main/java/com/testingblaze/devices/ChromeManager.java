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
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author nauman.shahid
 * @category Handle browser specific settings and create driver instance
 * @returns chrome webdriver instance
 */

public final class ChromeManager implements Device {
    private WebDriver driver;
    private ChromeDriverService service;
    private static Boolean disableDriverEnforcedDownloadActivity = true;
    private Boolean headlessMode = false;

    @Override
    public void setupController() {
        //System.setProperty("webdriver.chrome.logfile", System.getProperty("user.dir")+ "/target/chromedriver.log");
        //System.setProperty("webdriver.chrome.verboseLogging", "true");

        // Need to make it thread safe somehow
        if (disableDriverEnforcedDownloadActivity) {
            WebDriverManager.chromedriver().clearCache();
            disableDriverEnforcedDownloadActivity = false;
        }

        if (System.getProperty("eDriverVersion") != null && !("default".equals(System.getProperty("eDriverVersion")))) {
            WebDriverManager.chromedriver().version(System.getProperty("eDriverVersion")).forceCache().setup();
        } else {
            WebDriverManager.chromedriver().forceCache().setup();
        }

        //System.setProperty("webdriver.chrome.silentOutput", "true");

        service = new ChromeDriverService.Builder().usingDriverExecutable(new File(WebDriverManager.chromedriver().getBinaryPath())).usingAnyFreePort().build();
        try {
            service.start();
        } catch (IOException e) {

        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars"); // disabling infobars
        chromeOptions.addArguments("--disable-extensions"); // disabling extensions
        chromeOptions.addArguments("--disable-gpu"); // applicable to windows os only
        chromeOptions.addArguments("--no-sandbox");
        HashMap<String, Object> chromePrefs = new HashMap<>();

        if (null != System.getProperty("evaluatePerformance") && "true".equalsIgnoreCase(System.getProperty("evaluatePerformance"))) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
            chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        }

        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", "false");
        chromePrefs.put("download.directory_upgrade", "true");
        chromePrefs.put("download.default_directory", System.getProperty("user.dir") + "\\target");
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        if ("Headless".equalsIgnoreCase(EnvironmentFactory.getExecutionMode())) {
            chromeOptions.addArguments("--window-size=2560,1440");
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--mute-audio");
            headlessMode = true;
        }

        if ("local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            this.driver = new RemoteWebDriver(service.getUrl(), chromeOptions);
            if(!headlessMode) driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.SECONDS);
        } else {
            DesiredCapabilities chromeCapabilities = new DesiredCapabilities();
            chromeCapabilities.setBrowserName("chrome");
            chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            try {
                this.driver = new RemoteWebDriver(new URL(EnvironmentFactory.getHub() + "/wd/hub"),
                        new DesiredCapabilitiesManagement().getBrowserCapabilities(chromeCapabilities));
                ((RemoteWebDriver) this.driver).setFileDetector(new LocalFileDetector());
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
