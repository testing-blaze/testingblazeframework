package com.testingblaze.healing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testingblaze.actionsfactory.api.ElementAPI;
import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.objects.TwoColumnSorting;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HealLocators {
    public By performHealing() {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT,"------Self-Healing Activated--------");
        By locator = null;
        if (TouchLocators.locatorInUse.containsKey("id"))
            locator = executeForID(getIdRecovery((String) TouchLocators.locatorInUse.get("id").get(1)));
        else if (TouchLocators.locatorInUse.containsKey("xpath"))
            locator = executeForXpath((String) TouchLocators.locatorInUse.get("xpath").get(1));
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT,"------Self-Healing Completed--------");
        return locator;
    }


    private By executeForXpath(String locatorName) {
        // fetch all children to analyze alive children
        Map<Object, Object> initialMapOfChildLocatorsWithDbIds = TouchLocators.httpCalls.getCall(getEndPoint("getAllChildLocators", "xpath", locatorName), "monu.kumar@reisystems.com", "Test@123").jsonPath().getMap("listOfChildLocators");
        JsonObject preRecoveryInformation = new JsonObject();
        JsonArray listOfDeadChildren = new JsonArray();
        for (var entrySet : initialMapOfChildLocatorsWithDbIds.entrySet()) {
            if (getElement().findElements(By.xpath((String) entrySet.getValue())).size() == 0) {
                listOfDeadChildren.add((String)entrySet.getKey());
            } else if (getElement().findElements(By.xpath((String) entrySet.getValue())).size() > 0) {
                if (!getElement().findElement(By.xpath((String) entrySet.getValue())).isDisplayed())
                    listOfDeadChildren.add((String) entrySet.getKey());
            }
        }
        preRecoveryInformation.addProperty("actionType", "getRecoveryChildLocators");
        preRecoveryInformation.addProperty("locatorType", "xpath");
        preRecoveryInformation.addProperty("theLocatorName", locatorName);
        preRecoveryInformation.add("listOfIdsOfDeadChildren", listOfDeadChildren);
        // push back alive children
        JsonObject dictOfSuccessfulSortedLocators = new JsonObject();
        JsonObject dictOfAliveSortedChildCombinations = new JsonObject();
        dictOfSuccessfulSortedLocators.addProperty("actionType", "getRecoveryLocators");
        dictOfSuccessfulSortedLocators.addProperty("locatorType", "xpath");
        dictOfSuccessfulSortedLocators.addProperty("theLocatorName", locatorName);
        Map<Object, List<String>> dictOfPossibleChildCombinations = TouchLocators.httpCalls.postCall(preRecoveryInformation, null, getEndPoint("getRecoveryChildLocators", "xpath", locatorName), "monu.kumar@reisystems.com", "Test@123", null).jsonPath().getMap("listOfChildRecoveryLocators");
        for (var keySet : dictOfPossibleChildCombinations.keySet()) {
            List<TwoColumnSorting> listOfSortedChildLocators = sortingLocators(dictOfPossibleChildCombinations.get(keySet).stream().filter(locator -> getElement().findElements(By.xpath(locator)).size() > 0).
                    map(locator -> new TwoColumnSorting(locator, getElement().findElements(By.xpath(locator)).size())).collect(Collectors.toList()));
            JsonArray listOfFinalSortedChildLocators = new JsonArray();
            listOfSortedChildLocators.stream().forEach(locator -> listOfFinalSortedChildLocators.add(locator.getKey()));

            dictOfAliveSortedChildCombinations.add((String) keySet, listOfFinalSortedChildLocators);
        }
        dictOfSuccessfulSortedLocators.add("dictOfSuccessfulSortedLocators", dictOfAliveSortedChildCombinations);
        //fetching final recovery locators
        List<String> listOfFinalLocators=TouchLocators.httpCalls.postCall(dictOfSuccessfulSortedLocators, null,getEndPoint("getRecoveryLocators","xpath", locatorName),"monu.kumar@reisystems.com", "Test@123", null).jsonPath().getList("listOfRecoveryLocators");
        // evaluating the alive xpath and saving it back to DB
        By finalLocator = null;
        for (int i = 0; i < listOfFinalLocators.size(); i++) {
            if (getElement().findElement(By.xpath(listOfFinalLocators.get(0))).isDisplayed()) {
                finalLocator = ElementAPI.getBy((String)TouchLocators.locatorInUse.get("xpath").get(0),listOfFinalLocators.get(0));
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT,"New Locator is "+finalLocator);
                TouchLocators.performTouchDocuments(null, (String) TouchLocators.locatorInUse.get("xpath").get(0), listOfFinalLocators.get(0), (String) TouchLocators.locatorInUse.get("xpath").get(1), true);
                break;
            }
        }
        return finalLocator;
    }

    private List<String> getIdRecovery(String locatorName) {
        return TouchLocators.httpCalls.getCall(getEndPoint("getRecoveryLocators", "id", locatorName), "monu.kumar@reisystems.com", "Test@123").jsonPath().getList("listOfLocators");
    }

    private By executeForID(List<String> listOfLocators) {
        By finalLocator = null;
        List<TwoColumnSorting> listOfFinalLocators = sortingLocators(listOfLocators.stream().filter(locator -> getElement().findElements(By.xpath(locator)).size() > 0).
                map(locator -> new TwoColumnSorting(locator, getElement().findElements(By.xpath(locator)).size())).collect(Collectors.toList()));
        for (int i = 0; i < listOfFinalLocators.size(); i++) {
            if (getElement().findElement(By.xpath(listOfFinalLocators.get(0).getKey())).isDisplayed()) {
                JsonObject attributesMap = (JsonObject) TouchLocators.fetchLocatorDetails(getElement().findElement(By.xpath(listOfFinalLocators.get(0).getKey())), false).get("attributes");
                var id = attributesMap.get("id").getAsString();
                finalLocator = ElementAPI.getBy((String)TouchLocators.locatorInUse.get("id").get(0),id);
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_IMPORTANT,"New Locator is "+finalLocator);
                TouchLocators.performTouchDocuments(null, (String) TouchLocators.locatorInUse.get("id").get(0), id, (String) TouchLocators.locatorInUse.get("id").get(1), true);
                break;
            }
        }
        return finalLocator;
    }

    private List<TwoColumnSorting> sortingLocators(List<TwoColumnSorting> locatorsWithMatchingNodes) {
        Comparator<TwoColumnSorting> customComparison = Comparator.comparingInt(sort -> sort.value);
        return locatorsWithMatchingNodes.stream().sorted(customComparison).collect(Collectors.toList());
    }

    private String getEndPoint(String endPointType, String locatorType, String locatorName) {
        var finalEndPoint = "";
        var initTouchDocuments = "http://127.0.0.1:8000/apis/locator_healing/?";
        if ((endPointType.equalsIgnoreCase("getRecoveryLocators") && locatorType.equalsIgnoreCase("id")) || endPointType.equalsIgnoreCase("getAllChildLocators")) {
            finalEndPoint = initTouchDocuments + "actionType=" + endPointType + "&locatorType=" + locatorType + "&locatorName=" + locatorName + "&projectName=product";
        } else if (endPointType.equalsIgnoreCase("getRecoveryChildLocators") || (endPointType.equalsIgnoreCase("getRecoveryLocators") && locatorType.equalsIgnoreCase("xpath"))) {
            finalEndPoint = initTouchDocuments + "projectName=product";
        }
        return finalEndPoint;
    }

    private WebDriver getElement() {
        return InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

}
