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
package com.testingblaze.actionsfactory.type;


import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.objects.InstanceRecording;
import org.openqa.selenium.WebElement;

public class ElementReference {
    private Element elementApi;
    private ElementsList elementsList;

    // Accessed from Fetch class
    public ElementReference() {
        elementApi = InstanceRecording.getInstance(Element.class);
        this.elementsList = new ElementsList();
    }

    /**
     * Only to grasp element on webPage , mobilePage , AngularPage: Not allowed to use any function like click or send keys from here.
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     *                @return webElement reference
     * @author nauman.shahid
     */
    public <T> WebElement of(T locator) {
        return of(locator, true);
    }

    /**
     * Only to grasp element on webPage , mobilePage , AngularPage: Not allowed to use any function like click or send keys from here.
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @param processing true/false
     * @return webElement reference
     * @author nauman.shahid
     */
    public <T> WebElement of(T locator, Boolean processing) {
        return elementApi.locator(locator,processing);
    }

    /**
     * Only to grasp nested element on webPage , mobilePage , AngularPage: Not allowed to use any function like click or send keys from here.
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @param element the main element
     * @return webElement
     * @author nauman.shahid
     */
    public <T> WebElement ofNested(WebElement element, T locator) {
        return elementApi.nestedElement(element, locator);
    }

    /**
     * Only to grasp element list on webPage , mobilePage , AngularPage: Not allowed to use any function like click or send keys from here.
     * Works for ->   Mobile , Ng , By :
     *                @return List<Elements></Elements> reference
     * @author nauman.shahid
     */
    public ElementsList forListOfElements() {
        return elementsList;
    }

    /**
     * clicks on android specific locator strategy
     * @param locator: Mobile.
     * @
     */
    public void ofAndroidApp(String locator) {
        // to be added
    }

    /**
     * clicks on IOS specific locator strategy
     * @param locator: Mobile.
     * @
     */
    public void ofIosApp(String locator) {
        // to be added
    }


}
