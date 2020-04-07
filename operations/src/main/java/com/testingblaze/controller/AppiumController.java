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
package com.testingblaze.controller;

import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author nauman.shahid
 * @category Manages appium server initialization and ports management
 */

public final class AppiumController {
    private static AppiumDriverLocalService service;
    private static final int DEFAULT_PORT = System.getProperty("port") != null ? Integer.parseInt(System.getProperty("port")) : 4723;

    /**
     * Build Appium Service Execute Appium Server
     */
    public static void startServer() {
        killPort();
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(DEFAULT_PORT)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .build();
        service.start();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Appium Server is running: " + service.isRunning());
    }

    /**
     * Stops Service
     */
    public static void stopServer() {
        service.stop();
        killPort();
    }

    /**
     * default method to close the port if it is open due to last execution
     */
    private static void killPort() {
        try {
            new ServerSocket(DEFAULT_PORT).close();
        } catch (IOException e) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Socket closed");
        }
    }
}
