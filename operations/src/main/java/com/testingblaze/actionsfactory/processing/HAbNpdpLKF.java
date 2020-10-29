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
package com.testingblaze.actionsfactory.processing;

import com.testingblaze.actionsfactory.abstracts.ElementProcessing;
import com.testingblaze.actionsfactory.abstracts.PageLoadProcessing;
import com.testingblaze.actionsfactory.api.HGJGcYGHQk;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.actionsfactory.elementfunctions.Waits;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.controller.TestingBlazeGlobal;
import com.testingblaze.exception.TestingBlazeRunTimeException;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.EnvironmentFactory;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HAbNpdpLKF implements ElementProcessing {

    private final WebDriver driver;
    private final JavaScript javaScript;
    private int magicWaitRetry = 0;
    private final HGJGcYGHQk iframeAnalyzer;
    private static By processingHoldOnScreen = null;
    private static Boolean turnOnProcessingHoldOnScreen = null;

    public HAbNpdpLKF() {
        this.javaScript = InstanceRecording.getInstance(JavaScript.class);
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
        iframeAnalyzer = InstanceRecording.getInstance(HGJGcYGHQk.class);
    }

    @Override
    public WebElement forSingleElement(By locator) {
        I.amPerforming().switchTo().acceptAlert();
        PageLoadProcessing.documentLoad.status("for DOM ");
        PageLoadProcessing.documentLoad.status("for script loading ");
        WebElement element = elementWaitProcessing(locator);
        TestingBlazeGlobal.setVariable("locatorInProgress", locator);
        if (!isViewPort(element)) {
            javaScript.scrollElementToPageDetailCenter(element);
        }
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Element Processing Ends");
        CompletableFuture.supplyAsync(() -> {
            try {
                if (TestingBlazeGlobal.getVariable("highlightElements") != null && ((String) TestingBlazeGlobal.getVariable("highlightElements")).equalsIgnoreCase("off")) {
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
        PageLoadProcessing.documentLoad.status("for DOM ");
        PageLoadProcessing.documentLoad.status("for script loading ");
        List<WebElement> listOfElements = listOfElementsWaitProcessing(locator);
        TestingBlazeGlobal.setVariable("locatorInProgress", "ignore");
        javaScript.scrollElementToPageDetailCenter(locator);
        if (listOfElements != null) isViewPort(listOfElements.get(0));
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "List of Elements Processing Ends");
        if (listOfElements == null)
            return new ArrayList<>();
        return listOfElements;
    }

    @Override
    public WebElement forNestedElement(WebElement element, By locator) {
        I.amPerforming().switchTo().acceptAlert();
        PageLoadProcessing.documentLoad.status("for DOM ");
        PageLoadProcessing.documentLoad.status("for script loading ");
        WebElement finalElement = elementWaitProcessing(element.findElement(locator));
        TestingBlazeGlobal.setVariable("locatorInProgress", "ignore");
        if (!isViewPort(element)) {
            javaScript.scrollElementToPageDetailCenter(element);
        }
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Element Processing Ends");
        CompletableFuture.supplyAsync(() -> {
            try {
                if (TestingBlazeGlobal.getVariable("highlightElements") != null && ((String) TestingBlazeGlobal.getVariable("highlightElements")).equalsIgnoreCase("off")) {
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
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("List of Elements Presence Check Completed in %.1f seconds", reportEndTime));
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
                            if (ele.isDisplayed()) {
                                element = ele;
                                isElementDrawnValidated = true;
                                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, "Element is Displayed & Enabled on page");
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
                    } catch (WebDriverException noSuchWindowAndTypeError) {
                        if (magicWaitRetry == 0) {
                            magicWaitRetry++;
                            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "No context is available, so re-trying one more time");
                            I.amPerforming().waitFor().makeThreadSleep(4000);
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
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Element does not exist in DOM");
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

                    if (element.isDisplayed()) {
                        isElementDrawnValidated = true;
                        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, "Element is Displayed & Enabled on page");
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
        javaScript.scrollElementToPageDetailCenter(element);
        magicWaitRetry = 0;
        double reportEndTime = (System.currentTimeMillis() / 1000.0) - reportStartTime;
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("Element Presence/Creation Completed in %.1f seconds", reportEndTime));
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
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, "Element Creation on UI completed");
                break;
            }
        }
    }

    private void countMatchingNodesOnPage(By locator) {
        int nodes = driver.findElements(locator).size();
        if (nodes != 1) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "Total element matching nodes in DOM --> " + nodes);
        } else {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Total element matching nodes in DOM --> " + nodes);
        }
    }

    private List<WebElement> getElementsForMagicWait(By locator) {
        try {
            if (driver.findElements(locator).size() > 0) {
                projectProcessingWrapper();
                return driver.findElements(locator);
            } else if (driver.findElements(locator).size() == 0) {
                iframeAnalyzer.evaluatePossibleIFrameToSwitch();
                if (HGJGcYGHQk.isFrameSwitchStatusSuccess) {
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
                    HGJGcYGHQk.isFrameSwitchStatusSuccess = false;
                }
                projectProcessingWrapper();
            }

        } catch (WebDriverException noSuchWindowAndTypeError) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "No initial context is available to switch, so re-trying one more time");
            I.amPerforming().waitFor().makeThreadSleep(5000);
            InstanceRecording.getInstance(DeviceBucket.class).getDriver().switchTo().defaultContent();
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Default Context Enabled");
        } catch (StackOverflowError stackOverflowError) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "Unable to handle frame switch");
            throw new TestingBlazeRunTimeException("Unable to handle frame switch");
        }
        return driver.findElements(locator);
    }

    private void projectProcessingWrapper() {
        if (turnOnProcessingHoldOnScreen == null && processingHoldOnScreen == null) {
            processingHoldOnScreen = (By) TestingBlazeGlobal.getVariable("processingHoldOnScreen");
            turnOnProcessingHoldOnScreen = processingHoldOnScreen != null;
        }
        if (driver.findElements(processingHoldOnScreen).size() > 0) {
            I.amPerforming().waitFor().makeThreadSleep(500);
        } else return;
        try {
            if (turnOnProcessingHoldOnScreen && (driver.findElements(processingHoldOnScreen).get(0).getRect().getDimension().getWidth() > 0 || driver.findElement(processingHoldOnScreen).isEnabled())) {
                long startTime = System.currentTimeMillis() / 1000;
                I.amPerforming().waitFor().disappearForProcessingONLY(processingHoldOnScreen, 120);
                I.amPerforming().waitFor().makeThreadSleep(400);
                long endTime = (System.currentTimeMillis() / 1000) - startTime;
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("Waited for hold on screen to fade away for %s seconds", endTime));
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

    private Boolean isViewPort(WebElement element) {
        Boolean status = false;
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
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Element ViewPort confirmed");
                status = true;
            } else {
                if (isViewPortCounter < 5) {
                    isViewPortCounter++;
                    isViewPort((element));
                }
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT, "Element ViewPort not confirmed");
                status = false;
            }
        } catch (Exception e) {
            //Only to handle unexpected error of JS
        }
        return status;
    }

}
