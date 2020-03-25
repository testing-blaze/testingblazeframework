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
package com.testblaze.controller;

import com.testblaze.devices.AndroidManager;
import com.testblaze.devices.ChromeManager;
import com.testblaze.devices.EdgeManager;
import com.testblaze.devices.FireFoxManager;
import com.testblaze.devices.IEManager;
import com.testblaze.devices.IOSManager;
import com.testblaze.devices.SafariManager;
import com.testblaze.register.EnvironmentFetcher;
import org.openqa.selenium.WebDriver;


/**
 * @author nauman.shahid
 * @category Handles pico container dependency for dependency injection
 */


public final class DeviceBucket implements Device {
    private final Device delegate;

    public DeviceBucket() {
        switch (EnvironmentFetcher.getDevice().toLowerCase()) {
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
