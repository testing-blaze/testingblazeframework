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
package com.testingblaze.report;

import static com.testingblaze.misclib.ConsoleFormatter.COLOR;
import static com.testingblaze.misclib.ConsoleFormatter.COLOR.*;
import static com.testingblaze.misclib.ConsoleFormatter.setTextColor;

public enum LogLevel {
    TEST_BLAZE_CRITICAL(YELLOW, "[Blaze:911]"),
    TEST_BLAZE_INFO(BLUE, "[Blaze:Info]"),
    TEST_BLAZE_IMPORTANT(CYAN, "[Blaze:Imp]"),
    TEST_BLAZE_ERROR(RED, "[Blaze:Err]"),
    BUSINESS_LAYER_INFO(GREEN, "[BuLayer:Info]"),
    BUSINESS_LAYER_ERROR(BRIGHT_YELLOW, "[BuLayer:Error]"),
    PROJECT_LAYER_INFO(CYAN, "[PLayer:Info]"),
    PROJECT_LAYER_ERROR(BRIGHT_WHITE, "[PLayer:Error]"),
    EMPTY_LABEL(BRIGHT_WHITE, "");

    private COLOR logColor;
    private String log;
    private static int longestLogLevel = 0;

    public String getLog() {
        return setTextColor(this.logColor, this.log + " ".repeat(getLongestLogLevel() - this.log.length() + 1));
    }

    LogLevel(COLOR logColor, String log) {
        this.logColor = logColor;
        this.log = log;
    }

    private int getLongestLogLevel() {
        if (longestLogLevel == 0) {
            for (LogLevel level : LogLevel.values()) {
                if (level.log.length() > longestLogLevel) {
                    longestLogLevel = level.log.length();
                }
            }
        }
        return longestLogLevel;
    }
}
