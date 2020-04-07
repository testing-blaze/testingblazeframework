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
package com.testingblaze.actionsfactory.elementfunctions;

import com.testingblaze.actionsfactory.abstracts.ElementProcessing;
import com.testingblaze.actionsfactory.abstracts.LocatorProcessing;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static com.testingblaze.misclib.ConsoleFormatter.COLOR.RED;
import static com.testingblaze.misclib.ConsoleFormatter.ICON.X_MARK;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Handles finding elements and returning
 */

public final class FindMyElements {
    private LocatorProcessing getLocator;
    private ElementProcessing elementProcessing;
    private WebDriver driver;

    public FindMyElements() {
        this.getLocator = InstanceRecording.getInstance(LocatorProcessing.class);
        this.elementProcessing = InstanceRecording.getInstance(ElementProcessing.class);
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

    /**
     * A smart getelement method which evaluated the provided locator to add up any value from properties file in the locator if the parameter is passed in locator
     * pass any parameter to be read from property file as "---filename.properties::NameOfParameter---"
     *
     * @param locator
     * @param enablePreProcessing :true if preprocessing is required else false
     * @return webElement
     */
    public WebElement getElement(By locator, Boolean enablePreProcessing) {
        By refinedFinalLocator = getLocator.get(locator);
        if (enablePreProcessing) {
            try {
                return elementProcessing.forSingleElement(refinedFinalLocator);
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, RED, X_MARK, "Element is not present or locator is not correct | " + refinedFinalLocator);
                throw e;
            }
        } else {
            try {
                return driver.findElement(refinedFinalLocator);
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, RED, X_MARK, "Element is not present or locator is not correct | " + refinedFinalLocator);
                throw e;
            }
        }
    }

    /**
     * A smart getelement method which evaluated the provided locator to add up any value from properties file in the locator if the parameter is passed in locator
     * pass any parameter to be read from property file as "---filename.properties::NameOfParameter---"
     *
     * @param locator
     * @return webElement
     */
    public WebElement getNestedElement(WebElement element, By locator, Boolean enablePreProcessing) {
        By refinedFinalLocator = getLocator.get(locator);
        if (enablePreProcessing) {
            try {
                return elementProcessing.forNestedElement(element, refinedFinalLocator);
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Nested Element is not present or locator is not correct | " + refinedFinalLocator);
                throw e;
            }
        } else {
            try {
                return element.findElement(refinedFinalLocator);
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Nested Element is not present or locator is not correct | " + refinedFinalLocator);
                throw e;
            }
        }
    }

    /**
     * A smart getelements List method which evaluated the provided locator to add up any value from properties file in the locator if the parameter is passed in locator
     * pass any parameter to be read from property file as "---filename.properties::NameOfParameter---"
     *
     * @param locator
     * @param enablePreProcessing :true if preprocessing is required else false
     * @return list of webelements
     * @author nauman.shahid
     */
    public List<WebElement> getElements(By locator, Boolean enablePreProcessing) {
        By refinedFinalLocator = getLocator.get(locator);
        if (enablePreProcessing) {
            try {
                return elementProcessing.forListOfElements(refinedFinalLocator);
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "List of Elements is not present or locator is not correct | " + refinedFinalLocator);
                throw e;
            }
        } else {
            try {
                return driver.findElements(refinedFinalLocator);
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "List of Elementss is not present or locator is not correct | " + refinedFinalLocator);
                throw e;
            }
        }
    }

    /**
     * get a nested element
     *
     * @param element
     * @param locator
     * @return
     * @warning No Pre-Processing
     */
    public List<WebElement> getNestedElementList(WebElement element, By locator) {
        By refinedFinalLocator = getLocator.get(locator);
        try {
            return element.findElements(locator);
        } catch (Exception e) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Element is not present or locator is not correct | " + refinedFinalLocator);
            throw e;
        }
    }

    /**
     * A smart getDropDown method which evaluated the provided locator to add up any value from properties file in the locator if the parameter is passed in locator
     * pass any parameter to be read from property file as "---filename.properties::NameOfParameter---"
     *
     * @param locator
     * @param enablePreProcessing:true if preprocessing is required else false
     * @return dropdown
     * @author nauman.shahid
     */
    public Select getDropDown(By locator, Boolean enablePreProcessing) {
        By refinedFinalLocator = getLocator.get(locator);
        if (enablePreProcessing) {
            try {
                return new Select(elementProcessing.forSingleElement(refinedFinalLocator));
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Element is not present or locator is not correct | " + locator);
                throw e;
            }
        } else {
            try {
                return new Select(driver.findElement(refinedFinalLocator));
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Element is not present or locator is not correct | " + locator);
                throw e;
            }
        }
    }

    /**
     * @param element
     * @return dropdown
     * @author nauman.shahid
     */
    public Select getDropDown(WebElement element) {
        try {
            return new Select(element);
        } catch (Exception e) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Element is not present or locator is not correct | " + element);
            throw e;
        }
    }
}
