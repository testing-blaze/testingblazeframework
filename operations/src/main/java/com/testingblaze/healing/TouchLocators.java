package com.testingblaze.healing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testingblaze.actionsfactory.api.ElementAPI;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.http.RestfulWebServices;
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
    public static final Map<String, List> locatorInUse = new HashMap<>();
    private static RestfulWebServices selfHealer;
    protected static internalHttp httpCalls = new internalHttp();

    /**
     * @param locatorType format is "By-id"
     * @param locatorName
     * @return
     */
    public static String fetchLocatorFromDB(String locatorType, String locatorName) {
        if (!locatorRepository.containsKey(locatorName)) {
            var response = httpCalls.getCall(getEndPoint("fetchLocator","getlocator", locatorType.split("-")[1], locatorName), "monu.kumar@reisystems.com", "Test@123");
            String theLocator = response.getBody().jsonPath().get("theLocator");
            locatorRepository.put(locatorName, theLocator);
            performTouchDocuments(response, locatorType, theLocator, locatorName, false);
        }
        locatorInUse.put(locatorType.split("-")[1].toLowerCase(), List.of(locatorType, locatorName));
        return locatorRepository.get(locatorName);
    }

    protected static void performTouchDocuments(Response response, String locatorType, String theLocator, String locatorName, Boolean isRecovering) {
        if (isRecovering) {
            I.amPerforming().waitFor().ElementToBePresent(ElementAPI.getBy(locatorType, theLocator));
            JsonObject attributePayload = new JsonObject();
            attributePayload.addProperty("actionType", "recoveryLocatorTree");
            attributePayload.addProperty("locatorType", locatorType.split("-")[1]);
            attributePayload.addProperty("theLocatorName", locatorName);
            attributePayload.addProperty("theLocator", theLocator);
            httpCalls.postCall(getInitialLocatorTree(attributePayload, theLocator, locatorType), null, getEndPoint(null,"postLocatorTree", "none", "none"), "monu.kumar@reisystems.com", "Test@123", null);
            locatorRepository.put(locatorName, theLocator);
        } else {
            Boolean processingFlag = response.getBody().jsonPath().get("processingFlag");
            if (!processingFlag) {
                I.amPerforming().waitFor().ElementToBePresent(ElementAPI.getBy(locatorType, theLocator));
                JsonObject attributePayload = new JsonObject();
                attributePayload.addProperty("actionType", "createTheLocatorTree");
                attributePayload.addProperty("locatorType", locatorType.split("-")[1]);
                attributePayload.addProperty("theLocatorName", locatorName);
                httpCalls.postCall(getInitialLocatorTree(attributePayload, theLocator, locatorType), null, getEndPoint(null,"postLocatorTree", "none", "none"), "monu.kumar@reisystems.com", "Test@123", null);
            }
        }

        if (locatorType.split("-")[1].equalsIgnoreCase("xpath")) {
            if (isRecovering) {
                List<String> listOfChildLocators = httpCalls.getCall(getEndPoint("getChildLocators","getlocator", locatorType.split("-")[1], locatorName), "monu.kumar@reisystems.com", "Test@123").jsonPath().getList("listOfChildLocators");
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


    private static JsonObject getInitialLocatorTree(JsonObject attributePayload, String theLocator, String locatorType) {
        var theLocatorTree = fetchLocatorDetails(getElement().findElement(ElementAPI.getBy(locatorType, theLocator)), false);
        var iParentLocatorTree = fetchLocatorDetails(getElement().findElement(ElementAPI.getBy(locatorType, theLocator)), false);

        attributePayload.addProperty("theLocatorFieldType", (String) theLocatorTree.get("fieldType"));
        attributePayload.addProperty("theLocatorPosition", (long) theLocatorTree.get("position"));
        attributePayload.addProperty("theLocatorText", (String) theLocatorTree.get("text"));
        attributePayload.add("theLocatorAttributes", (JsonObject) theLocatorTree.get("attributes"));

        if (StringUtils.containsIgnoreCase(locatorType, "id")) {
            if (getElement().findElements(By.xpath(getXpathForIdParent(theLocator, ((String) theLocatorTree.get("fieldType")).toLowerCase()))).size() > 0) {
                var divParentLocatorTree = fetchLocatorDetails(getElement().findElement(By.xpath(getXpathForIdParent(theLocator, ((String) theLocatorTree.get("fieldType")).toLowerCase()))), false);
                attributePayload.addProperty("divParentFieldType", (String) divParentLocatorTree.get("fieldType"));
                attributePayload.addProperty("divParentPosition", (long) divParentLocatorTree.get("position"));
                attributePayload.add("divParentAttributes", (JsonObject) divParentLocatorTree.get("attributes"));
            } else {
                attributePayload.addProperty("divParentFieldType", "Not Found");
            }
        } else if (StringUtils.containsIgnoreCase(locatorType, "xpath")) {
            if (getElement().findElements(By.xpath(theLocator)).size() > 0) {
                var divParentLocatorTree = fetchLocatorDetails(getElement().findElement(By.xpath(theLocator)), false);
                attributePayload.addProperty("divParentFieldType", (String) divParentLocatorTree.get("fieldType"));
                attributePayload.addProperty("divParentPosition", (long) divParentLocatorTree.get("position"));
                attributePayload.add("divParentAttributes", (JsonObject) divParentLocatorTree.get("attributes"));
            } else {
                attributePayload.addProperty("divParentFieldType", "Not Found");
            }
        }

        attributePayload.addProperty("iParentFieldType", (String) iParentLocatorTree.get("fieldType"));
        attributePayload.addProperty("iParentPosition", (long) iParentLocatorTree.get("position"));
        attributePayload.add("iParentAttributes", (JsonObject) iParentLocatorTree.get("attributes"));
        return attributePayload;
    }

    private static void createChildTrees(String actionType, String locatorType, String locatorName, String theLocator, List<String> listOfChildLocators) {
        I.amPerforming().waitFor().ElementToBePresent(ElementAPI.getBy(locatorType, theLocator));
        JsonObject corePayload = new JsonObject();
        JsonObject childAttributes = new JsonObject();
        JsonArray childArray = new JsonArray();
        corePayload.addProperty("actionType", actionType);
        corePayload.addProperty("locatorType", locatorType.split("-")[1]);
        corePayload.addProperty("theLocatorName", locatorName);

        for (int i = 0; i < listOfChildLocators.size(); i++) {
            childAttributes.addProperty("sequence", i + 2);
            childAttributes.addProperty("theChildLocator", listOfChildLocators.get(i));
            getInitialLocatorTree(childAttributes, theLocator, locatorType);
            childArray.add(childAttributes);
        }
        corePayload.add("childLocators", childArray);
        httpCalls.postCall(corePayload, null, getEndPoint(null,"postLocatorTree", "none", "none"), "monu.kumar@reisystems.com", "Test@123", null);

    }

    protected static Map<String, Object> fetchLocatorDetails(WebElement element, Boolean parent) {
        Map<String, Object> locatorDetails = new LinkedHashMap<>();
        JsonObject attributesJson = new JsonObject();
        var theLocatorText = "blaze-no-text-found";
        long attributeLength = (long) jsInstance().executeJSCommand().executeScript("return arguments[0].attributes.length", element);
        if (parent) {
            for (long i = 0; i < attributeLength; i++) {
                String key = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.attributes[" + i + "].name", element);
                String value = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.attributes[" + i + "].value", element);
                attributesJson.addProperty(key, value);
            }
            locatorDetails.put("attributes", attributesJson);
            String fieldType = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].parentNode.tagName", element);
            locatorDetails.put("fieldType", fieldType);
        } else {
            for (long i = 0; i < attributeLength; i++) {
                String key = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].attributes[" + i + "].name", element);
                String value = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].attributes[" + i + "].value", element);
                attributesJson.addProperty(key, value);
            }
            locatorDetails.put("attributes", attributesJson);
            String fieldType = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].tagName", element);
            locatorDetails.put("fieldType", fieldType);
        }
        long position = (long) jsInstance().executeJSCommand().executeScript("return [].indexOf.call(arguments[0].parentNode.children, arguments[0])", element);
        //String convertedPosition = String.valueOf(position);
        locatorDetails.put("position", position);
        String textContent = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].innerText", element);
        if (!textContent.equals("")) theLocatorText = textContent;
        locatorDetails.put("text", theLocatorText);
        return locatorDetails;
    }

    private static String getEndPoint(String actionType,String endPointType, String locatorType, String locatorName) {
        var finalEndPoint = "";
        var initTouchDocuments = "http://127.0.0.1:8000/apis/touch_locator/?";
        switch (endPointType.toLowerCase()) {
            case "getlocator":
                finalEndPoint = initTouchDocuments + "actionType=" + actionType+ "&locatorType=" + locatorType + "&locatorName=" + locatorName + "&projectName=product";
                break;
            case "postlocatortree":
                finalEndPoint = initTouchDocuments + "projectName=product";
                break;
        }
        return finalEndPoint;

    }

    private static String getXpathForIdParent(String locator, String fieldType) {
        return "//" + fieldType + "[@id='" + locator + "']//ancestor::div[1]";
    }

    private static RestfulWebServices accessSelfHealer() {
        if (selfHealer == null) selfHealer = new RestfulWebServices();
        return selfHealer;
    }

    private static JavaScript jsInstance() {
        return InstanceRecording.getInstance(JavaScript.class);
    }

    private static WebDriver getElement() {
        return InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

}
