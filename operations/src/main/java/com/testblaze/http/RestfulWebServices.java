/*
 * Copyright 2020
 *
 * This file is part of  Test Blaze Bdd Framework [Test Blaze Automation Solution].
 *
 * Test Blaze Bdd Framework is licensed under the Apache License, Version
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
package com.testblaze.http;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testblaze.register.i;
import com.testblaze.report.LogLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.concurrent.TimeUnit;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Handles all Api calls
 */
public final class RestfulWebServices {
    Gson gson = new Gson();

    public enum CallTypes {GET, POST, PATCH, DELETE, PUT}


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
     * @param jsonBody
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response postCall(JsonObject jsonBody, String stringBody, String endPoint, String key, String keyValue,
                             String authToken) {
        return makeCall(CallTypes.POST, jsonBody, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     * make a post call
     *
     * @param jsonArray
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response postCall(JsonArray jsonArray, String endPoint, String key, String keyValue,
                             String authToken) {
        return makeCall(CallTypes.POST, jsonArray, endPoint, key, keyValue, authToken);
    }

    /**
     * make a put call
     *
     * @param jsonBody
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response putCall(JsonObject jsonBody, String stringBody, String endPoint, String key, String keyValue,
                            String authToken) {
        return makeCall(CallTypes.PUT, jsonBody, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     * make a post call
     *
     * @param jsonBody
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response patchCall(JsonObject jsonBody, String stringBody, String endPoint, String key, String keyValue,
                              String authToken) {
        return makeCall(CallTypes.PATCH, jsonBody, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     * make a post call
     *
     * @param jsonBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response patchCall(JsonArray jsonBody, String endPoint, String key, String keyValue,
                              String authToken) {
        return makeCall(CallTypes.PATCH, jsonBody, endPoint, key, keyValue, authToken);
    }


    /**
     * make a delete call
     *
     * @param jsonBody
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    public Response DeleteCall(JsonObject jsonBody, String stringBody, String endPoint, String key, String keyValue,
                               String authToken) {
        return makeCall(CallTypes.DELETE, jsonBody, stringBody, endPoint, key, keyValue, authToken);
    }

    /**
     * Combines the different request methods, since they had a lot of duplicated code.
     *
     * @param callType
     * @param jsonBody
     * @param stringBody
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    private Response makeCall(CallTypes callType,
                              JsonObject jsonBody, String stringBody, String endPoint, String key, String keyValue,
                              String authToken) {

        // Print pre-request logs
        try {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Executing " + callType.name() + " Api");
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "End Point is " + endPoint);
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
        request.header("Content-Type", "application/json");
        if (keyValue != null) {
            request.header(key, keyValue);
        }
        if (authToken != null) {
            request.auth().oauth2(authToken);
        }
        if (jsonBody != null) {
            try {
                i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Json is " + jsonBody);
            } catch (Exception e) {
                // to handle reporting exception - avoid unnecessary exceptions
            }
            request.body(gsonToJson(jsonBody));
        }
        if (stringBody != null) {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Json is " + stringBody);
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

    /**
     * Combines the different request methods, since they had a lot of duplicated code.
     *
     * @param callType
     * @param jsonArray
     * @param endPoint
     * @param key
     * @param keyValue
     * @param authToken
     * @return
     */
    private Response makeCall(CallTypes callType,
                              JsonArray jsonArray, String endPoint, String key, String keyValue,
                              String authToken) {

        // Print pre-request logs
        try {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Executing " + callType.name() + " Api");
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "End Point is " + endPoint);
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
        request.header("Content-Type", "application/json");
        if (keyValue != null) {
            request.header(key, keyValue);
        }
        if (authToken != null) {
            request.auth().oauth2(authToken);
        }
        if (jsonArray != null) {
            try {
                i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Json is " + jsonArray);
            } catch (Exception e) {
                // to handle reporting exception - avoid unnecessary exceptions
            }
            request.body(gson.toJson(jsonArray));
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

    /**
     * convert to gson to json
     *
     * @param body
     * @return
     */
    public String gsonToJson(JsonObject body) {
        return gson.toJson(body);
    }

    private void reportsLogger(CallTypes callType, Response response) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, callType.name() + " Api call Response details:");
        i.amPerforming().updatingReportWith().write(LogLevel.EMPTY_LABEL, "1 - Response status: " + response.statusLine());
        i.amPerforming().updatingReportWith().write(LogLevel.EMPTY_LABEL, "2 - Response time: " + response.timeIn(TimeUnit.SECONDS) + " sec");
        if (System.getProperty("postTestResults") == null)
        i.amPerforming().updatingReportWith().write(LogLevel.EMPTY_LABEL, "3 - Response body: " + response.body().asString());
    }

    private void consoleLogger(CallTypes callType, Response response) {
        System.out.println(callType.name() + " Api call Response details:");
        System.out.println("1 - Response status: " + response.statusLine());
        System.out.println("2 - Response time: " + response.timeIn(TimeUnit.SECONDS) + " sec");
        if (System.getProperty("postTestResults") == null)
        System.out.println("3 - Response body: " + response.body().asString());
    }
}
