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
package com.fast.actionsfactory.processing;

import com.fast.actionsfactory.api.IframeAnalyzer;
import com.fast.exception.FastRunTimeException;
import com.fast.objects.InstanceRecording;
import com.fast.register.i;
import com.fast.report.LogLevel;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.IOException;

import static com.fast.misclib.ConsoleFormatter.COLOR.CYAN;
import static com.fast.misclib.ConsoleFormatter.ICON.HORSE;
import static com.fast.misclib.ConsoleFormatter.ICON.THUMBS_UP;


public class ExecuteLocatorProcessing {
    IframeAnalyzer iframe;

    public ExecuteLocatorProcessing() {
        this.iframe = InstanceRecording.getInstance(IframeAnalyzer.class);
    }

    /**
     * Receive a By lcoator. Evaluate it for parameterization and fetches value from prop file if requried and return final locator
     *
     * @param locator
     * @return BY Locator
     * @author nauman.shahid
     */
    public <T> T getRefinedLocator(T locator) {
        T finalRefinedLocator = null;
        if (locator instanceof By) {
            finalRefinedLocator = (T) i.perform().convert().stringToBy(getLocatorParameters(locator.toString()));
            iframe.setUpLocator((By) finalRefinedLocator);
        } else if (locator instanceof String) {
            finalRefinedLocator = (T) getLocatorParameters(locator.toString().split(":")[1]);
        }
        i.perform().report().newLine();
        i.perform().report().write(LogLevel.FAST_INFO, CYAN, HORSE, "Locator and Element Processing Starts");
        if (finalRefinedLocator == null)
            throw new FastRunTimeException("Locator processing failed: " + locator.toString());
        i.perform().report().write(LogLevel.FAST_INFO, THUMBS_UP, "Locator = " + finalRefinedLocator);
        return finalRefinedLocator;
    }


    /**
     * 1- Name properties file in small letters only
     * 2- Parameter naame should be in small . If its longer user underscore
     * Example: participants_table_id=GrantorSiteVisitContacts
     * 3- Syntax in gherkin:
     * ---siteVisits:-:participants_table_id---
     * 4- if paramter is needed to be added from saved value in properties file then: syntax in gherkin
     * ---SavedValue:-:Key---
     *
     * @param parameter
     * @return string element
     */
    private String handleLocatorParameter(String parameter) {
        if (parameter == null || !parameter.contains(":-:")) {
            return parameter;
        }
        String finalParameter = parameter;
        String[] parameters = parameter.split(":-:");
        if (parameters[0].equalsIgnoreCase("SavedValue")) {
            try {
                finalParameter = i.perform().Properties().getValue(parameters[1]);
            } catch (Exception e) {
                i.perform().report().write(LogLevel.FAST_ERROR, "Key :" + parameters[0] + " not found");
                Assert.fail("Scenario Failed: Key was not found.");
            }
        } else {
            try {
                finalParameter = i.perform().Properties().ReadPropertyFile(parameters[0] + ".properties", parameters[1]);
            } catch (IOException e) {
                i.perform().report().write(LogLevel.FAST_ERROR, "Properties file name :" + parameters[0] + " not found");
                Assert.fail("Scenario Failed: properties file was not found.");
            }
        }
        return finalParameter;
    }

    /**
     * get strinng lcoator and split is based on properties parameter if any
     *
     * @param locator
     * @return
     */
    private String getLocatorParameters(String locator) {
        if (!locator.contains("---")) {
            return locator;
        }

        String parameterizedLocator = locator;
        boolean locatorTypeIsXpath = locator.contains("xpath:");
        for (String i : locator.split("---")) {
            if (i.contains(":-:")) {
                parameterizedLocator = parameterizedLocator.replace("---" + i + "---", handleLocatorParameter(i));
                if (locatorTypeIsXpath) {
                    //fast.perform().xpath().translate(parameterizedLocator); To be added asap
                }
            }
        }
        return parameterizedLocator;
    }

}
