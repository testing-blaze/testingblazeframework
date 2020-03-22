/*
 * Copyright 2020
 *
 * This file is part of Fregata Automated Testing Solution [FAST].
 *
 * FAST is licensed under the Apache License, Version
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
package com.fast.actionsfactory.type;

import com.fast.actionsfactory.abstracts.Action;
import com.fast.actionsfactory.abstracts.Element;
import com.fast.actionsfactory.api.ExecuteCoreActions;
import com.fast.objects.InstanceRecording;
import org.openqa.selenium.support.ui.Select;

public class DropDown {
    private Element elementApi;
    private Action executeAction;

    public DropDown() {
        elementApi = InstanceRecording.getInstance(Element.class);
        executeAction = InstanceRecording.getInstance(ExecuteCoreActions.class);
    }

    /**
     * select on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> Select from(T locator) {
        return from(locator, true);
    }

    /**
     * select on webPage , mobilePage , AngularPage
     *
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> Select from(T locator, Boolean processing) {
        return elementApi.selectLocator(locator, processing);
    }

}
