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
package com.testingblaze.actionsfactory.elementfunctions;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.ByAngular.Factory;
import com.paulhammant.ngwebdriver.NgWebDriver;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.exception.TestingBlazeRunTimeException;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.EnvironmentFactory;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Handles angular specific locators
 *
 * @author nauman.shahid
 */
public final class Ng {
    private NgWebDriver ngDriver;
    private WebDriver driver;

    public Ng() {
        if (!(EnvironmentFactory.getDevice().equalsIgnoreCase("android") || EnvironmentFactory.getDevice().equalsIgnoreCase("ios"))) {
            this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
            this.ngDriver = new NgWebDriver((JavascriptExecutor) this.driver);
            ngDriver.waitForAngularRequestsToFinish();
        }
    }

    /**
     * @param rootSelector
     * @return Angular control to receive a locator
     */
    public Factory rootSelector(String rootSelector) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + rootSelector);
        return ByAngular.withRootSelector(rootSelector);
    }

    /**
     * Access angular based attributes and element
     *
     * @param locator MODEL,BINDING,BUTTON TEXT,
     * @return
     */
    public WebElement getNgElement(ByAngular.BaseBy locator,Boolean processing) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + locator);
        return driver.findElement(locator);
    }

    /**
     * Access angular based attributes and element
     *
     * @param locator MODEL,BINDING,BUTTON TEXT,
     * @return
     */
    public List<WebElement> getNgElements(ByAngular.BaseBy locator,Boolean processing) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + locator);
        List<WebElement> fetchElement = driver.findElements(locator);
        if(fetchElement == null) {
            throw new TestingBlazeRunTimeException("Angular element is not present or incorrect: " + locator);
        }
        return fetchElement;
    }

}
