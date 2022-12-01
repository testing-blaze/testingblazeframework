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
import com.testingblaze.controller.*;
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
    protected Thread aliveThread;
    private final int threadCount = System.getProperty("threads") == null ? 0 : Integer.parseInt(System.getProperty("threads"));

    public toquzjGnQQTBR(DeviceBucket device, Avrb8aYEmH coreLib, TestSetupController registerSetup) {
        this.registerSetup = registerSetup;
        InstanceRecording.recordInstance(Avrb8aYEmH.class, coreLib);
        InstanceRecording.recordInstance(DeviceBucket.class, device);
    }

    @Before(order = 0)
    public void registerSetup(Scenario scenario) throws IOException, AWTException {
        registerSetup.initializer(scenario);
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    }

    @After(order = 0)
    public void unRegisterSetup() throws IOException {
        performClosureJobs();
        registerSetup.theEnd();
    }

    /**
     * perform After All jobs.
     *
     * @author nauman.shahid
     */
    private synchronized void performClosureJobs() {
        int scenarioCountClosure = ScenarioController.getTotalScenarioCount() - ScenarioController.getExecutedScenarioCount();

        if (threadCount < 2) {
            if (scenarioCountClosure == 0) {
                triggerReportAnalyticsGeneration();
            }
        }

        if (threadCount > 1) {
            int testJvmCount = 0;
            for (VirtualMachineDescriptor listOfProcess : VirtualMachine.list()) {
                if (listOfProcess.toString().contains("jvmRun")) {
                    testJvmCount++;
                    if (testJvmCount > 1) break;
                }
            }
            if (testJvmCount == 1) {
                triggerReportAnalyticsGeneration();
            }
        }
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
    private void triggerReportAnalyticsGeneration() {
        try {
            new ReportAnalyzer().publishReportAnalytics();
        } catch (Exception e) {
            System.out.println("!.!.!.! Report Publishing Failed ?.?.?.?");
            e.printStackTrace();
        }
        try {
            System.out.println("Report Analysis Started ....");
            new ReportAnalyzer().executeAnalysis();
            System.out.println("Report Analysis Completed.");
        } catch (Exception e) {
            System.out.println("!.!.!.! Report Analysis Failed ?.?.?.?");
        }

    }


    /**
     * handles report consolidation according to thread count
     *
     * @@author nauman.shahid
     */
    private void triggerMandatoryClosureJobs() {
        Thread performReportAnalysisActivities = new Thread(() -> {
            int testJvvmCount = 0;
            for (VirtualMachineDescriptor listOfProcess : VirtualMachine.list()) {
                if (listOfProcess.toString().contains("jvmRun") && listOfProcess.toString().contains("jvmRun")) {
                    testJvvmCount++;
                    if (testJvvmCount > 1) break;
                }
            }
            int threadCount = System.getProperty("threads") == null ? 0 : Integer.parseInt(System.getProperty("threads"));
            if (threadCount < 2 || testJvvmCount == 1) {
                try {
                    new ReportAnalyzer().publishReportAnalytics();
                } catch (Exception e) {
                    System.out.println("!.!.!.! Report Publishing Failed ?.?.?.?");
                    e.printStackTrace();
                }
                try {
                    System.out.println("Report Analysis Started ....");
                    new ReportAnalyzer().executeAnalysis();
                    System.out.println("Report Analysis Completed.");
                } catch (Exception e) {
                    System.out.println("!.!.!.! Report Analysis Failed ?.?.?.?");
                }

            }
        });
        try {
            System.out.println("AT Joining Hook Started");
            aliveThread.join(15000);
            System.out.println("AT Joining Hook Complete");
        } catch (Exception e) {
            System.out.println("AT Joining Hook Failed");
        }
        Runtime.getRuntime().addShutdownHook(performReportAnalysisActivities);

    }


}
