package com.testingblaze.healingdoor;

import com.google.gson.JsonObject;
import com.testingblaze.actionsfactory.api.ElementAPI;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.http.RestfulWebServices;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Gateway {
    private static final Map<String, String> locatorRepository = new HashMap<>();
    private static RestfulWebServices selfHealer;

    public static String fetchLocatorFromDB(String locatorCategory, String locatorType, String locatorName) {
        if (!locatorRepository.containsKey(locatorName)) {
            var response = accessSelfHealer().getCall(getEndPoint("fetchLocator", locatorType, locatorName), "monu.kumar@reisystems.com", "Test@123");
            String theLocator = response.getBody().jsonPath().get("theLocator");
            locatorRepository.put(locatorName, theLocator);
            performTouchDocuments(response, locatorType, theLocator, locatorName);
        }
        return locatorRepository.get(locatorName);
    }

    private static void performTouchDocuments(Response response, String locatorType, String theLocator, String locatorName) {
        Boolean processingFlag = response.getBody().jsonPath().get("processingFlag");

        if (!processingFlag) {
            if (locatorType.equalsIgnoreCase("id")) {
                manageID("createTheLocatorTree", ElementAPI.getBy(locatorType, theLocator), theLocator, locatorName, locatorType);
            }

        }

        if (locatorType.equalsIgnoreCase("xpath")) {
            Boolean childProcessingFlag = response.getBody().jsonPath().get("childProcessingFlag");
            if (!childProcessingFlag) {

            }
        }
    }

    private static RestfulWebServices accessSelfHealer() {
        if (selfHealer == null) selfHealer = new RestfulWebServices();
        return selfHealer;
    }

    private static String getEndPoint(String endPointType, String locatorType, String locatorName) {
        var finalEndPoint = "";
        var initTouchDocuments = "http://127.0.0.1:8000/apis/touch_locator/?";
        switch (endPointType.toLowerCase()) {
            case "fetchlocator":
                finalEndPoint = initTouchDocuments + "actionType=" + endPointType + "&locatorType=" + locatorType + "&locatorName=" + locatorName + "&projectName=product";
                break;
        }
        return finalEndPoint;

    }

    private static <T> void manageID(String payLoadType, By locator, String theLocator, String locatorName, String locatorType) {
        I.amPerforming().waitFor().ElementToBePresent(locator);
        JsonObject attributePayload = new JsonObject();
        if (payLoadType.equalsIgnoreCase("createTheLocatorTree")) {
            var theLocatorTree = fetchLocatorDetails(getElement().findElement(locator), false);
            attributePayload.addProperty("actionType", payLoadType);
            attributePayload.addProperty("locatorType", locatorType);
            attributePayload.addProperty("theLocatorName", locatorName);
            attributePayload.addProperty("theLocatorFieldType", (String) theLocatorTree.get("fieldType"));
            attributePayload.addProperty("theLocatorPosition", (String) theLocatorTree.get("position"));
            attributePayload.addProperty("theLocatorText", (String) theLocatorTree.get("text"));
            attributePayload.add("theLocatorAttributes", (JsonObject) theLocatorTree.get("attributes"));
        }
        System.out.println("payload is " + attributePayload);
    }

    private static JavaScript jsInstance() {
        return InstanceRecording.getInstance(JavaScript.class);
    }

    private static WebDriver getElement() {
        return InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }



    private static Map<String, Object> fetchLocatorDetails(WebElement element, Boolean parent) {
        Map<String, Object> locatorDetails = new LinkedHashMap<>();
        Map<String, String> attributes = new LinkedHashMap<>();
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
        String convertedPosition = String.valueOf(position);
        locatorDetails.put("position", convertedPosition);
        String textContent = (String) jsInstance().executeJSCommand().executeScript("return arguments[0].innerText", element);
        if (!textContent.equals("")) theLocatorText = textContent;
        locatorDetails.put("text", theLocatorText);
        return locatorDetails;
    }

    private String getXpathforidparent(String locator, String fieldType) {
        return "//" + fieldType + "[@id='" + locator + "']//ancestor::div[1]";
    }

}
