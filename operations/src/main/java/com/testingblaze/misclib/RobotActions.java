/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Framework [BDD] .
 *
 * Testing Blaze Automation Framework is licensed under the Apache License, Version
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
package com.testingblaze.misclib;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category simulates hardware actions
 */

public final class RobotActions {
    Robot robot;

    public RobotActions() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Simulates hardware mouse actions according to required x-axis and y-axis
     */
    public void mouseClick(int x, int y) throws AWTException {
        int mask = InputEvent.BUTTON1_DOWN_MASK;
        robot.mouseMove(x, y);
        robot.mousePress(mask);
        robot.mouseRelease(mask);
    }

    /**
     * Simulates key board Esc
     */
    public void escape() throws AWTException {
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
    }

    /**
     * Simulates key board Enter
     */
    public void enter() throws AWTException {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    /**
     * Simulates key board Control key press
     */
    public void ctrl_Press() throws AWTException {
        robot.keyPress(KeyEvent.VK_CONTROL);
    }

    /**
     * Simulates key board Control key release
     */
    public void ctrl_Release() throws AWTException {
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    /**
     * Simulates key board Control key release
     */
    public void ctrl_Save() throws AWTException {
        ctrl_Press();
        robot.keyPress(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_S);
        ctrl_Release();
    }

    /**
     * Simulates key board Control key release
     */
    public void copy() throws AWTException {
        ctrl_Press();
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        ctrl_Release();
    }

    /**
     * Simulates key board Control key rel
     */
    public void paste() throws AWTException {
        ctrl_Press();
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        ctrl_Release();
    }

    public Robot getRobot() {
        return robot;
    }
}
