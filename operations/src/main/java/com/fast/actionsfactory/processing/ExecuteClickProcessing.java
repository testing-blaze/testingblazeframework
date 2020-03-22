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
package com.fast.actionsfactory.processing;

import com.fast.actionsfactory.abstracts.ActionProcessing;
import com.fast.actionsfactory.type.Is;
import com.fast.controller.DeviceBucket;
import com.fast.objects.InstanceRecording;
import com.fast.register.i;
import com.fast.report.LogLevel;

import java.util.ArrayList;
import java.util.List;

import static com.fast.misclib.ConsoleFormatter.COLOR.GREEN;
import static com.fast.misclib.ConsoleFormatter.ICON.CYCLONE;
import static com.fast.misclib.ConsoleFormatter.setBoldText;
import static com.fast.misclib.ConsoleFormatter.setTextColor;

public class ExecuteClickProcessing {
    public final Is is = InstanceRecording.getInstance(Is.class);
    private final DeviceBucket device = InstanceRecording.getInstance(DeviceBucket.class);
    List<String> windowHandles = new ArrayList<>();

    // Change it to windows handler and use LocatorProcessing updated abstract

    public ActionProcessing preProcessingFast = () -> {
        windowHandles.clear();
        windowHandles.addAll(i.perform().switchTo().getWindowsHandlers());
    };

    public ActionProcessing postProcessingFast = () -> {
        //PageLoadProcessing.documentLoad.status("before action processing");
        List<String> newWindowHandles = i.perform().switchTo().getWindowsHandlers();
        if (newWindowHandles.size() != windowHandles.size()) {
            for (int i = 0; i < newWindowHandles.size(); i++) {
                if (!windowHandles.contains(newWindowHandles.get(i))) {
                    switchToWindowHandler(i);
                    com.fast.register.i.perform().report().write(LogLevel.FAST_INFO, GREEN, CYCLONE, setBoldText(setTextColor(GREEN, "Switched to a new Window ")));
                    break;
                }
            }
        }
    };

    /**
     * switch to window handler
     *
     * @param handlerNumber : Usually child number is "1" and parent number is "0". However,
     *                      method is dyanmic to handle any child number
     */
    private void switchToWindowHandler(int handlerNumber) {
        device.getDriver().switchTo().window(getWindowsHandlers().get(handlerNumber));
    }

    /**
     * Windows handlers
     */
    public ArrayList<String> getWindowsHandlers() {
        return new ArrayList<String>(device.getDriver().getWindowHandles());
    }
}
