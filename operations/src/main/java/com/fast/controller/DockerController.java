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

import com.fast.register.EnvironmentFetcher;
import com.fast.register.i;
import com.fast.report.LogLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class DockerController {
    public static void startDocker() {
        if (!isAlive()) {
            i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, "Trying to start Docker container " + getContainerId());
            runCommand("docker start " + getContainerId());

            long startTime = System.currentTimeMillis() / 1000;
            while (!isAlive() && (startTime + 15 > System.currentTimeMillis() / 1000)) {
                i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, "Waiting for Docker container " + getContainerId() + " to start");
            }
        } else {
            i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, "Docker container " + getContainerId() + " is already started");
        }
    }

    public static void stopDocker() {
        if (isAlive()) {
            i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, "Trying to stop Docker container " + getContainerId());
            runCommand("docker stop " + getContainerId());

            long startTime = System.currentTimeMillis() / 1000;
            while (isAlive() && (startTime + 15 > System.currentTimeMillis() / 1000)) {
                i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, "Waiting for Docker container " + getContainerId() + " to stop");
            }
        } else {
            i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, "Docker container " + getContainerId() + " is already stopped");
        }
    }

    public static boolean isAlive() {
        return readCommandResult(runCommand("docker inspect -f {{.State.Running}} " + getContainerId()));
    }

    private static Process runCommand(String command) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    private static boolean readCommandResult(Process proc) {
        String s;
        boolean result = false;
        try {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
                    result = Boolean.parseBoolean(s);
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, s);
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getContainerId() {
        return "selenium-" + EnvironmentFetcher.getDevice().toLowerCase();
    }
}
