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
package com.testingblaze.controller;

import com.testingblaze.devices.AndroidManager;
import com.testingblaze.devices.ChromeManager;
import com.testingblaze.devices.EdgeManager;
import com.testingblaze.devices.FireFoxManager;
import com.testingblaze.devices.IEManager;
import com.testingblaze.devices.IOSManager;
import com.testingblaze.devices.SafariManager;
import com.testingblaze.register.EnvironmentFactory;
import org.openqa.selenium.WebDriver;


/**
 * @author nauman.shahid
 * @category Handles pico container dependency for dependency injection
 */


public final class DeviceBucket implements Device {
    private final Device delegate;

    public DeviceBucket() {
        switch (EnvironmentFactory.getDevice().toLowerCase()) {
            case "firefox":
                delegate = new FireFoxManager();
                break;
            case "chrome":
                delegate = new ChromeManager();
                break;
            case "ie":
                delegate = new IEManager();
                break;
            case "ie-32":
                delegate = new IEManager();
                break;
            case "android":
                delegate = new AndroidManager();
                break;
            case "ios":
                delegate = new IOSManager();
                break;
            case "edge":
                delegate = new EdgeManager();
                break;
            case "edge-32":
                delegate = new EdgeManager();
                break;
            case "safari":
                delegate = new SafariManager();
                break;
            default:
                delegate = new ChromeManager();
        }
    }

    @Override
    public WebDriver getDriver() {
        return delegate.getDriver();
    }

    @Override
    public void setupController() {
        delegate.setupController();
    }

    @Override
    public void stopServiceProvider() {
        delegate.stopServiceProvider();
    }
}
