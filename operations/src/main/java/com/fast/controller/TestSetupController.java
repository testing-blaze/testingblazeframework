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
package com.fast.controller;

import com.fast.misclib.ScreenCapture;
import com.fast.objects.InstanceRecording;
import com.fast.register.EnvironmentFetcher;
import com.fast.register.i;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static com.fast.misclib.ConsoleFormatter.COLOR;
import static com.fast.misclib.ConsoleFormatter.setTextColor;

/**
 * device responsible for connecting dots between projects framework and
 * core framework
 * Currently implemented in calling projects
 *
 * @author nauman.shahid
 */
public final class TestSetupController {
    private DeviceBucket device;
    private int retryCount = 0;
    public static UsersController usersController;

    static {
        createLogo();
    }

    /**
     * setup the launching device
     *
     * @param device
     * @author nauman.shahid
     */
    public TestSetupController(DeviceBucket device) {
        this.device = device;
    }

    /**
     * Handles various pre-test execution parameters
     *
     * @throws IOException
     * @throws AWTException
     * @author nauman.shahid
     */
    public void initializer(Scenario scenario) throws IOException, AWTException {
        ScenarioController.setScenario(scenario);

        if ("true".equalsIgnoreCase(System.getProperty("recordVideo"))) {
            ScreenCapture.startRecordVideo();
        }
        if ("true".equalsIgnoreCase(System.getProperty("docker"))) {
            DockerController.startDocker();
        }
        if ("android".equalsIgnoreCase(EnvironmentFetcher.getDevice()) || "ios".equalsIgnoreCase(EnvironmentFetcher.getDevice())) {
            runMobileApp();
        } else {
            runBrowser();
        }

        ScenarioController.printInitialLogs();
    }

    /**
     * Handles various post test execution parameters
     *
     * @throws IOException
     */
    public void theEnd() throws IOException {
        ScenarioController.printFinalLogs();

        if ("true".equalsIgnoreCase(System.getProperty("docker"))) {
            DockerController.stopDocker();
        }
        if ("android".equalsIgnoreCase(EnvironmentFetcher.getDevice()) || "ios".equalsIgnoreCase(EnvironmentFetcher.getDevice())) {
            mobileWrapUp();
        } else {
            browserWrapUp();
        }

        if (null != System.getProperty("evaluatePerformance") && "true".equalsIgnoreCase(System.getProperty("evaluatePerformance")) && "chrome".equalsIgnoreCase(EnvironmentFetcher.getDevice())) {
            generateChromeBrowserPerformanceLog();
        }
        InstanceRecording.flushInstance();
    }

    private void generateChromeBrowserPerformanceLog() {
        List<LogEntry> performanceEntries = device.getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
        List<LogEntry> browserEntries = device.getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
        for (LogEntry entry : performanceEntries) {
            i.perform().file().writeInFile(System.getProperty("user.dir") + "/target/Automation-Report/performance.txt", entry.toString());
        }
        for (LogEntry entry : browserEntries) {
            i.perform().file().writeInFile(System.getProperty("user.dir") + "/target/Automation-Report/browser.txt", entry.toString());
        }
    }

    /**
     * Capture screen shot to add up to cucumber report
     *
     * @author nauman.shahid
     */
    private void captureScreenshot() {
        try {
            if (System.getProperty("enableFullScreenShot") != null && "true".equalsIgnoreCase(System.getProperty("enableFullScreenShot"))) {
                ScenarioController.getScenario().embed((i.perform().convert().imageToByteArray(i.perform().screenCapture().captureFullScreenShot(), "PNG")), "image/png");
            } else {
                ScenarioController.getScenario().embed(((TakesScreenshot) device.getDriver()).getScreenshotAs(OutputType.BYTES), "image/png");
            }
        } catch (Exception e){
            // Handles exception if thrown for any reason
        }
    }

    /**
     * handles users instance between project frameworks and core framework
     *
     * @param usersController
     * @author nauman.shahid
     */

    public void usersController(UsersController usersController) {
        TestSetupController.usersController = usersController;
    }

    /**
     * @throws IOException
     * @author nauman.shahid
     */
    private void runBrowser() {
        device.setupController();
        device.getDriver().get(EnvironmentFetcher.getEnvironmentUrl().toUpperCase());
    }

    /**
     * @author nauman.shahid
     */
    private void runMobileApp() {
        AppiumController.startServer();
        device.setupController();
    }

    /**
     * @throws IOException
     * @author nauman.shahid
     */
    private void browserWrapUp() throws IOException {
        if ("true".equalsIgnoreCase(System.getProperty("recordVideo"))) {
            ScreenCapture.stopRecordVideo();
        }

        if ((ScenarioController.getScenario().isFailed()) || ("true".equalsIgnoreCase(System.getProperty("enableScreenShotsForAll")))) {
            captureScreenshot();
        }

        device.getDriver().manage().deleteAllCookies();
        device.getDriver().quit();
        device.stopServiceProvider();
    }

    /**
     * @author nauman.shahid
     */
    private void mobileWrapUp() {
        captureScreenshot();
        device.getDriver().quit();
        AppiumController.stopServer();
    }

    private static void createLogo() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(setTextColor(COLOR.values()[i], "*".repeat(i)));
        }
        System.out.println("Fregata Automated Testing Solution [REI-FAST] - Apache License 2.0");
        for (int i = 5; i >= 1; i--) {
            System.out.println(setTextColor(COLOR.values()[i], "*".repeat(i)));
        }
    }
}
