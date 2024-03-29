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
package com.testingblaze.http;

import com.google.gson.*;
import com.testingblaze.controller.TestingBlazeGlobal;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author nauman.shahid

 * Handles all Api calls
 */
public final class RestfulWebServices {
    private static final Gson gson = new Gson();
    public boolean isJvmHookOn=false;

    public enum CallTypes {GET, POST, PATCH, DELETE, PUT}

    /**
     * Make a raw request with own compiled request
     *
     * @param callTypes   CallTypes.GET
     * @param requestLoad
     * @param endPoint
     * @return
     * @author nauman.shahid
     */
    public Response rawRequest(CallTypes callTypes, RequestSpecification requestLoad, String endPoint) {
        Response response = null;
        // Print pre-request logs
        try {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Executing " + callTypes.name() + " Api");
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "End Point is " + endPoint);
        } catch (Exception e) {
            // to handle reporting exception - avoid unnecessary exceptions
        }
        if (callTypes.equals(CallTypes.POST)) response = requestLoad.post(endPoint);
        else if (callTypes.equals(CallTypes.GET)) response = requestLoad.get(endPoint);
        else if (callTypes.equals(CallTypes.PATCH)) response = requestLoad.patch(endPoint);
        else if (callTypes.equals(CallTypes.DELETE)) response = requestLoad.delete(endPoint);
        try {
            this.reportsLogger(callTypes, response);
        } catch (Exception var11) {
            this.consoleLogger(callTypes, response);
        }
        return response;
    }

    /**
     * Make a get call
     *
     * @param endPoint
     * @param key
     * @param keyValue
     * @return
     */
    public Response getCall(String endPoint, String key, String keyValue) {
        return makeCall(CallTypes.GET, null, null, endPoint, key, keyValue, null);
    }

    /**
     * make a post call
     *
     * @param jsonElement
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response postCall(JsonElement jsonElement, String stringBody, String endPoint, String key, String keyValue,
                             String authToken) {
        return makeCall(CallTypes.POST, jsonElement, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     * make a put call
     *
     * @param jsonElement
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response putCall(JsonElement jsonElement, String stringBody, String endPoint, String key, String keyValue,
                            String authToken) {
        return makeCall(CallTypes.PUT, jsonElement, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     * make a post call
     *
     * @param jsonElement
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response patchCall(JsonElement jsonElement, String stringBody, String endPoint, String key, String keyValue,
                              String authToken) {
        return makeCall(CallTypes.PATCH, jsonElement, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     * make a delete call
     *
     * @param jsonElement
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response DeleteCall(JsonElement jsonElement, String stringBody, String endPoint, String key, String keyValue,
                               String authToken) {
        return makeCall(CallTypes.DELETE, jsonElement, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     *  This method can validate any sort of response.
     * @param expectedResponseInString pass response in String
     * @param actualResponseInString pass actual response in String
     * @param dontCheckTheseKeys add Set of keywords that should not be part of validation...
     *                           e.g
     *                           {
     *                              "name":{
     *                                  "firstname":"jitu",
     *                                  "lastname":"pisal
     *                              }
     *                           }
     *   if we don't want to validate "lastname" then add it in the Set<String>
     */
    public void validateJsonResponse(String expectedResponseInString, String actualResponseInString, Set<String> dontCheckTheseKeys){
        validateJsonResponse(I.amPerforming().fileHandling().forJsonAnd().getJsonObject(expectedResponseInString),
                I.amPerforming().fileHandling().forJsonAnd().getJsonObject(actualResponseInString), dontCheckTheseKeys);
    }

    /**
     *  This method can validate any sort of response.
     * @param expectedResponseInString pass response in String
     * @param actualResponseInString pass actual response in String
     */
    public void validateJsonResponse(String expectedResponseInString, String actualResponseInString){
        validateJsonResponse(I.amPerforming().fileHandling().forJsonAnd().getJsonObject(expectedResponseInString),
                I.amPerforming().fileHandling().forJsonAnd().getJsonObject(actualResponseInString),
                new HashSet<>());
    }

    /**
     * Combines the different request methods, since they had a lot of duplicated code.
     *
     * @param callType
     * @param jsonElement
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    private Response makeCall(CallTypes callType,
                              JsonElement jsonElement, String stringBody, String endPoint, String key, String keyValue,
                              String authToken) {

        // Print pre-request logs
        try {
            if ((Boolean) TestingBlazeGlobal.getVariable("turnOffHealerLogs")) {
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Executing " + callType.name() + " Api");
                I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "End Point is " + endPoint);
            }
        } catch (Exception e) {
            // to handle reporting exception - avoid unnecessary exceptions
        }

        // Make sure that GET calls do not use relaxed HTTPS validation
        RequestSpecification request;
        if (callType == CallTypes.GET) {
            request = RestAssured.given();
        } else {
            request = RestAssured.given().relaxedHTTPSValidation();
        }

        // Construct the request
        request.accept("application/json");
        request.contentType(ContentType.JSON);
        if (keyValue != null) {
            if (StringUtils.containsIgnoreCase(keyValue, "Bearer")) request.header(key, keyValue);
            else {
                request.auth().preemptive().basic(key, keyValue);
            }

        }
        if (authToken != null) {
            request.auth().oauth2(authToken);
        }
        if (jsonElement != null) {
            if(!isJvmHookOn) {
                try {
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Json is " + jsonElement);
                } catch (Exception e) {
                    // to handle reporting exception - avoid unnecessary exceptions
                }
            }
            request.body(gson.toJson(jsonElement));
        }
        if (stringBody != null) {
            if(!isJvmHookOn) {
                try {
                    I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Json is " + stringBody);
                } catch (Exception e) {
                    // to handle reporting exception - avoid unnecessary exceptions
                }
            }
            request.body(stringBody);
        }

        // Make the actual call
        Response response = null;
        switch (callType) {
            case GET:
                response = request.get(endPoint);
                break;
            case POST:
                response = request.post(endPoint);
                break;
            case PUT:
                response = request.put(endPoint);
                break;
            case PATCH:
                response = request.patch(endPoint);
                break;
            case DELETE:
                response = request.delete(endPoint);
                break;
        }
        try {
            reportsLogger(callType, response);
        } catch (Exception e) {
            // to handle reporting exception - avoid unnecessary exceptions
            consoleLogger(callType, response);
        }
        return response;
    }

    private void reportsLogger(CallTypes callType, Response response) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, callType.name() + " Api call Response details:");
        I.amPerforming().updatingOfReportWith().write(LogLevel.EMPTY_LABEL, "1 - Response status: " + response.statusLine());
        I.amPerforming().updatingOfReportWith().write(LogLevel.EMPTY_LABEL, "2 - Response time: " + response.timeIn(TimeUnit.SECONDS) + " sec");
        if (System.getProperty("postTestResults") == null)
            I.amPerforming().updatingOfReportWith().write(LogLevel.EMPTY_LABEL, "3 - Response body: " + response.body().asString());
    }

    private void consoleLogger(CallTypes callType, Response response) {
        System.out.println(callType.name() + " Api call Response details:");
        System.out.println("1 - Response status: " + response.statusLine());
        System.out.println("2 - Response time: " + response.timeIn(TimeUnit.SECONDS) + " sec");
        if (System.getProperty("postTestResults") == null)
            System.out.println("3 - Response body: " + response.body().asString());
    }

    /**
     *  This method can validate any sort of response.
     * @param expectedResponse pass response in JsonObject(gson)
     * @param actualResponse pass actual response in JsonObject(gson)
     * @param dontCheckTheseKeys add Set of keywords that should not be part of validation...
     *                           e.g
     *                           {
     *                              "name":{
     *                                  "firstname":"jitu",
     *                                  "lastname":"pisal
     *                              }
     *                           }
     *   if we don't want to validate "lastname" then add it in the Set<String>
     */
    private void validateJsonResponse(JsonObject expectedResponse, JsonObject actualResponse, Set<String> dontCheckTheseKeys) {
        for (String key : actualResponse.keySet()) {
            if (actualResponse.get(key) instanceof JsonObject) {
                JsonObject expectedChildResponseObject = expectedResponse.getAsJsonObject(key);
                JsonObject childResponseObject = actualResponse.getAsJsonObject(key);
                validateJsonResponse(expectedChildResponseObject, childResponseObject, dontCheckTheseKeys);
            } else if (actualResponse.get(key) instanceof JsonArray) {
                JsonArray childResponseArray = actualResponse.getAsJsonArray(key);
                JsonArray expectedChildResponseArray = expectedResponse.getAsJsonArray(key);
                for (int i = 0; i < childResponseArray.size(); i++) {
                    if (childResponseArray.get(i) instanceof JsonObject) {
                        validateJsonResponse((JsonObject) expectedChildResponseArray.get(i), (JsonObject) childResponseArray.get(i), dontCheckTheseKeys);
                    } else {
                        if (!dontCheckTheseKeys.contains(key)) {
                            if (actualResponse.get(String.valueOf(childResponseArray.get(i))) == null) {
                                Assert.assertEqualsNoOrder(new Gson().fromJson(actualResponse.get(key), String[].class),
                                        new Gson().fromJson(expectedResponse.get(key), String[].class));
                            } else {
                                Assert.assertEquals(childResponseArray.get(i),
                                        expectedChildResponseArray.get(i),
                                        "Failed while validating the response" +
                                                actualResponse.get(key.toString()));
                            }
                        }
                    }
                }
            } else if (!dontCheckTheseKeys.contains(key)) {
                Assert.assertEquals(actualResponse.get(key).toString(), expectedResponse.get(key).toString(),
                        "Failed while validating the response" + actualResponse.get(key.toString()));
            }
        }
    }
}
