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
package com.testingblaze.actionsfactory.elementfunctions;

import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.NoSuchElementException;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Handles different click options
 */

public final class MouseActions {
    private Actions actions;

    public MouseActions(){
        actions = new Actions(InstanceRecording.getInstance(DeviceBucket.class).getDriver());
    }

    /**
     * slides any slider by a given x and y value
     *
     * @param sliderElement the slider which is to be moved
     * @param xOffset       the amount to move the slider in the horizontal direction
     * @param yOffset       the amount to move the slider in the vertical direction
     * @author nauman.shahid
     */
    public void moveSliderByOffset(WebElement sliderElement, int xOffset, int yOffset) {
        actions.clickAndHold(sliderElement).moveByOffset(xOffset, yOffset).release().build().perform();
    }

    /**
     * clicks on desired location based on dimension
     *
     * @author nauman.shahid
     */
    public void mouseClick(int x, int y) {
        actions.moveByOffset(x, y).click().build().perform();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Scrolled using mouse");

    }

    /**
     * clicks on desired location on web page using webElement
     *
     * @author nauman.shahid
     */
    public void mouseClick(WebElement element) {
        actions.click(element).build().perform();
    }

    /**
     * click and hold on desired location on web page
     *
     * @param element
     * @param holdTimeSeconds
     * @author nauman.shahid
     */
    public void mouseClickAndHold(WebElement element, int holdTimeSeconds) {
        actions.clickAndHold(element).pause(holdTimeSeconds * 1000).release().build().perform();
    }

    /**
     * Double clicks on desired location on web page
     *
     * @author nauman.shahid
     */
    public void mouseDoubleClick(WebElement element) {
        actions.doubleClick(element).build().perform();
    }

    /**
     * Drag and drop on desired location on web page
     *
     * @author nauman.shahid
     */
    public void mouseDragAndDrop(WebElement elementSource, WebElement elementTarget) {
        actions.dragAndDrop(elementSource, elementTarget).build().perform();
    }

    /**
     * Drag and drop on desired location on web page
     *
     * @author nauman.shahid
     */
    public void dragAndDrop(WebElement elementSource, WebElement elementTarget) {
        var sourceXaxis=elementSource.getRect().getX();
        var sourceYaxis=elementSource.getRect().getY();
        var targetXaxis=elementTarget.getRect().getX();
        var targetYaxis=elementTarget.getRect().getY();
        var xAxis=targetXaxis-sourceXaxis;
        var YAxis=targetYaxis-sourceYaxis;
        var drag=actions.clickAndHold(elementSource)
                .moveByOffset(10, 0)
                .moveByOffset(-10, 0)
                .moveByOffset(xAxis, YAxis)
                .release()
                .build();
        drag.perform();
    }

    public void dragAndDropInHtml5(String elementSourceCss,String elementTargetCss) {
        BufferedReader dragDropJSFile= new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/drag_drop_html5.js")));
        StringBuffer buffer = new StringBuffer();
        String java_script="";
        String text = null;
        while (true) {
            try {
                if (!((text = dragDropJSFile.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.append(text + " ");
        }
        java_script = buffer.toString();

        try {
            dragDropJSFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        java_script = java_script+"$('#"+elementSourceCss+"').simulate( '#" +elementTargetCss+ "');" ;
        InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript(java_script);
    }

    /**
     * Drag and drop on desired location on web page
     *
     * @author nauman.shahid
     */
    public void mouseDragAndDrop(WebElement elementSource, int xOffset,int yOffset) {
        actions.dragAndDropBy(elementSource,xOffset,yOffset).build().perform();
    }

    /**
     * Mouse right click on desired location on web page
     *
     * @author nauman.shahid
     */
    public void mouseRightClick(WebElement context) {
        actions.contextClick(context).build().perform();
    }

    /**
     * Scroll to desired location on web page
     *
     * @author nauman.shahid
     */
    public void scrollTo(WebElement elementScrollTo) {
        WebDriverWait wait = new WebDriverWait(InstanceRecording.getInstance(DeviceBucket.class).getDriver(), 10);
        while (wait.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.visibilityOf(elementScrollTo)) != null) {
            actions.keyDown(Keys.PAGE_DOWN).build().perform();
        }
        actions.keyUp(Keys.PAGE_DOWN).build().perform();
    }

    /**
     * move to desired location on web page
     *
     * @author nauman.shahid
     */
    public void moveMouseToSpecificLocation(WebElement elementScrollTo) {
        actions.moveToElement(elementScrollTo).build().perform();
    }

    /**
     * move element to desired location on web page
     *
     * @author nauman.shahid
     */
    public void moveMouseToTargetAndOffset(WebElement elementScrollTo, int xOffset, int yOffset) {
        actions.moveToElement(elementScrollTo, xOffset, yOffset).build().perform();
    }

}
