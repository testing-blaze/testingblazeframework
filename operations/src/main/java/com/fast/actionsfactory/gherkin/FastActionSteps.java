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
package com.fast.actionsfactory.gherkin;

import com.fast.controller.ReportingLogsPlugin;
import com.fast.exception.FastExceptionWithoutStackTrace;
import com.fast.register.i;
import com.fast.report.LogLevel;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Description;

public final class FastActionSteps {

    /*
    @When("^I pause execution$")
    public void pauseExecution() throws Throwable {
        Toolkit.getDefaultToolkit().beep();
        Thread.sleep(1000);
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, "Execution paused. Click \"OK\" to resume execution.");
    }*/

    @When("^I pause execution for \"(\\d+)\" seconds$")
    public void pauseExecutionForMinutes(int seconds) throws Throwable {
        Thread.sleep(seconds * 1000);
    }

    @Description("To switch to Parent Frame")
    @When("^I switch to Parent iframe$")
    public void switchToParentFrame() {
        i.amPerforming().switchTo().parentFrame();
    }

    @Description("To switch to specific Frame")
    @When("I switch to iframe with id \"([^\"]*)\"$")
    public void switchToFrame(String iframeID) {
        i.amPerforming().switchTo().frame(By.xpath("//iframe[@id='" + iframeID + "']"));
    }

    @Description("refresh page")
    @When("^I refresh the page$")
    public void refreshPage() {
        i.amPerforming().get().pageRefresh();
    }

    @Description("switch to parent application page")
    @When("^I switch to parent tab$")
    public void switchToParentPage() {
        i.amPerforming().switchTo().windowHandler(0);
    }

    @Description("switch to tab")
    @When("^I switch to tab number \"(\\d+)\"$")
    public void switchToTab(int tabNumber) {
        i.amPerforming().switchTo().windowHandler(tabNumber);
    }

    @Description("wait for seconds")
    @When("^I wait for \"(\\d+)\" seconds$")
    public void waitFor(int waitTime) {
        i.amPerforming().waitFor().makeThreadSleep(waitTime);
    }

    @Description("perform assertions")
    @Then("^(?i)I assert \"([^\"]*)\" (is|is not) (displayed|present)(?-i)$")
    public void validate(String content, String isOrNot, String displayedorPresent) {
        // This step is empty. I do not know why. It can probably be deleted.
        // Leaving it in case there are future plans for it.
    }

    @Then("^I verify that no soft assertions failed in the previous step$")
    public void verifyingPreviousStepSoftAssertions() throws FastExceptionWithoutStackTrace {
        if (ReportingLogsPlugin.getErrorsFromPreviousStep().size() > 0) {
            throw new FastExceptionWithoutStackTrace("Soft assertions failed in the previous step.");
        } else {
            i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, "No soft assertions failed in the previous step.");
        }
    }
}
