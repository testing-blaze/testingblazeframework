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
import com.testingblaze.objects.Elements;
import com.testingblaze.objects.InstanceRecording;
import org.openqa.selenium.WebElement;

import java.util.List;

public class akzkdaiqAC {
    private Element elementApi;

    public akzkdaiqAC() {
        elementApi = InstanceRecording.getInstance(Element.class);
    }

    /**
     * Only to grasp element list on webPage , mobilePage , AngularPage: Not allowed to use any function like click or send keys from here.
     * @param locator Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     *                @return List<Elements> reference
     * @author nauman.shahid
     */
    public <T> List<Elements> of(T locator) {
        return of(locator, true);
    }

    /**
     * Only to grasp element list on webPage , mobilePage , AngularPage: Not allowed to use any function like click or send keys from here.
     * @param processing true/false
     * @param locator Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     *                @return List<Elements>reference
     * @author nauman.shahid
     */
    public <T> List<Elements> of(T locator, Boolean processing) {
        return elementApi.locators(locator, processing);
    }

    /**
     * Only to grasp element list on webPage , mobilePage , AngularPage: Not allowed to use any function like click or send keys from here.
     * @param element the main element
     * @param locator Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     *                @return List<Elements> reference
     * @author nauman.shahid
     */
    public <T> List<Elements> ofNested(WebElement element, T locator) {
        return elementApi.nestedElementsList(element, locator);
    }


}
