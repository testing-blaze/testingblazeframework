/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Solution.
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
package com.testingblaze.controller;

import com.testingblaze.register.EnvironmentFactory;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author nauman.shahid
 * @category Desired Capabilities for different browsers
 * @function platforms , JS , browsers , proxy
 */

public final class DesiredCapabilitiesManagement {
    DesiredCapabilities desiredCapabilities;

    public DesiredCapabilities getBrowserCapabilities(DesiredCapabilities desiredCapabilities) {
        browserBaseCapabilities(desiredCapabilities);
        return desiredCapabilities;
    }

    public DesiredCapabilities getAndroidCapabilities(DesiredCapabilities desiredCapabilities) {
        androidCapabilities(desiredCapabilities);
        return desiredCapabilities;
    }

    public DesiredCapabilities getIOSCapabilities(DesiredCapabilities desiredCapabilities) {
        iosCapabilities(desiredCapabilities);
        return desiredCapabilities;
    }

    private void browserBaseCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
        this.desiredCapabilities.acceptInsecureCerts();
        this.desiredCapabilities.setJavascriptEnabled(true);
        handleBrowser();
        handlePlatform();
        setProxy();
    }

    private void androidCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;

        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
        ;
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "8.0");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Samsung");
        desiredCapabilities.setCapability("locationServicesAuthorized", true);
        desiredCapabilities.setCapability("autoAcceptAlerts", true);
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.ACCEPT_INSECURE_CERTS, true);
        desiredCapabilities.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS, true);
        desiredCapabilities.setCapability(MobileCapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
    }

    private void iosCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "IOS");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 8 Plus");
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.4");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");
        desiredCapabilities.setCapability("locationServicesAuthorized", true);
        desiredCapabilities.setCapability("autoAcceptAlerts", true);
        desiredCapabilities.setCapability(MobileCapabilityType.ACCEPT_INSECURE_CERTS, true);
        desiredCapabilities.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS, true);
        desiredCapabilities.setCapability(MobileCapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
    }

    private void handlePlatform() {
        if (System.getProperty("platform") != null) {
            if (System.getProperty("platform").toUpperCase().contains("WINDOWS")) {
                switch (System.getProperty("version")) {
                    case "8":
                        this.desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, "win8");
                        break;
                    case "10":
                        this.desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, "win10");
                        break;
                    case "7":
                        this.desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, "vista");
                        break;
                    default:
                        this.desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, "win10");
                        break;
                }
            } else if (System.getProperty("platform").toUpperCase().contains("MAC")) {
                this.desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, "mac");
            }
        }
    }

    private void handleBrowser() {
        switch (EnvironmentFactory.getDevice().toLowerCase()) {
            case "chrome":
                desiredCapabilities.setBrowserName("Chrome");
                break;
            case "firefox":
                desiredCapabilities.setBrowserName("firefox");
                break;
            case "ie":
                desiredCapabilities.setBrowserName("internetExplorer");
                break;
            default:
                desiredCapabilities.setBrowserName("chrome");
                break;
        }
    }

    private void setProxy() {
        if (System.getProperty("proxy") != null) {
            this.desiredCapabilities.setCapability(CapabilityType.PROXY, System.getProperty("proxy"));
        }
    }

}
