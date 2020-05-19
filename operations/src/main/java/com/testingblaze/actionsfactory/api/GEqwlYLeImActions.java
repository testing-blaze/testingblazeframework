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
package com.testingblaze.actionsfactory.api;

import com.testingblaze.actionsfactory.abstracts.Action;
import com.testingblaze.actionsfactory.elementfunctions.FindMyElements;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.actionsfactory.elementfunctions.Waits;
import com.testingblaze.actionsfactory.processing.wlGgOnuIbI;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.controller.TestingBlazeGlobal;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class GEqwlYLeImActions implements Action {
    private wlGgOnuIbI clickProcessingController;
    private static long timerLimit;
    private Waits wait;
    private FluentWait<WebDriver> localWait = null;

    public GEqwlYLeImActions() {
        if (clickProcessingController == null) clickProcessingController = new wlGgOnuIbI();
        if (wait == null) wait = InstanceRecording.getInstance(Waits.class);
        if (localWait == null) {
            localWait = new WebDriverWait(InstanceRecording.getInstance(DeviceBucket.class).getDriver(), Waits.STANDARD_WAIT_TIME).pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class, NullPointerException.class);
        }
    }

    @Override
    public void doIt(WebElement element) {
        clickProcessingController.preProcessingTestBlaze.perform();
        localWait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.click();
            I.amPerforming().switchTo().acceptAlert();
            CompletableFuture.supplyAsync(() -> {
                try {
                    InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("arguments[0].setAttribute('style', 'background-color: #ccffcc; border: 2px solid black;');", element);
                } catch (Exception e) {

                }
                return true;
            });
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Click action performed successfully");

        } catch (ElementClickInterceptedException interceptException) {
            retryExecutor(element, "click", "Intercept Exception Caught");
        } catch (ElementNotInteractableException interactableException) {
            retryExecutor(element, "click", "Not Interactable Exception Caught");
        } catch (WebDriverException notClickableOrNoFocusException) {
            if (notClickableOrNoFocusException.getMessage().contains("is not clickable at point") | notClickableOrNoFocusException.getMessage().contains("cannot focus element")) {
                retryExecutor(element, "click", "Element is not clickable or in focus");
            } else {
                throw notClickableOrNoFocusException;
            }
        }
        clickProcessingController.postProcessingTestBlaze.perform();
    }

    @Override
    public void doIt(WebElement element, String input) {
        localWait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            if (input.equalsIgnoreCase("--clear--")) {
                element.clear();
                I.amPerforming().switchTo().acceptAlert();
                CompletableFuture.supplyAsync(() -> {
                    try {
                        InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("arguments[0].setAttribute('style', 'background-color: #ccffcc; border: 2px solid black;');", element);
                    } catch (Exception e) {

                    }
                    return true;
                });
            } else {
                element.sendKeys(input);
                I.amPerforming().switchTo().acceptAlert();
                CompletableFuture.supplyAsync(() -> {
                    try {
                        InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("arguments[0].setAttribute('style', 'background-color: #ccffcc; border: 2px solid black;');", element);
                    } catch (Exception e) {

                    }
                    return true;
                });
            }
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Input action performed successfully");
        } catch (ElementClickInterceptedException interceptException) {
            retryExecutor(element, "input", "Intercept Exception Caught", input);
        } catch (ElementNotInteractableException interactableException) {
            retryExecutor(element, "input", "Not Interactable Exception Caught", input);
        } catch (WebDriverException notClickableOrNoFocusException) {
            if (notClickableOrNoFocusException.getMessage().contains("is not clickable at point") | notClickableOrNoFocusException.getMessage().contains("cannot focus element")) {
                retryExecutor(element, "input", "Element is not entering text or in focus", input);
            } else {
                throw notClickableOrNoFocusException;
            }
        }
    }

    /**
     * handle retry
     *
     * @param element
     * @param args    actionType,exception,inputText(for input only)
     */
    private void retryExecutor(WebElement element, String... args) {
        CompletableFuture.supplyAsync(() -> {
            try {
                InstanceRecording.getInstance(JavaScript.class).executeJSCommand().executeScript("arguments[0].setAttribute('style', 'background-color: red; border: 2px solid black;');", element);
            } catch (Exception e) {

            }
            return true;
        });
        timerLimit = wait.getWaitTime();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, args[1] + " : Retrying to " + args[0] + " for maximum " + Waits.STANDARD_WAIT_TIME + " seconds");
        while (timerLimit > System.currentTimeMillis() / 1000) {
            try {
                if (args[0].equalsIgnoreCase("click")) {
                    element.click();
                    I.amPerforming().switchTo().acceptAlert();
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, args[1] + " : Retrying is Successful ");
                    break;
                } else if (args[0].equalsIgnoreCase("input")) {
                    if (args[2].equalsIgnoreCase("--clear--")) {
                        element.clear();
                        I.amPerforming().switchTo().acceptAlert();
                    } else {
                        element.sendKeys(args[2]);
                        I.amPerforming().switchTo().acceptAlert();
                    }
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, args[1] + " : Retrying is Successful ");
                    break;
                }
            } catch (ElementClickInterceptedException interceptException) {
                if (timerLimit <= System.currentTimeMillis() / 1000) {
                    throw interceptException;
                }
            } catch (ElementNotInteractableException interactableException) {
                if (timerLimit <= System.currentTimeMillis() / 1000) {
                    throw interactableException;
                }
            } catch (StaleElementReferenceException staleException) {

                if (TestingBlazeGlobal.getVariable("locatorInProgress") instanceof By) {
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "Stale Element Exception : Retrying once again");
                    WebElement freshElement = InstanceRecording.getInstance(FindMyElements.class).getElement((By) TestingBlazeGlobal.getVariable("locatorInProgress"), true);
                    I.amPerforming().waitFor().makeThreadSleep(2000);
                    if (args[0].equalsIgnoreCase("click")) {
                        freshElement.click();
                        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, args[1] + " : Retrying is Successful ");
                        I.amPerforming().switchTo().acceptAlert();
                    } else if (args[0].equalsIgnoreCase("input")) {
                        if (args[2].equalsIgnoreCase("--clear--")) {
                            freshElement.clear();
                            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, args[1] + " : Retrying is Successful ");
                            I.amPerforming().switchTo().acceptAlert();
                        } else {
                            freshElement.sendKeys(args[2]);
                            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, args[1] + " : Retrying is Successful ");
                            I.amPerforming().switchTo().acceptAlert();
                        }
                    }
                } else {
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "Stale Element Exception : Retry cancelled");
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_CRITICAL, "Next Step might fail");
                }
                break;
            } catch (WebDriverException notClickableException) {
                if ((timerLimit <= System.currentTimeMillis() / 1000) || !notClickableException.getMessage().contains("is not clickable at point")) {
                    throw notClickableException;
                }
            }
        }
    }

}
