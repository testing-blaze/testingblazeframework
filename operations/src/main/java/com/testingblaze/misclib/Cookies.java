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
package com.testingblaze.misclib;

import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public final class Cookies {
    WebDriver driver;

    public Cookies() {
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    public void addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
    }

    public void getCookie(String name) {
        driver.manage().getCookieNamed(name);
    }

    public Set<Cookie> getAllCookies() {
        return driver.manage().getCookies();
    }
}
