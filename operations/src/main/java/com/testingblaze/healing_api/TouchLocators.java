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
package com.testingblaze.healing_api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testingblaze.actionsfactory.abstracts.ElementProcessing;
import com.testingblaze.actionsfactory.api.ElementAPI;
import com.testingblaze.actionsfactory.api.HGJGcYGHQk;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.exception.TestingBlazeRunTimeException;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import io.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TouchLocators {
    private static final Map<String, String> locatorRepository = new LinkedHashMap<>();
    public static Map<String, List> locatorInUse = new HashMap<>();
    private static Map<String, String> userCredentials = null;
    protected static InternalHttp httpCalls = new InternalHttp();
    protected static HGJGcYGHQk iframeAnalyzer = InstanceRecording.getInstance(HGJGcYGHQk.class);
    protected static ElementProcessing elementProcessing = InstanceRecording.getInstance(ElementProcessing.class);

    /**
     * gets the locator and perform initial Touch document operatiosn
     *
     * @param locatorType format is "By-id"
     * @param locatorName
     * @return locator
     * @author nauman.shahid
     */
    public static String fetchLocatorFromDB(String locatorType, String locatorName) {
        if (!locatorRepository.containsKey(locatorName)) {
            var response = httpCalls.getCall(getEndPoint("fetchLocator", "getlocator", locatorType.split("-")[1], locatorName), getCredentials().get("user"), getCredentials().get("password"));
            String theLocator = response.getBody().jsonPath().get("theLocator");
            if (theLocator == null) {
                throw new TestingBlazeRunTimeException("Locator or credentials information is not valid.check locators name or information in self-healing.properties");
            } else {
                locatorRepository.put(locatorName, theLocator);
                performTouchDocuments(response, locatorType, theLocator, locatorName, false);
            }
        }
        locatorInUse.put(locatorType.split("-")[1].toLowerCase(), List.of(locatorType, locatorName));
        return locatorRepository.get(locatorName);
    }

    /**
     * perform api calls to get , post locators at initial and recovery stages
     *
     * @param response     as received from
     * @param locatorType
     * @param theLocator
     * @param locatorName
     * @param isRecovering true : if call is comming from {@link HealLocators}
     * @author nauman.shahid
     */
    protected static void performTouchDocuments(Response response, String locatorType, String theLocator, String locatorName, Boolean isRecovering) {
        if (isRecovering) {
            I.amPerforming().waitFor().ElementToBePresent(ElementAPI.getBy(locatorType, theLocator));
            JsonObject attributePayload = new JsonObject();
            attributePayload.addProperty("actionType", "recoveryLocatorTree");
            attributePayload.addProperty("locatorType", locatorType.split("-")[1]);
            attributePayload.addProperty("theLocatorName", locatorName);
            attributePayload.addProperty("theLocator", theLocator);
            httpCalls.postCall(getInitialLocatorTree(attributePayload, theLocator, locatorType), null, getEndPoint(null, "postLocatorTree", "none", "none"), getCredentials().get("user"), getCredentials().get("password"), null);
            locatorRepository.put(locatorName, theLocator);
        } else {
            Boolean processingFlag = response.getBody().jsonPath().get("processingFlag");
            if (!processingFlag) {
                iframeAnalyzer.setUpLocator(ElementAPI.getBy(locatorType, theLocator));
                elementProcessing.forSingleElement(ElementAPI.getBy(locatorType, theLocator));
                JsonObject attributePayload = new JsonObject();
                attributePayload.addProperty("actionType", "createTheLocatorTree");
                attributePayload.addProperty("locatorType", locatorType.split("-")[1]);
                attributePayload.addProperty("theLocatorName", locatorName);
                if (!StringUtils.containsIgnoreCase(HGJGcYGHQk.lastSuccessInfo, "Default Content")) {
                    attributePayload.addProperty("executionContext", HGJGcYGHQk.lastSuccessInfo);
                } else {
                    attributePayload.addProperty("executionContext", "Default Context");
                }
                httpCalls.postCall(getInitialLocatorTree(attributePayload, theLocator, locatorType), null, getEndPoint(null, "postLocatorTree", "none", "none"), getCredentials().get("user"), getCredentials().get("password"), null);
            }
        }

        if (locatorType.split("-")[1].equalsIgnoreCase("xpath")) {
            if (isRecovering) {
                List<String> listOfChildLocators = httpCalls.getCall(getEndPoint("getChildLocators", "getlocator", locatorType.split("-")[1], locatorName), getCredentials().get("user"), getCredentials().get("password")).jsonPath().getList("listOfChildLocators");
                createChildTrees("recoveryChildLocatorTree", locatorType, locatorName, theLocator, listOfChildLocators);
            } else {
                Boolean childProcessingFlag = response.getBody().jsonPath().get("childProcessingFlag");
                if (!childProcessingFlag) {
                    List<String> listOfChildLocators = response.getBody().jsonPath().getList("childLocators");
                    createChildTrees("createChildLocatorTree", locatorType, locatorName, theLocator, listOfChildLocators);
                }
            }
        }

    }

    /**
     * prepares locator details for all type of payloads
     *
     * @param element
     * @param parent
     * @return map of locator details
     * @author nauman.shahid
     */
    protected static Map<String, Object> fetchLocatorDetails(WebElement element, Boolean parent) {
        Map<String, Object> locatorDetails = new LinkedHashMap<>();
        JsonObject attributesJson = new JsonObject();
        var theLocatorText = "blaze-no-text-found";
        long attributeLength = 0;

        if (parent)
            attributeLength = (long) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.attributes.length", element);
        else {
            attributeLength = (long) jsInstance().executeJSCommand().executeScript("return arguments[0].attributes.length", element);
        }

        if (parent) {
            for (long i = 0; i < attributeLength; i++) {
                String key = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.attributes[" + i + "].name", element);
                String value = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.attributes[" + i + "].value", element);
                attributesJson.addProperty(key, value);
            }
            locatorDetails.put("attributes", attributesJson);
            String fieldType = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.tagName", element);
            locatorDetails.put("fieldType", fieldType);
            long position = (long) jsInstance().executeJSCommand().executeScript("return [].indexOf.call(arguments[0].parentNode.children, arguments[0].parentNode)", element);
            locatorDetails.put("position", position);
            String textContent = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.innerText", element);
            if (!textContent.equals("")) theLocatorText = textContent;
            locatorDetails.put("text", theLocatorText);
        } else {
            for (long i = 0; i < attributeLength; i++) {
                String key = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].attributes[" + i + "].name", element);
                String value = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].attributes[" + i + "].value", element);
                attributesJson.addProperty(key, value);
            }
            locatorDetails.put("attributes", attributesJson);
            String fieldType = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].tagName", element);
            locatorDetails.put("fieldType", fieldType);
            long position = (long) jsInstance().executeJSCommand().executeScript("return [].indexOf.call(arguments[0].parentNode.children, arguments[0])", element);
            locatorDetails.put("position", position);
            String textContent = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].innerText", element);
            if (!textContent.equals("")) theLocatorText = textContent;
            locatorDetails.put("text", theLocatorText);
        }

        return locatorDetails;
    }

    protected static Map<String, String> getCredentials() {
        if (userCredentials == null) {
            userCredentials = new LinkedHashMap<>();
            try {
                userCredentials.put("user", I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile("selfhealing.properties", "user"));
                userCredentials.put("password", I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile("selfhealing.properties", "password"));
                userCredentials.put("project", I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile("selfhealing.properties", "project"));
                userCredentials.put("connection", I.amPerforming().propertiesFileOperationsTo().ReadPropertyFile("selfhealing.properties", "connection"));
            } catch (Exception e) {
                throw new TestingBlazeRunTimeException("There is a problem with self healing credentials");
            }
        }
        return userCredentials;
    }

    /**
     * prepares payload for initial locator tree
     *
     * @param attributePayload
     * @param theLocator
     * @param locatorType
     * @return JsonObject of initial locator tree
     * @author nauman.shahid
     */
    private static JsonObject getInitialLocatorTree(JsonObject attributePayload, String theLocator, String locatorType) {
        var theLocatorTree = fetchLocatorDetails(getElement().findElement(ElementAPI.getBy(locatorType, theLocator)), false);
        var iParentLocatorTree = fetchLocatorDetails(getElement().findElement(ElementAPI.getBy(locatorType, theLocator)), true);

        attributePayload.addProperty("theLocatorFieldType", (String) theLocatorTree.get("fieldType"));
        attributePayload.addProperty("theLocatorPosition", (long) theLocatorTree.get("position"));
        attributePayload.addProperty("theLocatorText", (String) theLocatorTree.get("text"));
        attributePayload.add("theLocatorAttributes", (JsonObject) theLocatorTree.get("attributes"));

        if (StringUtils.containsIgnoreCase(locatorType, "id")) {
            if (getElement().findElements(By.xpath(getXpathForIdParent(theLocator, ((String) theLocatorTree.get("fieldType")).toLowerCase()))).size() > 0) {
                var divParentLocatorTree = fetchLocatorDetails(getElement().findElement(By.xpath(getXpathForIdParent(theLocator, ((String) theLocatorTree.get("fieldType")).toLowerCase()))), false);
                if (!iParentLocatorTree.get("attributes").equals(divParentLocatorTree.get("attributes"))) {
                    attributePayload.addProperty("divParentFieldType", (String) divParentLocatorTree.get("fieldType"));
                    attributePayload.addProperty("divParentPosition", (long) divParentLocatorTree.get("position"));
                    attributePayload.add("divParentAttributes", (JsonObject) divParentLocatorTree.get("attributes"));
                } else {
                    attributePayload.addProperty("divParentFieldType", "Not Found");
                }
            } else {
                attributePayload.addProperty("divParentFieldType", "Not Found");
            }
        } else if (StringUtils.containsIgnoreCase(locatorType, "xpath")) {
            if (getElement().findElements(By.xpath(theLocator)).size() > 0) {
                var divParentLocatorTree = fetchLocatorDetails(getElement().findElement(By.xpath(theLocator + "//ancestor::div[1]")), false);
                if (!iParentLocatorTree.get("attributes").equals(divParentLocatorTree.get("attributes"))) {
                    attributePayload.addProperty("divParentFieldType", (String) divParentLocatorTree.get("fieldType"));
                    attributePayload.addProperty("divParentPosition", (long) divParentLocatorTree.get("position"));
                    attributePayload.add("divParentAttributes", (JsonObject) divParentLocatorTree.get("attributes"));
                } else {
                    attributePayload.addProperty("divParentFieldType", "Not Found");
                }
            } else {
                attributePayload.addProperty("divParentFieldType", "Not Found");
            }
        }

        attributePayload.addProperty("iParentFieldType", (String) iParentLocatorTree.get("fieldType"));
        attributePayload.addProperty("iParentPosition", (long) iParentLocatorTree.get("position"));
        attributePayload.add("iParentAttributes", (JsonObject) iParentLocatorTree.get("attributes"));
        return attributePayload;
    }

    /**
     * prepares payload for xpath children dtree
     *
     * @param actionType
     * @param locatorType
     * @param locatorName
     * @param theLocator
     * @param listOfChildLocators
     * @author nauman.shahid
     */
    private static void createChildTrees(String actionType, String locatorType, String locatorName, String theLocator, List<String> listOfChildLocators) {
        iframeAnalyzer.setUpLocator(ElementAPI.getBy(locatorType, theLocator));
        elementProcessing.forSingleElement(ElementAPI.getBy(locatorType, theLocator));
        JsonObject corePayload = new JsonObject();
        JsonArray childArray = new JsonArray();
        corePayload.addProperty("actionType", actionType);
        corePayload.addProperty("locatorType", locatorType.split("-")[1]);
        corePayload.addProperty("theLocatorName", locatorName);

        for (int i = 0; i < listOfChildLocators.size(); i++) {
            JsonObject childAttributes = new JsonObject();
            childAttributes.addProperty("sequence", i + 2);
            childAttributes.addProperty("theChildLocator", listOfChildLocators.get(i));
            childArray.add(getInitialLocatorTree(childAttributes, theLocator, locatorType));
        }
        corePayload.add("childLocators", childArray);
        httpCalls.postCall(corePayload, null, getEndPoint(null, "postLocatorTree", "none", "none"), getCredentials().get("user"), getCredentials().get("password"), null);
    }


    private static String getEndPoint(String actionType, String endPointType, String locatorType, String locatorName) {
        var finalEndPoint = "";
        var initTouchDocuments = getCredentials().get("connection") + "/apis/touch_locator/?";
        switch (endPointType.toLowerCase()) {
            case "getlocator":
                finalEndPoint = initTouchDocuments + "actionType=" + actionType + "&locatorType=" + locatorType + "&locatorName=" + locatorName + "&projectName=" + getCredentials().get("project");
                break;
            case "postlocatortree":
                finalEndPoint = initTouchDocuments + "projectName=" + getCredentials().get("project");
                break;
        }
        return finalEndPoint;

    }

    private static String getXpathForIdParent(String locator, String fieldType) {
        return "//" + fieldType + "[@id='" + locator + "']//ancestor::div[1]";
    }

    private static JavaScript jsInstance() {
        return InstanceRecording.getInstance(JavaScript.class);
    }

    private static WebDriver getElement() {
        return InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

}
