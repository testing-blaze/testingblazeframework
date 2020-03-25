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
package com.testblaze.objects;

import com.testblaze.actionsfactory.elementfunctions.FindMyElements;
import com.testblaze.actionsfactory.type.Click;
import com.testblaze.actionsfactory.type.EnterText;
import com.testblaze.actionsfactory.type.Is;
import com.testblaze.register.i;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Elements {
    private WebElement element;

    public Elements(WebElement element) {
        this.element = element;
    }

    public void click() {
        InstanceRecording.getInstance(Click.class).on(element);
    }

    public void enterText(String text) {
        InstanceRecording.getInstance(EnterText.class).in(element, text);
    }

    public void clearText() {
        InstanceRecording.getInstance(EnterText.class).toClear(element);
    }

    public String getAttributes(String attributeName) {
        return element.getAttribute(attributeName);
    }

    public String getCssValue(String cssValue) {
        return element.getCssValue(cssValue);
    }

    public String getText() {
        return element.getText();
    }

    public Boolean isStale() {
        return InstanceRecording.getInstance(Is.class).isStale(element);
    }

    public Boolean isDisplayed() {
        return InstanceRecording.getInstance(Is.class).isDisplayed(element);
    }

    public Boolean isEnabled() {
        return InstanceRecording.getInstance(Is.class).isEnabled(element);
    }

    public Boolean isSelected() {
        return InstanceRecording.getInstance(Is.class).isSelected(element);
    }

    public void switchFrame() {
        i.amPerforming().switchTo().frame(element);
    }

    public WebElement nestedElement(By locator) {
        return InstanceRecording.getInstance(FindMyElements.class).getNestedElement(this.element, locator, true);
    }
}
