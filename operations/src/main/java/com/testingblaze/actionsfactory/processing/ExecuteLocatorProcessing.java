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
package com.testingblaze.actionsfactory.processing;

import com.testingblaze.actionsfactory.api.IframeAnalyzer;
import com.testingblaze.exception.TestingBlazeRunTimeException;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.IOException;


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
            finalRefinedLocator = (T) I.amPerforming().conversionOf().stringToBy(getLocatorParameters(locator.toString()));
            iframe.setUpLocator((By) finalRefinedLocator);
        } else if (locator instanceof String) {
            finalRefinedLocator = (T) getLocatorParameters(locator.toString().split(":")[1]);
        }
        I.amPerforming().updatingOfReportWith().newLine();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO,  "Locator and Element Processing Starts");
        if (finalRefinedLocator == null)
            throw new TestingBlazeRunTimeException("Locator processing failed: " + locator.toString());
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO,  "Locator = " + finalRefinedLocator);
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
                finalParameter = I.amPerforming().propertiesFileOperationsTo().getValue(parameters[1]);
            } catch (Exception e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Key :" + parameters[0] + " not found");
                Assert.fail("Scenario Failed: Key was not found.");
            }
        } else {
            try {
                finalParameter = I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile(parameters[0] + ".properties", parameters[1]);
            } catch (IOException e) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Properties file name :" + parameters[0] + " not found");
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
            }
        }
        return parameterizedLocator;
    }

}
