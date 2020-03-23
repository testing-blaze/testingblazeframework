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
package com.fast.integrations;

import com.fast.controller.ReportingLogsPlugin;
import com.fast.register.EnvironmentFetcher;
import com.fast.register.i;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tfs {
    private static String tfsUrl, pat, authType, apiVersion;
    private static boolean loadTfsConfig = true;
    private static Map<String, List<String>> suitesToPlansMapping = new LinkedHashMap<>();
    private static List<String> planIds = new ArrayList<>();
    private static List<String> runIds = new ArrayList<>();
    private static Map<String, Map<String, String>> testIdsToTestResultIdsMappingToRuns = new LinkedHashMap<>();

    public Tfs() {
        if (loadTfsConfig) {
            System.out.println("-------------------- TFS Result Post Configuration Started -----------------------");
            loadTfsConfiguration();
            System.out.println("........ in Progress");
            setUpInitialConfiguration();
            System.out.println("-------------------- TFS Result Post Configuration Completed -----------------------");
        }
    }

    /******************* Set up Pre-Result Posting Configurations  **********************************/
    private static void setUpInitialConfiguration() {
        setUpTestPlanIdsInfo();
        mapSuitesToPlans();
        mapTestPointsToTestRunAndTestCaseId();
    }

    private static void mapSuitesToPlans() {
        // Collect all plans in project
        for (var planId : planIds) {
            associateSuiteIdsToPlanId(planId);
        }
    }

    private static void associateSuiteIdsToPlanId(String planId) {
        List<String> temporarySuiteBucket = new ArrayList<>();
        // Collect all suites in each plan and push to
        List<Map<String, Object>> getSuitesList = i.amPerforming().restHttp().getCall(tfsUrl + "/test/plans/" + planId + "/suites?" + apiVersion, "Authorization", authType + " " + pat).jsonPath().getList("value");
        for (var suite : getSuitesList) {
            temporarySuiteBucket.add(suite.get("id").toString());
        }
        suitesToPlansMapping.put(planId, temporarySuiteBucket);
    }

    private static void mapTestPointsToTestRunAndTestCaseId() {
        for (String planId : suitesToPlansMapping.keySet()) {
            List<String> testPointIds = new ArrayList<>();
            Map<String, String> tempTestIdsToTestResultIdsMapping = new LinkedHashMap<>();
            for (String suiteId : suitesToPlansMapping.get(planId)) {
                // Fetch all test points for in question plan at the moment
                List<Map<String, Object>> testPoints = i.amPerforming().restHttp().getCall(tfsUrl + "/test/plans/" + planId + "/suites/" + suiteId + "/points?" + apiVersion, "Authorization", authType + " " + pat).jsonPath().getList("value");
                for (Map pointsInfo : testPoints) {
                    testPointIds.add(pointsInfo.get("id").toString());
                }
            }
            JsonArray testPointIdsContainer = new JsonArray();
            testPointIds.stream().forEach(testPoints -> testPointIdsContainer.add(testPoints));

            // create a run for all the test points
            var runId = i.amPerforming().restHttp().postCall(getTestRunCreationPayLoad(planId, testPointIdsContainer), null, tfsUrl + "/test/runs?" + apiVersion, "Authorization", authType + " " + pat, null).jsonPath().get("id").toString();
            runIds.add(runId);
            //get all test result ends against test cases
            List<Map<String, Object>> testResultsIds = i.amPerforming().restHttp().getCall(tfsUrl + "/test/Runs/" + runId + "/results?" + apiVersion, "Authorization", authType + " " + pat).jsonPath().getList("value");

            for (Map testResultInfo : testResultsIds) {
                Map<String, Object> testCaseInfo = (Map<String, Object>) testResultInfo.get("testCase");
                tempTestIdsToTestResultIdsMapping.put(testCaseInfo.get("id").toString(), testResultInfo.get("id").toString());
                testIdsToTestResultIdsMappingToRuns.put(runId, tempTestIdsToTestResultIdsMapping);
            }
        }
    }

    private static void setUpTestPlanIdsInfo() {
        List<Map<String, Object>> getAllTestPlanIds = i.amPerforming().restHttp().getCall(tfsUrl + "/test/plans?" + apiVersion, "Authorization", authType + " " + pat).jsonPath().getList("value");
        for (var plan : getAllTestPlanIds) {
            planIds.add(plan.get("id").toString());
        }
    }

    private static JsonObject getTestRunCreationPayLoad(String planId, JsonArray testPointId) {
        JsonObject mainContainer = new JsonObject();
        JsonObject plan = new JsonObject();
        plan.addProperty("id", planId);
        mainContainer.addProperty("name", "Rei-Fast " + EnvironmentFetcher.getScenarioTag() + " automated run for Test Plan:" + planId);
        mainContainer.add("plan", plan);
        mainContainer.add("pointIds", testPointId);
        mainContainer.addProperty("automated", false);
        return mainContainer;
    }
    /******************* End of Pre-Result Posting Configurations  **********************************/

    /*----------------------------------------------------------------------------------------------*/

    /******************* Setup Result Posting Configurations
     * @return***************************************/
    public static void addTestResultToTfs() {
        System.out.println("-------------------- TFS Result Posting Started -----------------------");
        postAndPatchTestResult();
        System.out.println("-------------------- TFS Result Posting Completed -----------------------");
    }

    private static void postAndPatchTestResult() {
        var runsToTestResultIdsAndResultsMapping = getTestResultsData();
        for (var runId : runsToTestResultIdsAndResultsMapping.keySet()) {
            JsonArray testRunResultPayLoadHolder = new JsonArray();
            var testResultIdsAndResultsMapping = runsToTestResultIdsAndResultsMapping.get(runId);
            for (var entrySet : testResultIdsAndResultsMapping.entrySet()) {
                testRunResultPayLoadHolder.add(getTestCaseResultPayload(entrySet.getKey(), entrySet.getValue()));
            }
            if (testRunResultPayLoadHolder.size() > 0) patchTestResult(testRunResultPayLoadHolder, runId);
        }
        patchRunsCompletion();
    }

    public static void patchRunsCompletion() {
        for (String runId : runIds) {
            JsonObject completeState = new JsonObject();
            completeState.addProperty("state", "Completed");
            i.amPerforming().restHttp().patchCall(completeState, null, tfsUrl + "/test/runs/" + runId + "?" + apiVersion, "Authorization", authType + " " + pat, null).jsonPath().prettyPrint();
        }
    }

    private static Map<String, Map<String, String>> getTestResultsData() {
        var executedTestResults = ReportingLogsPlugin.getTagResults();
        Map<String, Map<String, String>> resultIdsToResultMappingAgainstRuns = new LinkedHashMap<>();
        for (String runIds : testIdsToTestResultIdsMappingToRuns.keySet()) {
            var testIdsToTestResultIdsMapping = testIdsToTestResultIdsMappingToRuns.get(runIds);
            Map<String, String> tempResultIdsToResultMapping = new LinkedHashMap<>();
            for (var executedTestCaseIds : executedTestResults.keySet()) {
                if (testIdsToTestResultIdsMapping.containsKey(executedTestCaseIds)) {
                    tempResultIdsToResultMapping.put(testIdsToTestResultIdsMapping.get(executedTestCaseIds), executedTestResults.get(executedTestCaseIds) ? "Passed" : "Failed");
                }
            }
            resultIdsToResultMappingAgainstRuns.put(runIds, tempResultIdsToResultMapping);
        }
        return resultIdsToResultMappingAgainstRuns;
    }

    private static void patchTestResult(JsonArray testCaseResultPayLoadHolder, String testRun) {
        i.amPerforming().restHttp().patchCall(testCaseResultPayLoadHolder, tfsUrl + "/test/runs/" + testRun + "/results?" + apiVersion, "Authorization", authType + " " + pat, null).jsonPath().prettyPrint();
    }

    private static JsonObject getTestCaseResultPayload(String resultId, String result) {
        JsonObject resultHolder = new JsonObject();
        resultHolder.addProperty("id", resultId);
        resultHolder.addProperty("state", "Completed");
        resultHolder.addProperty("outcome", result);
        resultHolder.addProperty("comment", "Result posted by Rei - FAST :Refer to automation report for details & screenshot");
        return resultHolder;
    }


    /******************* Generic settings and Configurations  ***************************************/
    private static void loadTfsConfiguration() {
        tfsUrl = i.amPerforming().fileHandlingTo().json().getDataFromJson("tfs.json", "profile", "tfsUrl") + "/_apis";
        pat = i.amPerforming().fileHandlingTo().json().getDataFromJson("tfs.json", "profile", "pat");
        authType = i.amPerforming().fileHandlingTo().json().getDataFromJson("tfs.json", "profile", "authType");
        apiVersion = "api-version=" + i.amPerforming().fileHandlingTo().json().getDataFromJson("tfs.json", "profile", "apiVersion");
        loadTfsConfig = false;
    }
}
