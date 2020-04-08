/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Solution.
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

import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.util.Arrays;

/**
 * Keep track of environment information
 *
 * @author nauman.shahid
 */
public final class EnvironmentFactory {
    private static String projectName;

    public static String getProjectName() {
        if (projectName == null) return "";
        else
            return projectName;
    }

    public static void setProjectName(String projectName) {
        EnvironmentFactory.projectName = projectName;
    }

    private static String orgName;

    public static String getOrgName() {
        if (orgName == null) return "";
        else
            return orgName;
    }

    public static void setOrgName(String orgName) {
        EnvironmentFactory.orgName = orgName;
    }


    private static String projectPath;

    public static String getProjectPath() {
        if (projectPath == null) return "";
        else
            return projectPath;
    }

    public static void setProjectPath(String projectPath) {
        EnvironmentFactory.projectPath = projectPath;
    }


    private static String environmentUrl;

    public static String getEnvironmentUrl() {
        if (environmentUrl == null) {
            try {
                environmentUrl = I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile("environment.properties", EnvironmentFactory.getEnvironmentName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return environmentUrl;
    }

    private static String environmentName;

    public static String getEnvironmentName() {
        if (environmentName == null) {
            environmentName = System.getProperty("env") != null ? System.getProperty("env").toUpperCase() : "QA";
        }
        return environmentName;
    }

    private static String appName;
    private static boolean checkedAppName = false;

    public static String getAppName() {
        if (!checkedAppName) {
            checkedAppName = true;
            appName = System.getProperty("appName");
        }
        return appName;
    }

    private static String hub;

    public static String getHub() {
        if (hub == null) {
            hub = System.getProperty("hub") != null ? System.getProperty("hub") : "local";
        }
        return hub;
    }

    private static String device;

    public static String getDevice() {
        if (device == null) {
            device = System.getProperty("device") != null ? System.getProperty("device") : "chrome";
        }
        return device;
    }

    private static String deviceVersion;

    public static String getDeviceVersion() {
        if (deviceVersion == null) {
            WebDriver driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
            if (driver instanceof RemoteWebDriver) {
                deviceVersion = ((RemoteWebDriver) driver).getCapabilities().getVersion();
            } else {
                deviceVersion = "Not Available";
            }
        }
        return deviceVersion;
    }

    private static String platformInfo;

    public static String getPlatformInfo() {
        if (platformInfo == null) {
            WebDriver driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
            if (driver instanceof RemoteWebDriver) {
                platformInfo = Arrays.toString(((RemoteWebDriver) driver).getCapabilities().getPlatform().getPartOfOsName());
            } else {
                platformInfo = "Not Available";
            }
        }
        return platformInfo;
    }

    private static Long maxWaitTime;

    public static Long getMaxWaitTime() {
        if (maxWaitTime == null) {
            maxWaitTime = System.getProperty("waitTime") != null ? Long.parseLong(System.getProperty("waitTime")) : 10;
        }
        return maxWaitTime;
    }

    private static String executionMode;

    public static String getExecutionMode() {
        if (executionMode == null) {
            executionMode = "true".equalsIgnoreCase(System.getProperty("headless")) ? "Headless" : "Browser UI";
        }
        return executionMode;
    }

    private static Integer slowDownExecutionTime;

    public static Integer getSlowDownExecutionTime() {
        if (slowDownExecutionTime == null) {
            slowDownExecutionTime = System.getProperty("slowDownExecution") != null ? Integer.parseInt(System.getProperty("slowDownExecution")) : 0;
        }
        return slowDownExecutionTime;
    }

    private static String eDriverVersion;

    public static String getDriverVersion() {
        if (eDriverVersion == null) {
            eDriverVersion = System.getProperty("eDriverVersion") != null ? System.getProperty("eDriverVersion") != "default" ? System.getProperty("eDriverVersion") : getDeviceVersion() : getDeviceVersion();
        }
        return eDriverVersion;
    }

    private static String scenarioTag;

    public static String getScenarioTag() {
        if (scenarioTag == null) {
            if (System.getProperty("tags") != null) scenarioTag = System.getProperty("tags");
        }
        return scenarioTag;
    }
}
