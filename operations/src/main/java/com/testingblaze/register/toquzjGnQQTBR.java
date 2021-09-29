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
package com.testingblaze.register;


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.testingblaze.controller.Avrb8aYEmH;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.controller.ReportingLogsPlugin;
import com.testingblaze.controller.ScenarioController;
import com.testingblaze.controller.TestSetupController;
import com.testingblaze.exception.TestingBlazeExceptionWithoutStackTrace;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.report.LogLevel;
import com.testingblaze.report.ReportAnalyzer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;

public final class toquzjGnQQTBR {
    private final TestSetupController registerSetup;
    private ReportAnalyzer reportAnalyzer;
    private static Boolean analyzeReportsJvmFlag = false;

    public toquzjGnQQTBR(DeviceBucket device, Avrb8aYEmH coreLib, TestSetupController registerSetup) {
        this.registerSetup = registerSetup;
        InstanceRecording.recordInstance(Avrb8aYEmH.class, coreLib);
        InstanceRecording.recordInstance(DeviceBucket.class, device);
        if (!analyzeReportsJvmFlag) {
            triggerMandatoryClosureJobs();
            analyzeReportsJvmFlag = true;
        }

    }

    @Before(order = 0)
    public void registerSetup(Scenario scenario) throws IOException, AWTException {
        registerSetup.initializer(scenario);
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    }

    @After(order = 0)
    public void unRegisterSetup() throws IOException {
        registerSetup.theEnd();
    }

    @After(order = 5)
    public void verifyingScenarioSoftAssertions() throws TestingBlazeExceptionWithoutStackTrace {
        if (ScenarioController.getScenario().isFailed()) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "The scenario is already failed.  Skipping check of soft assertions.");
        } else if (ReportingLogsPlugin.getErrorsFromScenario().size() > 0) {
            throw new TestingBlazeExceptionWithoutStackTrace("The following soft assertions failed in the scenario:\n"
                    + String.join("\n", ReportingLogsPlugin.getErrorsFromScenario()));
        } else {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "No soft assertions failed in the scenario.");
        }
    }

    /**
     * handles report consolidation according to thread count
     *
     * @@author nauman.shahid
     */
    private void triggerMandatoryClosureJobs() {
        Thread performClosureActivities = new Thread(() -> {
            int testJvvmCount = 0;
            for(VirtualMachineDescriptor listOfProcess:VirtualMachine.list()) {
                if(listOfProcess.toString().contains("jvmRun") && listOfProcess.toString().contains("jvmRun")) {
                    testJvvmCount++;
                    if(testJvvmCount > 1) break;
                }
            }
            if(testJvvmCount == 1) {
            if (reportAnalyzer == null) {
                reportAnalyzer = new ReportAnalyzer();
            }
            try {
                System.out.println("Report Analysis Started ....");
                reportAnalyzer.executeAnalysis();
                System.out.println("Report Analysis Completed.");
            } catch (Exception e) {
                System.out.println("Report Analysis Failed");
            }
        }});
        Runtime.getRuntime().addShutdownHook(performClosureActivities);
    }

}
