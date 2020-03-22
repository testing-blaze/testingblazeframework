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
package com.fast.actionsfactory.abstracts;

import com.fast.objects.Elements;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public interface Element {

    <T> WebElement locator(T locator, Boolean processing) ;

    <T> WebElement nestedElement(WebElement element, T locator);

    <T> List<Elements> locators(T locator, Boolean processing);

    <T> List<Elements> nestedElementsList(WebElement element, T locator);

    <T> Select selectLocator(T locator, Boolean processing);
}
