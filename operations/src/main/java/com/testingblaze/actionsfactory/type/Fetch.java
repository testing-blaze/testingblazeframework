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
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.misclib.KeysHandler;
import com.testingblaze.objects.InstanceRecording;
import org.openqa.selenium.WebDriver;

/**
 * get various functionalities for dates , urls , browser handling , navigations etc.
 *
 * @author nauman.shahid
 */
public final class Fetch {
    private final Element elementApi;

    public Fetch() {
        elementApi = InstanceRecording.getInstance(Element.class);
    }

    /**
     * get inner text on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @return inner text
     * @author nauman.shahid
     */
    public <T> String text(T locator) {
        return text(locator, true);
    }

    /**
     * get inner text on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @return inner text
     * @author nauman.shahid
     */
    public <T> String text(T locator, Boolean processing) {
        return elementApi.locator(locator, processing).getText();
    }

    /**
     * get attribute value on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @return attribute value
     * @author nauman.shahid
     */
    public <T> String attribute(T locator, String attribute) {
        return attribute(locator, attribute, true);
    }

    /**
     * get attribute value on webPage , mobilePage , AngularPage
     *
     * @param locator    -> Mobile , Ng , By :
     *                   Mobile.
     *                   Angular.
     *                   By.
     * @param processing
     * @return attribute value
     * @author nauman.shahid
     */
    public <T> String attribute(T locator, String attribute, Boolean processing) {
        return elementApi.locator(locator, processing).getAttribute(attribute);
    }

    /**
     * get css property on webPage , mobilePage , AngularPage
     *
     * @param locator  -> Mobile , Ng , By :
     *                 Mobile.
     *                 Angular.
     *                 By.
     * @param property
     * @return css value
     * @author nauman.shahid
     */
    public <T> String cssProperty(T locator, String property) {
        return cssProperty(locator, property, true);
    }

    /**
     * get css property on webPage , mobilePage , AngularPage
     *
     * @param locator    -> Mobile , Ng , By :
     *                   Mobile.
     *                   Angular.
     *                   By.
     * @param property
     * @param processing
     * @return css value
     * @author nauman.shahid
     */
    public <T> String cssProperty(T locator, String property, Boolean processing) {
        return elementApi.locator(locator, processing).getCssValue(property);
    }

}
