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

import com.paulhammant.ngwebdriver.ByAngular;
import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.actionsfactory.elementfunctions.FindMyElements;
import com.testingblaze.actionsfactory.elementfunctions.Mobile;
import com.testingblaze.actionsfactory.elementfunctions.Ng;
import com.testingblaze.healingdoor.Gateway;
import com.testingblaze.objects.Elements;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class ElementAPI implements Element {

    private final FindMyElements findMyElements;
    private final Mobile findMobileElement;
    private final Ng ng;

    public ElementAPI() {
        this.findMyElements = InstanceRecording.getInstance(FindMyElements.class);
        this.findMobileElement = InstanceRecording.getInstance(Mobile.class);
        this.ng = InstanceRecording.getInstance(Ng.class);
    }

    @Override
    public <T> WebElement locator(T locator, Boolean processing) {
        if (locator instanceof String) {
            return handleSelfHealingLocator((String) locator, processing);
        } else {
            return handleLocatorInstanceOf(locator, processing);
        }
    }

    @Override
    public <T> List<Elements> locators(T locator, Boolean processing) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Getting list of elements located by: " + locator.toString());
        if (locator instanceof String) {
            return handleSelfHealingLocators((String) locator, processing);
        } else {
            return handleLocatorsInstanceOf(locator, processing);
        }
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
        if (locator instanceof String) {
            var locatorType = ((String) locator).split(":")[0];
            var locatorName = ((String) locator).split(":")[1];
            element = findMyElements.getNestedElement(webElement, getBy(locatorType, Gateway.fetchLocatorFromDB(locatorType, locatorName)), true);
        } else if (locator instanceof By) {
            element = findMyElements.getNestedElement(webElement, (By) locator, true);
        }
        return element;
    }


    /**
     * *********************** Important *********************
     * This method is expected to handle nested elements for mobile and ng as well
     *
     * @param webElement
     * @param locator
     * @param <T>
     * @return
     */
    @Override
    public <T> List<Elements> nestedElementsList(WebElement webElement, T locator) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Getting nested list of elements located by: " + locator.toString());
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Getting nested list of elements located by: " + locator.toString());
        List<WebElement> elementList = new ArrayList<>();
        List<Elements> testBlazeElements = new ArrayList<>();

        if (locator instanceof String) {
            var locatorType = ((String) locator).split(":")[0];
            var locatorName = ((String) locator).split(":")[1];
            elementList = findMyElements.getNestedElementList(webElement, getBy(locatorType, Gateway.fetchLocatorFromDB(locatorType, locatorName)));
        } else if (locator instanceof By) {
            elementList = findMyElements.getNestedElementList(webElement, (By) locator);
        }

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

    /******************** Private Methods ***********************/

    private WebElement handleSelfHealingLocator(String locator, Boolean processing) {
        var locatorType = locator.split(":")[0];
        var locatorName = locator.split(":")[1];
        WebElement element = null;
        return handleLocatorInstanceOf(getBy(locatorType, Gateway.fetchLocatorFromDB(locatorType, locatorName)), processing);
    }

    private List<Elements> handleSelfHealingLocators(String locator, Boolean processing) {
        var locatorType = locator.split(":")[0];
        var locatorName = locator.split(":")[1];
        List<Elements> element = null;
        return handleLocatorsInstanceOf(getBy(locatorType, Gateway.fetchLocatorFromDB(locatorType, locatorName)), processing);
    }

    private <T> WebElement handleLocatorInstanceOf(T locator, Boolean processing) {
        if (locator instanceof WebElement) {
            return (WebElement) locator;
        }
        if (locator instanceof MobileBy) {
            return findMobileElement.getMobileElement((MobileBy) locator, processing);
        } else if (locator instanceof ByAngular.BaseBy) {
            return ng.getNgElement((ByAngular.BaseBy) locator, processing);
        } else {
            return findMyElements.getElement((By) locator, processing);
        }
    }

    private <T> List<Elements> handleLocatorsInstanceOf(T locator, Boolean processing) {
        List<WebElement> elementList = null;
        if (locator instanceof MobileBy) {
            elementList = findMobileElement.getMobileElements((MobileBy) locator, processing);
        } else if (locator instanceof ByAngular.BaseBy) {
            elementList = ng.getNgElements((ByAngular.BaseBy) locator, processing);
        } else if (locator instanceof By) {
            elementList = findMyElements.getElements((By) locator, processing);
        }

        List<Elements> testBlazeElements = new ArrayList<>();
        if (elementList.size() > 0) {
            for (WebElement element : elementList) {
                testBlazeElements.add(new Elements(element));
            }
        }
        return testBlazeElements;
    }

    public static By getBy(String type, String expression) {
        By getBy = null;
        switch (type.toLowerCase()) {
            case "by-xpath":
                getBy = By.xpath(expression);
                break;
            case "by-id":
                getBy = By.id(expression);
                break;
            case "mobileby-xpath":
                getBy = MobileBy.xpath(expression);
                break;
            case "mobileby-id":
                getBy = MobileBy.id(expression);
                break;
            case "byangular.baseby-xpath":
                getBy = ByAngular.BaseBy.xpath(expression);
                break;
            case "byangular.baseby-id":
                getBy = ByAngular.BaseBy.id(expression);
                break;
        }
        return getBy;
    }
}
