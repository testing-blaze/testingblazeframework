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
package com.testingblaze.actionsfactory.api;

import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.actionsfactory.elementfunctions.FindMyElements;
import com.testingblaze.actionsfactory.elementfunctions.Mobile;
import com.testingblaze.actionsfactory.elementfunctions.Ng;
import com.testingblaze.objects.Elements;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ElementAPI implements Element {

    private FindMyElements findMyElements;
    private Mobile findMobileElement;
    private Ng ng;

    public ElementAPI() {
        this.findMyElements = InstanceRecording.getInstance(FindMyElements.class);
        this.findMobileElement = InstanceRecording.getInstance(Mobile.class);
        this.ng = InstanceRecording.getInstance(Ng.class);
    }

    @Override
    public <T> WebElement locator(T locator, Boolean processing) {
        WebElement element = null;
        if(locator instanceof WebElement) return (WebElement) locator;
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Getting single element located by: " + locator.toString());
        if (locator instanceof By) element = findMyElements.getElement((By) locator, processing);
        else if (locator instanceof String && (locator.toString().startsWith("ByMobile"))) {
            element = getMobileElement(((String) locator).split("::")[1], processing);
        } else if (locator instanceof String && (locator.toString().startsWith("ByAngular"))) {
            element = ng.getNgElement(((String) locator).split("::")[1], processing);
        }
        return element;
    }

    /**
     * *********************** Important *********************
     * This method is currently expected to handle nested element for mobile and Ng as well
     *
     * @param webElement
     * @param locator
     * @param <T>
     * @return
     */

    @Override
    public <T> WebElement nestedElement(WebElement webElement, T locator) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Getting nested element located by: " + locator.toString());
        WebElement element = null;
        if (locator instanceof By) element = findMyElements.getNestedElement(webElement, (By) locator, true);
        return element;
    }

    @Override
    public <T> List<Elements> locators(T locator, Boolean processing) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Getting list of elements located by: " + locator.toString());
        List<WebElement> elementList = null;
        if (locator instanceof By) {
            elementList = findMyElements.getElements((By) locator, processing);
        } else if (locator instanceof String && ((String) locator).startsWith("ByMobile")) {
            elementList = getMobileElements(((String) locator).split("::")[1], processing);
        } else if (locator instanceof String && ((String) locator).startsWith("ByAngular")) {
            elementList = ng.getNgElements(((String) locator).split("::")[1], processing);
        }
        List<Elements> testBlazeElements = new ArrayList<>();
        if (elementList.size() > 0) {
            for (WebElement element : elementList) {
                testBlazeElements.add(new Elements(element));
            }
        }
        return testBlazeElements;
    }

    /**
     * *********************** Important *********************
     * This method is expected to handle nested elements for mobile and ng as well
     *
     * @param element
     * @param locator
     * @param <T>
     * @return
     */
    @Override
    public <T> List<Elements> nestedElementsList(WebElement element, T locator) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Getting nested list of elements located by: " + locator.toString());
        List<WebElement> elementList = findMyElements.getNestedElementList(element, (By) locator);
        List<Elements> testBlazeElements = new ArrayList<>();
        if (elementList.size() > 0) {
            for (WebElement ele : elementList) {
                testBlazeElements.add(new Elements(ele));
            }
        }
        return testBlazeElements;
    }

    @Override
    public <T> Select selectLocator(T locatorParameter, Boolean processing) {
        return new Select(locator(locatorParameter, processing));
    }

    /**
     * Accessing private methods of mobile class
     *
     * @param locator
     * @param processing
     * @return
     */
    private WebElement getMobileElement(String locator, Boolean processing) {
        WebElement element = null;
        try {
            Method mobileElement = Mobile.class.getDeclaredMethod("getMobileElement", String.class, Boolean.class);
            mobileElement.setAccessible(true);
            element = (WebElement) mobileElement.invoke(findMobileElement, locator, processing);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return element;
    }

    /**
     * Accessing private methods of mobile class
     *
     * @param locator
     * @param processing
     * @return
     */
    private List<WebElement> getMobileElements(String locator, Boolean processing) {
        List<WebElement> element = null;
        try {
            Method mobileElement = Mobile.class.getDeclaredMethod("getMobileElements", String.class, Boolean.class);
            mobileElement.setAccessible(true);
            element = (List<WebElement>) mobileElement.invoke(findMobileElement, locator, processing);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return element;
    }
}
