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
package com.testblaze.report;


import com.testblaze.controller.ScenarioController;
import com.testblaze.misclib.ConsoleFormatter;

import static com.testblaze.misclib.ConsoleFormatter.ICON.NO_ICON;

public final class ReportController {
    private String mostRecentLog;


    /**
     * Calls {@link #write(LogLevel, ConsoleFormatter.COLOR, ConsoleFormatter.ICON, String) write(LogLevel, COLOR, ICON, log)}
     * with the specified LogLevel and reportLog.
     * <br> COLOR is defaulted to not be changed, and ICON is defaulted to NO_ICON
     *
     * @param logLevel  The LogLevel of the report message
     * @param reportLog The message that should be printed to the report
     */
    public void write(LogLevel logLevel, String reportLog) {
        write(logLevel, NO_ICON, reportLog);
    }

    /**
     * Calls {@link #write(LogLevel, ConsoleFormatter.COLOR, ConsoleFormatter.ICON, String) write(LogLevel, COLOR, ICON, log)}
     * with the specified LogLevel, icon and reportLog.
     * <br> COLOR is defaulted to not be changed.
     *
     * @param logLevel  The LogLevel of the report message
     * @param reportLog The message that should be printed to the report
     */
    public void write(LogLevel logLevel, ConsoleFormatter.ICON icon, String reportLog) {
        write(logLevel, null, icon, reportLog);
    }

    /**
     * Writes an empty line to the report.
     */
    public void newLine() {
        write(LogLevel.EMPTY_LABEL, null, NO_ICON, "");
    }

    /**
     * Writes a log to the report, prefaced by a given LogLevel and ICON
     *
     * @param logLevel  The LogLevel of the written report
     * @param color     The color of icon which prefaces the log
     * @param icon      The icon with which to preface the log
     * @param reportLog The message what should be written to the log
     */
    public void write(LogLevel logLevel, ConsoleFormatter.COLOR color, ConsoleFormatter.ICON icon, String reportLog) {
        if (!isDuplicateLog(reportLog)) {
            if (!"false".equalsIgnoreCase(System.getProperty("printConsoleLogs"))) {
                printConsoleLog(logLevel, color, icon, reportLog);
            }
            printHtmlLog(logLevel, color, icon, reportLog);
        }
    }


    /**
     * Writes a given log to the console.
     */
    private void printConsoleLog(LogLevel logLevel, ConsoleFormatter.COLOR color, ConsoleFormatter.ICON icon, String reportLog) {
        String toConsole = logLevel.getLog();
        if (icon != NO_ICON) {
            toConsole += (color != null ? ConsoleFormatter.setTextColor(color, icon.toString()) : icon.toString()) + "  ";
        }
        toConsole += reportLog;
        System.out.println(toConsole);
    }

    /**
     * Writes a given log to the HTML report.
     * <br>It performs the following formatting:<br>
     * <ul>
     *     <li>Wraps the log in a "pre" tag, to new lines and spaces are preserved</li>
     *     <li>Prints the log with a hanging indent, for easier line distinction</li>
     *     <li>Prints the LogLevel and Icon with a minimum width, so all lines are aligned correctly</li>
     *     <li>Converts all command-line format specifiers into HTML tags</li>
     * </ul>
     */
    private void printHtmlLog(LogLevel logLevel, ConsoleFormatter.COLOR color, ConsoleFormatter.ICON icon, String reportLog) {
        String toReport = "<pre style='display:inline-block;max-width:100%;padding-left:30px;text-indent:-30px;font-size:14px'>";

        toReport += "<span style='min-width:126px;float:left;'>" + logLevel.getLog() + "</span>";

        if (icon != NO_ICON) {
            toReport += "<span style='min-width:42px;float:left;'>";
            toReport += color != null ? ConsoleFormatter.setTextColor(color, icon.toString()) : icon.toString();
            toReport += "</span>";
        }

        toReport += reportLog + "</pre>";

        ScenarioController.getScenario().write(convertConsoleColorToHtmlTags(toReport));
    }

    /**
     * Checks if a String is the same as the previous String that was passed to the method
     *
     * @param reportLog The String to check
     * @return true if the previous String passed to the method is equal to the current String, false otherwise.
     */
    private boolean isDuplicateLog(String reportLog) {
        if (mostRecentLog == null) {
            this.mostRecentLog = reportLog;
            return false;
        } else if (reportLog.equalsIgnoreCase(mostRecentLog)) {
            return true;
        } else {
            this.mostRecentLog = reportLog;
            return false;
        }
    }

    /**
     * Parses a string and converts all console format specifiers ("[40m", etc.) into HTML tags.
     *
     * @param fullLog
     * @return
     */
    private String convertConsoleColorToHtmlTags(String fullLog) {
        return fullLog
                .replaceAll("\\u001B\\[30m", "<span style='color:black'>").replaceAll("\\u001B\\[40m", "<span style='background-color:black'>")
                .replaceAll("\\u001B\\[31m", "<span style='color:maroon'>").replaceAll("\\u001B\\[41m", "<span style='background-color:maroon'>")
                .replaceAll("\\u001B\\[32m", "<span style='color:green'>").replaceAll("\\u001B\\[42m", "<span style='background-color:green'>")
                .replaceAll("\\u001B\\[33m", "<span style='color:olive'>").replaceAll("\\u001B\\[43m", "<span style='background-color:olive'>")
                .replaceAll("\\u001B\\[34m", "<span style='color:navy'>").replaceAll("\\u001B\\[44m", "<span style='background-color:navy'>")
                .replaceAll("\\u001B\\[35m", "<span style='color:purple'>").replaceAll("\\u001B\\[45m", "<span style='background-color:purple'>")
                .replaceAll("\\u001B\\[36m", "<span style='color:teal'>").replaceAll("\\u001B\\[46m", "<span style='background-color:teal'>")
                .replaceAll("\\u001B\\[37m", "<span style='color:silver'>").replaceAll("\\u001B\\[47m", "<span style='background-color:silver'>")
                .replaceAll("\\u001B\\[90m", "<span style='color:gray'>").replaceAll("\\u001B\\[100m", "<span style='background-color:gray'>")
                .replaceAll("\\u001B\\[91m", "<span style='color:red'>").replaceAll("\\u001B\\[101m", "<span style='background-color:red'>")
                .replaceAll("\\u001B\\[92m", "<span style='color:lime'>").replaceAll("\\u001B\\[102m", "<span style='background-color:lime'>")
                .replaceAll("\\u001B\\[93m", "<span style='color:yellow'>").replaceAll("\\u001B\\[103m", "<span style='background-color:yellow'>")
                .replaceAll("\\u001B\\[94m", "<span style='color:blue'>").replaceAll("\\u001B\\[104m", "<span style='background-color:blue'>")
                .replaceAll("\\u001B\\[95m", "<span style='color:fuchsia'>").replaceAll("\\u001B\\[105m", "<span style='background-color:fuchsia'>")
                .replaceAll("\\u001B\\[96m", "<span style='color:aqua'>").replaceAll("\\u001B\\[106m", "<span style='background-color:aqua'>")
                .replaceAll("\\u001B\\[97m", "<span style='color:white'>").replaceAll("\\u001B\\[107m", "<span style='background-color:white'>")
                .replaceAll("\\033\\[0;1m", "<span style='font-weight:bold'>").replaceAll("\\033\\[0;0m", "</span>")
                .replaceAll("\\u001B\\[0m", "</span>");
    }
}
