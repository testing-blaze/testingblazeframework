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
package com.testingblaze.actionsfactory.gherkin;

import com.testingblaze.actionsfactory.api.ByUsing;
import com.testingblaze.controller.ReportingLogsPlugin;
import com.testingblaze.exception.TestingBlazeExceptionWithoutStackTrace;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Description;

import javax.swing.*;
import java.awt.*;

public final class TestBlazeActionSteps {

    @When("^I pause execution$")
    public void pauseExecution() throws Throwable {
        Toolkit.getDefaultToolkit().beep();
        Thread.sleep(1000);
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, "Execution paused. Click \"OK\" to resume execution.");
    }

    @When("^I pause execution for \"(\\d+)\" seconds$")
    public void pauseExecutionForMinutes(int seconds) throws Throwable {
        Thread.sleep(seconds * 1000);
    }

    @When("^I wait for \"(\\d+)\" seconds$")
    public void waitForMinutes(int seconds) throws Throwable {
        Thread.sleep(seconds * 1000);
    }

    @Description("To switch to Parent Frame")
    @When("^I switch to Parent iframe$")
    public void switchToParentFrame() {
        I.amPerforming().switchTo().parentFrame();
    }

    @Description("To switch to specific Frame")
    @When("I switch to iframe with id \"([^\"]*)\"$")
    public void switchToFrame(String iframeID) {
        I.amPerforming().switchTo().frame(By.xpath("//iframe[@id='" + iframeID + "']"));
    }

    @Description("refresh page")
    @When("^I refresh the page$")
    public void refreshPage() {
        I.amPerforming().browserOperationsTo().refreshPage();
    }

    @Description("switch to parent application page")
    @When("^I switch to parent tab$")
    public void switchToParentPage() {
        I.amPerforming().switchTo().windowHandler(0);
    }

    @Description("switch to tab")
    @When("^I switch to tab number \"(\\d+)\"$")
    public void switchToTab(int tabNumber) {
        I.amPerforming().switchTo().windowHandler(tabNumber);
    }

    @Description("To be used with self healing only")
    @When("I click {string}")
    public void iClick(String locatorName) {
        if (StringUtils.containsIgnoreCase(locatorName, "mobile")) {
            if (StringUtils.containsIgnoreCase(locatorName, "xpath")) {
                I.amPerforming().click().on(ByUsing.healingMobileXpathName(locatorName.split(":")[2]));
            } else if (StringUtils.containsIgnoreCase(locatorName, "id")) {
                I.amPerforming().click().on(ByUsing.healingMobileIdName(locatorName.split(":")[2]));
            } else if (StringUtils.containsIgnoreCase(locatorName, "css")) {
                I.amPerforming().click().on(ByUsing.healingMobileCssName(locatorName.split(":")[2]));
            }
        } else {
            if (StringUtils.containsIgnoreCase(locatorName, "xpath")) {
                I.amPerforming().click().on(ByUsing.healingXpathName(locatorName.split(":")[1]));
            } else if (StringUtils.containsIgnoreCase(locatorName, "id")) {
                I.amPerforming().click().on(ByUsing.healingIdName(locatorName.split(":")[1]));
            } else if (StringUtils.containsIgnoreCase(locatorName, "css")) {
                I.amPerforming().click().on(ByUsing.healingCssName(locatorName.split(":")[1]));
            }
        }
    }

    @Description("To be used with self healing only")
    @When("I input {string} in {string}")
    public void iInputIn(String text, String locatorName) {
        if (StringUtils.containsIgnoreCase(locatorName, "mobile")) {
            if (StringUtils.containsIgnoreCase(locatorName, "xpath")) {
                I.amPerforming().textInput().in(ByUsing.healingMobileXpathName(locatorName.split(":")[2]), text);
            } else if (StringUtils.containsIgnoreCase(locatorName, "id")) {
                I.amPerforming().textInput().in(ByUsing.healingMobileIdName(locatorName.split(":")[2]), text);
            } else if (StringUtils.containsIgnoreCase(locatorName, "css")) {
                I.amPerforming().textInput().in(ByUsing.healingMobileCssName(locatorName.split(":")[2]), text);
            }
        } else {
            if (StringUtils.containsIgnoreCase(locatorName, "xpath")) {
                I.amPerforming().textInput().in(ByUsing.healingXpathName(locatorName.split(":")[1]), text);
            } else if (StringUtils.containsIgnoreCase(locatorName, "id")) {
                I.amPerforming().textInput().in(ByUsing.healingIdName(locatorName.split(":")[1]), text);
            } else if (StringUtils.containsIgnoreCase(locatorName, "css")) {
                I.amPerforming().textInput().in(ByUsing.healingCssName(locatorName.split(":")[1]), text);
            }
        }
    }

    @Description("perform assertions")
    @Then("^(?i)I assert \"([^\"]*)\" (is|is not) (displayed|present)(?-i)$")
    public void validate(String content, String isOrNot, String displayedorPresent) {
        // This step is empty. I do not know why. It can probably be deleted.
        // Leaving it in case there are future plans for it.
    }

    @Then("^I verify that no soft assertions failed in the previous step$")
    public void verifyingPreviousStepSoftAssertions() throws TestingBlazeExceptionWithoutStackTrace {
        if (ReportingLogsPlugin.getErrorsFromPreviousStep().size() > 0) {
            throw new TestingBlazeExceptionWithoutStackTrace("Soft assertions failed in the previous step.");
        } else {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "No soft assertions failed in the previous step.");
        }
    }
}
