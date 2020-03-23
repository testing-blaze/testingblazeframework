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
package com.fast.actionsfactory.abstracts;

import com.fast.actionsfactory.elementfunctions.JavaScript;
import com.fast.controller.DeviceBucket;
import com.fast.objects.InstanceRecording;
import com.fast.register.i;
import com.fast.report.LogLevel;
import org.openqa.selenium.JavascriptExecutor;

import static com.fast.misclib.ConsoleFormatter.COLOR.BRIGHT_BLUE;
import static com.fast.misclib.ConsoleFormatter.COLOR.BRIGHT_CYAN;
import static com.fast.misclib.ConsoleFormatter.ICON.WEB;
import static com.fast.misclib.ConsoleFormatter.setBoldText;


@FunctionalInterface
public interface PageLoadProcessing<T> {
    double pageLaodStandardWait=60;

    void status(T reportMessage);

    PageLoadProcessing<String> windowLoad = (reportMessage) -> {
        JavascriptExecutor js = ((JavascriptExecutor) InstanceRecording.getInstance(DeviceBucket.class).getDriver());
        String windowLoader="return window.performance.timing.loadEventEnd";
        double pageLoadWait = System.currentTimeMillis() / 1000 + pageLaodStandardWait;
        double reportStartTime = System.currentTimeMillis() / 1000.0;
        String pageLoadStatus= "No status retrieved";
        Object result = 0;
        try {
        while (pageLoadWait > System.currentTimeMillis() / 1000.0) {
            result = js.executeScript(windowLoader);
            if (result != null) {
                pageLoadStatus = "complete";
                break;
            }
        }} catch (Exception e) {

        }
        double reportEndTime = (System.currentTimeMillis() / 1000.0) - reportStartTime;
        i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, BRIGHT_BLUE, WEB, "Level 2 -> page Load status "+reportMessage+" is: " + setBoldText(pageLoadStatus)+" in "+(Math.round(reportEndTime)*10)/10.0+" seconds");
    };

    PageLoadProcessing<String> documentLoad = (reportMessage) -> {
        double pageLoadWait = System.currentTimeMillis() / 1000 + pageLaodStandardWait;
        double reportStartTime = System.currentTimeMillis() / 1000.0;
        String pageLoadStatus = "No status retrieved";
        try {
        while (pageLoadWait > System.currentTimeMillis() / 1000.0) {
            pageLoadStatus = InstanceRecording.getInstance(JavaScript.class).getPageLoadStatus();
            if ("complete".equalsIgnoreCase(pageLoadStatus)) {
                break;
            } else if("Failed to get any status".equalsIgnoreCase(pageLoadStatus)) {
                i.amPerforming().waitFor().makeThreadSleep(200);
                break;
            }
        } } catch (Exception e) {

        }
        double reportEndTime = (System.currentTimeMillis() / 1000.0) - reportStartTime;
        i.amPerforming().updatingReportWith().write(LogLevel.FAST_INFO, BRIGHT_CYAN, WEB, "Level 1 -> page Load status "+reportMessage+" is: " + setBoldText(pageLoadStatus)+" in "+(Math.round(reportEndTime)*10)/10.0+" seconds");
    };
}
