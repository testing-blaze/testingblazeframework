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
package com.testingblaze.actionsfactory.type;

import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.Elements;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author nauman.shahid

 * checks for presence of certain elements like popup and text
 */

public final class Is {
    private Element elementApi;

    public Is() {
        elementApi = InstanceRecording.getInstance(Element.class);
    }

    /**
     * checks whether alert is present or not
     *
     * @author nauman.shahid
     */
    public boolean popupPresent() {
        boolean isPresent = false;
        try {
            Alert alert1 = InstanceRecording.getInstance(DeviceBucket.class).getDriver().switchTo().alert();
            if (alert1 != null) {
                isPresent = true;
            }
        } catch (Exception e) {

        }
        return isPresent;
    }

    /**
     * checks if specific text is present in complete body of html document
     *
     * @author nauman.shahid
     */
    public boolean textPresent(String expectedText) {
        try {
            return elementApi.locator(By.tagName("body"), true).getText().contains(expectedText);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * return true if specific element is displayed on current page
     *
     * @author nauman.shahid
     */
    public <T> boolean isDisplayed(T locator) {
        return isDisplayed(locator, true);
    }

    /**
     * return true if specific element is displayed on current page
     *
     * @author nauman.shahid
     */
    public <T> boolean isDisplayed(T locator, Boolean processing) {
        return elementApi.locator(locator, processing).isDisplayed();
    }

    /**
     * return true if specific element is displayed on current page
     *
     * @author nauman.shahid
     */
    public <T> boolean isElementDisplayed(T locator) {
        return isElementDisplayed(locator, true);
    }

    /**
     * return true if specific element is displayed on current page
     *
     * @author nauman.shahid
     */
    public <T> boolean isElementDisplayed(T locator, Boolean processing) {
        return elementApi.locator(locator, processing).getRect().getWidth() != 0 && elementApi.locator(locator, processing).getRect().getHeight() != 0;
    }

    /**
     * @param locator
     * @return
     * @author nauman.shahid
     */
    public <T> boolean isEnabled(T locator) {
        return isEnabled(locator, true);
    }

    /**
     * @param locator
     * @return
     * @author nauman.shahid
     */
    public <T> boolean isEnabled(T locator, Boolean processing) {
        return elementApi.locator(locator, processing).isEnabled();
    }

    /**
     * @param locator
     * @return
     * @author nauman.shahid
     */
    public <T> boolean isSelected(T locator) {
        return isSelected(locator, true);
    }

    /**
     * @param locator
     * @return
     * @author nauman.shahid
     */
    public <T> boolean isSelected(T locator, Boolean processing) {
        return elementApi.locator(locator, processing).isSelected();
    }


    /**
     * checks for element presence dynamically
     *
     * @author nauman.shahid
     */
    public <T> boolean IsElementPresentQuick(T locator) {
        try {
            elementApi.locator(locator, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param element
     * @return
     * @author john.phillips
     */
    public boolean isStale(WebElement element) {
        try {
            element.isDisplayed();
            return false;
        } catch (StaleElementReferenceException e) {
            return true;
        }
    }

    /**
     * Check if element is not stale but can possibly be interacted with or not
     *
     * @param element
     * @return
     */
    public boolean isInteractable(WebElement element) {
        try {
            element.click();
            return true;
        } catch (ElementNotInteractableException interactableException) {
            return false;
        }
    }

    /**
     * Provide any List of WebElements to validate sorting order
     *
     * @param rows_table
     * @param sorting    Ascending:Descending
     * @return true
     * @author Monu.Kumar
     */
    public Boolean isTableSorted(List<Elements> rows_table, String sorting) {
        return I.amPerforming().comparisonOf().sortAndCompareOneTableColumn(rows_table, sorting);
    }

}