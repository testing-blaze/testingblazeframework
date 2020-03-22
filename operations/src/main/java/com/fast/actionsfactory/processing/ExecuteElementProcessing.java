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

import com.fast.actionsfactory.abstracts.ElementProcessing;
import com.fast.actionsfactory.api.IframeAnalyzer;
import com.fast.actionsfactory.elementfunctions.JavaScript;
import com.fast.actionsfactory.elementfunctions.Waits;
import com.fast.controller.DeviceBucket;
import com.fast.controller.FastGlobal;
import com.fast.objects.InstanceRecording;
import com.fast.register.EnvironmentFetcher;
import com.fast.register.i;
import com.fast.report.LogLevel;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.fast.misclib.ConsoleFormatter.COLOR.PURPLE;
import static com.fast.misclib.ConsoleFormatter.ICON.*;

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
        i.perform().switchTo().acceptAlert();
        WebElement element = elementWaitProcessing(locator);
        javaScript.scrollElementToPageDetailCenter(element);
        isViewPort(element);
        i.perform().report().write(LogLevel.FAST_INFO, CHECKERED_FLAG, "Element Processing Ends");
        CompletableFuture.supplyAsync(() -> {
            try {
                if (FastGlobal.getVariable("highlightElements") != null && ((String) FastGlobal.getVariable("highlightElements")).equalsIgnoreCase("off")) {
                    FastGlobal.deleteRecord("highlightElements");
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
        i.perform().switchTo().acceptAlert();
        List<WebElement> listOfElements = listOfElementsWaitProcessing(locator);
        javaScript.scrollElementToPageDetailCenter(locator);
        if (listOfElements != null) isViewPort(listOfElements.get(0));
        i.perform().report().write(LogLevel.FAST_INFO, CHECKERED_FLAG, "List of Elements Processing Ends");
        if (listOfElements == null)
            return new ArrayList<>();
        return listOfElements;
    }

    @Override
    public WebElement forNestedElement(WebElement element, By locator) {
        i.perform().switchTo().acceptAlert();
        WebElement finalElement = elementWaitProcessing(element.findElement(locator));
        javaScript.scrollElementToPageDetailCenter(finalElement);
        isViewPort(element);
        i.perform().report().write(LogLevel.FAST_INFO, CHECKERED_FLAG, "Element Processing Ends");
        CompletableFuture.supplyAsync(() -> {
            try {
                if (FastGlobal.getVariable("highlightElements") != null && ((String) FastGlobal.getVariable("highlightElements")).equalsIgnoreCase("off")) {
                    FastGlobal.deleteRecord("highlightElements");
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
        long elementWaitTime = i.perform().WaitFor().getWaitTime();
        while (elementWaitTime > i.perform().WaitFor().getCurrentTimeInSecs() && listOfElements == null) {
            if (getElementsForMagicWait((By) locatorOrElement).size() > 0) {
                listOfElements = getElementsForMagicWait((By) locatorOrElement);
                break;
            }
        }
        double reportEndTime = (System.currentTimeMillis() / 1000.0) - reportStartTime;
        i.perform().report().write(LogLevel.FAST_INFO, THUMBS_UP, String.format("List of Elements Presence Check Completed in %.1f seconds", reportEndTime));
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
            long elementWaitTime = i.perform().WaitFor().getWaitTime();
            while (elementWaitTime > i.perform().WaitFor().getCurrentTimeInSecs() && element == null && displayedFlag) {
                for (WebElement ele : getElementsForMagicWait((By) locatorOrElement)) {
                    try {
                        if (isElementDrawn(ele)) {
                            javaScript.scrollpageToSpecificElement(ele);
                            if (ele.isDisplayed()) {
                                element = ele;
                                isElementDrawnValidated = true;
                                i.perform().report().write(LogLevel.FAST_IMPORTANT, THUMBS_UP, "Element is Displayed & Enabled on page");
                            }
                            break;
                        }
                    } catch (StaleElementReferenceException e) {
                        if (magicWaitRetry == 0) {
                            magicWaitRetry++;
                            i.perform().report().write(LogLevel.FAST_CRITICAL, THUMBS_DOWN, "Element is stale so re-trying one more time");
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
                    i.perform().report().write(LogLevel.FAST_ERROR, THUMBS_DOWN, "Element does not exist in DOM");
                    throw e;
                }
            }
        } else if (locatorOrElement instanceof WebElement) {
            element = (WebElement) locatorOrElement;
        }

        long elementVisibilityWaitTime = i.perform().WaitFor().getWaitTime();

        while ((elementVisibilityWaitTime > i.perform().WaitFor().getCurrentTimeInSecs() && displayedFlag) && !isElementDrawnValidated) {
            try {
                if (isElementDrawn(element)) {
                    javaScript.scrollpageToSpecificElement(element);
                    if (element.isDisplayed()) {
                        isElementDrawnValidated = true;
                        i.perform().report().write(LogLevel.FAST_IMPORTANT, THUMBS_UP, "Element is Displayed & Enabled on page");
                    }
                    break;
                }
            } catch (StaleElementReferenceException e) {
                if (magicWaitRetry == 0) {
                    magicWaitRetry++;
                    i.perform().report().write(LogLevel.FAST_CRITICAL, "Element is stale so re-trying one more time");
                    elementWaitProcessing(locatorOrElement);
                } else {
                    displayedFlag = false;
                }
            }
        }

        magicWaitRetry = 0;
        double reportEndTime = (System.currentTimeMillis() / 1000.0) - reportStartTime;
        i.perform().report().write(LogLevel.FAST_INFO, THUMBS_UP, String.format("Element Presence/Creation Completed in %.1f seconds", reportEndTime));
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
        double waitTime = i.perform().WaitFor().getCurrentTimeInSecs() + (Waits.STANDARD_WAIT_TIME * 0.5);
        int iSize = element.getRect().getHeight() + element.getRect().getWidth();
        while (i.perform().WaitFor().getCurrentTimeInSecs() < waitTime) {
            int newSize = element.getRect().getHeight() + element.getRect().getWidth();
            if (newSize > iSize) iSize = newSize;
            else {
                i.perform().report().write(LogLevel.FAST_IMPORTANT, TICK, "Element Creation on UI completed");
                break;
            }
        }
    }

    private void countMatchingNodesOnPage(By locator) {
        int nodes = driver.findElements(locator).size();
        if (nodes != 1) {
            i.perform().report().write(LogLevel.FAST_CRITICAL, THUMBS_DOWN, "Total element matching nodes in DOM --> " + nodes);
        } else {
            i.perform().report().write(LogLevel.FAST_INFO, THUMBS_UP, "Total element matching nodes in DOM --> " + nodes);
        }
    }

    private List<WebElement> getElementsForMagicWait(By locator) {
        if (driver.findElements(locator).size() > 0) {
            projectProcessingWrapper();
            return driver.findElements(locator);
        } else if (driver.findElements(locator).size() == 0) {
            iframeAnalyzer.evaluatePossibleIFrameToSwitch();
            if (IframeAnalyzer.setFlagForFrameSwitch) {
                i.perform().WaitFor().makeThreadSleep(200);
                if (EnvironmentFetcher.getSlowDownExecutionTime() > 0) {
                    i.perform().WaitFor().makeThreadSleep(1000 * EnvironmentFetcher.getSlowDownExecutionTime());
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
            processingHoldOnScreen = (By) FastGlobal.getVariable("processingHoldOnScreen");
            turnOnProcessingHoldOnScreen = processingHoldOnScreen != null;
        }
        if (driver.findElements(processingHoldOnScreen).size() > 0)
            i.perform().WaitFor().makeThreadSleep(500);
        else return;
        try {
            if (turnOnProcessingHoldOnScreen && (driver.findElements(processingHoldOnScreen).get(0).getRect().getDimension().getWidth() > 0 || driver.findElement(processingHoldOnScreen).isEnabled())) {
                long startTime = System.currentTimeMillis() / 1000;
                i.perform().WaitFor().waitToDisappearForProcessing(processingHoldOnScreen, 120);
                i.perform().WaitFor().makeThreadSleep(400);
                long endTime = (System.currentTimeMillis() / 1000) - startTime;
                i.perform().report().write(LogLevel.FAST_INFO, PURPLE, HOURGLASS, String.format("Waited for hold on screen to fade away for %s seconds", endTime));
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
                i.perform().report().write(LogLevel.FAST_INFO, OVERLAPPING_FRAME, "Element ViewPort confirmed");
            } else {
                if (isViewPortCounter < 5) {
                    isViewPortCounter++;
                    isViewPort((element));
                }
                i.perform().report().write(LogLevel.FAST_IMPORTANT, THUMBS_DOWN, "Element ViewPort not confirmed");
            }
        } catch (Exception e) {
            //Only to handle unexpected error of JS
        }
    }

}
