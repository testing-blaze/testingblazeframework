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
package com.testingblaze.actionsfactory.processing;

import com.testingblaze.actionsfactory.abstracts.ElementProcessing;
import com.testingblaze.actionsfactory.api.IframeAnalyzer;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.actionsfactory.elementfunctions.Waits;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.controller.TestBlazeGlobal;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.EnvironmentFactory;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.testingblaze.misclib.ConsoleFormatter.COLOR.PURPLE;
import static com.testingblaze.misclib.ConsoleFormatter.ICON.*;

public class ExecuteElementProcessing implements ElementProcessing {

    private WebDriver driver;
    private JavaScript javaScript;
    private int magicWaitRetry = 0;
    private IframeAnalyzer iframeAnalyzer;
    private static By processingHoldOnScreen = null;
    private static Boolean turnOnProcessingHoldOnScreen = null;

    public ExecuteElementProcessing() {
        this.javaScript = InstanceRecording.getInstance(JavaScript.class);
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
        iframeAnalyzer = InstanceRecording.getInstance(IframeAnalyzer.class);
    }

    @Override
    public WebElement forSingleElement(By locator) {
        I.amPerforming().switchTo().acceptAlert();
        WebElement element = elementWaitProcessing(locator);
        javaScript.scrollElementToPageDetailCenter(element);
        isViewPort(element);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, CHECKERED_FLAG, "Element Processing Ends");
        CompletableFuture.supplyAsync(() -> {
            try {
                if (TestBlazeGlobal.getVariable("highlightElements") != null && ((String) TestBlazeGlobal.getVariable("highlightElements")).equalsIgnoreCase("off")) {
                    TestBlazeGlobal.deleteRecord("highlightElements");
                    return element;
                } else {
                    InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("arguments[0].setAttribute('style', 'background-color: #e6ffff; border: 2px solid black;');", element);
                }

            } catch (Exception e) {

            }
            return true;
        });
        return element;
    }

    @Override
    public List<WebElement> forListOfElements(By locator) {
        I.amPerforming().switchTo().acceptAlert();
        List<WebElement> listOfElements = listOfElementsWaitProcessing(locator);
        javaScript.scrollElementToPageDetailCenter(locator);
        if (listOfElements != null) isViewPort(listOfElements.get(0));
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, CHECKERED_FLAG, "List of Elements Processing Ends");
        if (listOfElements == null)
            return new ArrayList<>();
        return listOfElements;
    }

    @Override
    public WebElement forNestedElement(WebElement element, By locator) {
        I.amPerforming().switchTo().acceptAlert();
        WebElement finalElement = elementWaitProcessing(element.findElement(locator));
        javaScript.scrollElementToPageDetailCenter(finalElement);
        isViewPort(element);
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, CHECKERED_FLAG, "Element Processing Ends");
        CompletableFuture.supplyAsync(() -> {
            try {
                if (TestBlazeGlobal.getVariable("highlightElements") != null && ((String) TestBlazeGlobal.getVariable("highlightElements")).equalsIgnoreCase("off")) {
                    TestBlazeGlobal.deleteRecord("highlightElements");
                    return element;
                } else {
                    InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("arguments[0].setAttribute('style', 'background-color: #e6ffff; border: 2px solid black;');", element);
                }

            } catch (Exception e) {

            }
            return true;
        });
        return finalElement;
    }

    private <T> List<WebElement> listOfElementsWaitProcessing(T locatorOrElement) {
        double reportStartTime = (System.currentTimeMillis() / 1000.0);
        List<WebElement> listOfElements = null;
        long elementWaitTime = I.amPerforming().waitFor().getWaitTime();
        while (elementWaitTime > I.amPerforming().waitFor().getCurrentTimeInSecs() && listOfElements == null) {
            if (getElementsForMagicWait((By) locatorOrElement).size() > 0) {
                listOfElements = getElementsForMagicWait((By) locatorOrElement);
                break;
            }
        }
        double reportEndTime = (System.currentTimeMillis() / 1000.0) - reportStartTime;
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, THUMBS_UP, String.format("List of Elements Presence Check Completed in %.1f seconds", reportEndTime));
        return listOfElements;
    }

    /**
     * A smart wait which will check for page load status , handles matching nodes and try to return valid node, is element is on page , stale rrtrty
     *
     * @param locatorOrElement : it can receive any type argument but is designed to handle By and WebElement types only
     * @return refined element - WebElement
     * @alert Handles matching nodes and stale retry for By type only
     * @author nauman.shahid
     */
    private <T> WebElement elementWaitProcessing(T locatorOrElement) {
        double reportStartTime = (System.currentTimeMillis() / 1000.0);
        boolean displayedFlag = true;
        boolean isElementDrawnValidated = false;

        WebElement element = null;
        if (locatorOrElement instanceof By) {
            long elementWaitTime = I.amPerforming().waitFor().getWaitTime();
            while (elementWaitTime > I.amPerforming().waitFor().getCurrentTimeInSecs() && element == null && displayedFlag) {
                for (WebElement ele : getElementsForMagicWait((By) locatorOrElement)) {
                    try {
                        if (isElementDrawn(ele)) {
                            javaScript.scrollpageToSpecificElement(ele);
                            if (ele.isDisplayed()) {
                                element = ele;
                                isElementDrawnValidated = true;
                                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, THUMBS_UP, "Element is Displayed & Enabled on page");
                            }
                            break;
                        }
                    } catch (StaleElementReferenceException e) {
                        if (magicWaitRetry == 0) {
                            magicWaitRetry++;
                            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, THUMBS_DOWN, "Element is stale so re-trying one more time");
                            elementWaitProcessing(locatorOrElement);
                        } else {
                            displayedFlag = false;
                        }
                    }
                }
            }
            displayedFlag = true;
            countMatchingNodesOnPage((By) locatorOrElement);
            if (element == null) {
                try {
                    element = getElementForMagicWait((By) locatorOrElement);
                } catch (Exception e) {
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, THUMBS_DOWN, "Element does not exist in DOM");
                    throw e;
                }
            }
        } else if (locatorOrElement instanceof WebElement) {
            element = (WebElement) locatorOrElement;
        }

        long elementVisibilityWaitTime = I.amPerforming().waitFor().getWaitTime();

        while ((elementVisibilityWaitTime > I.amPerforming().waitFor().getCurrentTimeInSecs() && displayedFlag) && !isElementDrawnValidated) {
            try {
                if (isElementDrawn(element)) {
                    javaScript.scrollpageToSpecificElement(element);
                    if (element.isDisplayed()) {
                        isElementDrawnValidated = true;
                        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, THUMBS_UP, "Element is Displayed & Enabled on page");
                    }
                    break;
                }
            } catch (StaleElementReferenceException e) {
                if (magicWaitRetry == 0) {
                    magicWaitRetry++;
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "Element is stale so re-trying one more time");
                    elementWaitProcessing(locatorOrElement);
                } else {
                    displayedFlag = false;
                }
            }
        }

        magicWaitRetry = 0;
        double reportEndTime = (System.currentTimeMillis() / 1000.0) - reportStartTime;
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, THUMBS_UP, String.format("Element Presence/Creation Completed in %.1f seconds", reportEndTime));
        return element;
    }

    private boolean isElementDrawn(WebElement element) {
        boolean flag = false;
        try {
            if (element.getRect().getWidth() > 2 && element.getRect().getHeight() > 2) {
                completeElementCreationOnUi(element);
                if (element.isEnabled()) {
                    flag = true;
                }
            }
        } catch (StaleElementReferenceException stale) {
            throw stale;
        } catch (Exception e) {
            //Do nothing
        }
        return flag;
    }

    private void completeElementCreationOnUi(WebElement element) {
        double waitTime = I.amPerforming().waitFor().getCurrentTimeInSecs() + (Waits.STANDARD_WAIT_TIME * 0.5);
        int iSize = element.getRect().getHeight() + element.getRect().getWidth();
        while (I.amPerforming().waitFor().getCurrentTimeInSecs() < waitTime) {
            int newSize = element.getRect().getHeight() + element.getRect().getWidth();
            if (newSize > iSize) iSize = newSize;
            else {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, TICK, "Element Creation on UI completed");
                break;
            }
        }
    }

    private void countMatchingNodesOnPage(By locator) {
        int nodes = driver.findElements(locator).size();
        if (nodes != 1) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, THUMBS_DOWN, "Total element matching nodes in DOM --> " + nodes);
        } else {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, THUMBS_UP, "Total element matching nodes in DOM --> " + nodes);
        }
    }

    private List<WebElement> getElementsForMagicWait(By locator) {
        if (driver.findElements(locator).size() > 0) {
            projectProcessingWrapper();
            return driver.findElements(locator);
        } else if (driver.findElements(locator).size() == 0) {
            iframeAnalyzer.evaluatePossibleIFrameToSwitch();
            if (IframeAnalyzer.setFlagForFrameSwitch) {
                I.amPerforming().waitFor().makeThreadSleep(200);
                if (EnvironmentFactory.getSlowDownExecutionTime() > 0) {
                    I.amPerforming().waitFor().makeThreadSleep(1000 * EnvironmentFactory.getSlowDownExecutionTime());
                }
                projectProcessingWrapper();
                try {
                    completeElementCreationOnUi(driver.findElement(By.xpath("//body")));
                } catch (Exception e) {
                    // Handles unexpected exception for //body
                }
                IframeAnalyzer.setFlagForFrameSwitch = false;
            }
            projectProcessingWrapper();
        }
        return driver.findElements(locator);
    }

    private void projectProcessingWrapper() {
        if (turnOnProcessingHoldOnScreen == null && processingHoldOnScreen == null) {
            processingHoldOnScreen = (By) TestBlazeGlobal.getVariable("processingHoldOnScreen");
            turnOnProcessingHoldOnScreen = processingHoldOnScreen != null;
        }
        if (driver.findElements(processingHoldOnScreen).size() > 0)
            I.amPerforming().waitFor().makeThreadSleep(500);
        else return;
        try {
            if (turnOnProcessingHoldOnScreen && (driver.findElements(processingHoldOnScreen).get(0).getRect().getDimension().getWidth() > 0 || driver.findElement(processingHoldOnScreen).isEnabled())) {
                long startTime = System.currentTimeMillis() / 1000;
                I.amPerforming().waitFor().disappearForProcessingONLY(processingHoldOnScreen, 120);
                I.amPerforming().waitFor().makeThreadSleep(400);
                long endTime = (System.currentTimeMillis() / 1000) - startTime;
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, PURPLE, HOURGLASS, String.format("Waited for hold on screen to fade away for %s seconds", endTime));
            }
        } catch (Exception e) {
            /* Ignore Exception */
        }
    }

    private WebElement getElementForMagicWait(By locator) {
        return driver.findElement(locator);
    }

    /**
     * confirms valid view port
     */
    private int isViewPortCounter = 0;

    private void isViewPort(WebElement element) {
        double top = 0.0, left = 0.0, right = 0.0, bottom = 0.0;
        try {
            var windowWidthOpt1 = InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("return window.innerWidth");
            var windowWidthOpt2 = InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("return document.documentElement.clientWidth");
            var windowHeightOpt1 = InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("return window.innerHeight");
            var windowHeightOpt2 = InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("return document.documentElement.clientHeight");
            var fourBounds = List.of(InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("return arguments[0].getBoundingClientRect()", element).toString().split(",")).
                    stream().filter(bounds -> bounds.contains("top") || bounds.contains("left") || bounds.contains("right") || bounds.contains("bottom")).collect(Collectors.toList());
            for (String fourBound : fourBounds) {
                if (StringUtils.containsIgnoreCase(fourBound, "top")) top = Double.valueOf(fourBound.split("=")[1]);
                else if (StringUtils.containsIgnoreCase(fourBound, "left"))
                    left = Double.valueOf(fourBound.split("=")[1]);
                else if (StringUtils.containsIgnoreCase(fourBound, "right"))
                    right = Double.valueOf(fourBound.split("=")[1]);
                else if (StringUtils.containsIgnoreCase(fourBound, "bottom"))
                    left = Double.valueOf(fourBound.split("=")[1]);
            }
            if (
                    top >= 0 &&
                            left >= 0 &&
                            (right <= Double.parseDouble(windowWidthOpt1.toString()) || right <= Double.parseDouble(windowWidthOpt2.toString()) &&
                                    (bottom <= Double.parseDouble(windowHeightOpt1.toString()) || bottom <= Double.parseDouble(windowHeightOpt2.toString())))
            ) {
                isViewPortCounter = 0;
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, OVERLAPPING_FRAME, "Element ViewPort confirmed");
            } else {
                if (isViewPortCounter < 5) {
                    isViewPortCounter++;
                    isViewPort((element));
                }
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, THUMBS_DOWN, "Element ViewPort not confirmed");
            }
        } catch (Exception e) {
            //Only to handle unexpected error of JS
        }
    }

}
