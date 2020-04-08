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
package com.testingblaze.integrations;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.testingblaze.controller.ReportingLogsPlugin;
import com.testingblaze.http.RestfulWebServices;
import com.testingblaze.register.EnvironmentFactory;
import com.testingblaze.register.I;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The class is responsible for creating test run , adding all tests to runs, collect test results and post them to tfs, close runs
 * @parallel: Supports multi threading and multi jvm safe.
 */
public class Tfs {
    private volatile static String tfsUrl, pat, authType, apiVersion;
    private volatile static boolean loadTfsConfig = true;
    private static Map<String, List<String>> suitesToPlansMapping = new LinkedHashMap<>();
    private static List<String> planIds = new ArrayList<>();
    private static List<String> runIds = new ArrayList<>();
    private static JsonArray jsonRunIds = new JsonArray();
    private static Map<String, Map<String, String>> testIdsToTestResultIdsMappingToRuns = new LinkedHashMap<>();
    private static JsonObject jsonTestIdsToTestResultIdsMappingToRuns = new JsonObject();
    public volatile static FileChannel fileChannel;
    private volatile static Path tfsTestsMappingFile;
    private volatile static boolean setThreadBasedConfiguration = false;
    private static RestfulWebServices tfsApiCalls;

    public Tfs() {
        if (tfsApiCalls == null) tfsApiCalls = new RestfulWebServices();
        if (loadTfsConfig) {
            triggerMandatoryClosureJobs();
            System.out.println("-------------------- TFS Result Post Configuration Started -----------------------");
            if (System.getProperty("threads") != null && !(System.getProperty("threads").equals("0") || System.getProperty("threads").equals("1"))) {
                createMultiJvmConfiguration();
                System.out.println("Running........");
            } else {
                loadTfsConfiguration();
                System.out.println("Running........");
                setUpInitialConfiguration();
            }
            System.out.println("-------------------- TFS Result Post Configuration Completed -----------------------");
        }
    }

    /******************* Set up Pre-Result Posting Configurations  **********************************/

    /**
     * Evaluates multi jvm execution and control run creation in parallel execution
     *
     * @author: nauman.shahid
     */
    private static void createMultiJvmConfiguration() {
        // The try block handles jvm that first approaches and is eligible to create the test runs
        setThreadBasedConfiguration = true;
        Path path = Paths.get(EnvironmentFactory.getProjectPath() + "/target");
        try {
            if (Files.list(path).noneMatch(file -> file.startsWith("tfs_tests_mapping"))) {
                Files.createFile(Paths.get(path + "/tfs_tests_mapping.json"));
                tfsTestsMappingFile = Paths.get(EnvironmentFactory.getProjectPath() + "/target/tfs_tests_mapping.json");
                fileChannel = FileChannel.open(tfsTestsMappingFile, StandardOpenOption.WRITE,
                        StandardOpenOption.APPEND);
                fileChannel.lock();
                loadTfsConfiguration();
                setUpInitialConfiguration();
                JsonObject jsonData = getDataFromJson(tfsTestsMappingFile).getAsJsonObject();
                runIds = new Gson().fromJson(jsonData.get("runIds").getAsJsonArray().getAsJsonArray(), new TypeToken<List<String>>() {
                }.getType());
                testIdsToTestResultIdsMappingToRuns = new Gson().fromJson(jsonData.get("resultMap"), new TypeToken<Map<String, Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            // This catch is executed in case test runs are already created by some other jvm
            try {
                Thread.sleep(60000);
            } catch (InterruptedException exx) {

            }
            try {
                tfsTestsMappingFile = Paths.get(EnvironmentFactory.getProjectPath() + "/target/tfs_tests_mapping.json");
            } catch (Exception ex) {
                try {
                    Thread.sleep(30000);
                    tfsTestsMappingFile = Paths.get(EnvironmentFactory.getProjectPath() + "/target/tfs_tests_mapping.json");
                } catch (InterruptedException exx) {

                }

            }

            loadTfsConfiguration();
            JsonObject jsonData = getDataFromJson(tfsTestsMappingFile).getAsJsonObject();
            runIds = new Gson().fromJson(jsonData.get("runIds").getAsJsonArray().getAsJsonArray(), new TypeToken<List<String>>() {
            }.getType());
            testIdsToTestResultIdsMappingToRuns = new Gson().fromJson(jsonData.get("resultMap"), new TypeToken<Map<String, Map<String, String>>>() {
            }.getType());

        }

    }

    /**
     * setup all mapping of test plans, suites , runs and test cases
     *
     * @author: nauman.shahid
     */
    private static void setUpInitialConfiguration() {
        setUpTestPlanIdsInfo();
        mapSuitesToPlans();
        mapTestPointsToTestRunAndTestCaseId();
    }

    /**
     * get all plan ids
     *
     * @authour: nauman.shahid
     */
    private static void setUpTestPlanIdsInfo() {
        List<Map<String, Object>> getAllTestPlanIds = tfsApiCalls.getCall(tfsUrl + "/test/plans?" + apiVersion, "Authorization", authType + " " + pat).jsonPath().getList("value");
        for (var plan : getAllTestPlanIds) {
            planIds.add(plan.get("id").toString());
        }
    }

    /**
     * maps suites to their corresponding plans
     *
     * @calls: associateSuiteIdsToPlanId()
     * @author: nauman.shahid
     */
    private static void mapSuitesToPlans() {
        // Collect all plans in project
        for (var planId : planIds) {
            associateSuiteIdsToPlanId(planId);
        }
    }

    private static void associateSuiteIdsToPlanId(String planId) {
        List<String> temporarySuiteBucket = new ArrayList<>();
        // Collect all suites in each plan and push to
        List<Map<String, Object>> getSuitesList = tfsApiCalls.getCall(tfsUrl + "/test/plans/" + planId + "/suites?" + apiVersion, "Authorization", authType + " " + pat).jsonPath().getList("value");
        for (var suite : getSuitesList) {
            temporarySuiteBucket.add(suite.get("id").toString());
        }
        suitesToPlansMapping.put(planId, temporarySuiteBucket);
    }

    /**
     * mapping of test points,cases to corresponding runs
     *
     * @call: getTestRunCreationPayLoad()
     * @author: nauman.shahid
     */
    private static void mapTestPointsToTestRunAndTestCaseId() {
        JsonObject runIdsJsonArray_jsonTestIdsToTestResultIdsMappingToRuns_holder = new JsonObject();
        for (String planId : suitesToPlansMapping.keySet()) {
            List<String> testPointIds = new ArrayList<>();
            Map<String, String> tempTestIdsToTestResultIdsMapping = new LinkedHashMap<>();
            JsonObject tempJsonTestIdsToTestResultIdsMapping = new JsonObject();
            for (String suiteId : suitesToPlansMapping.get(planId)) {
                // Fetch all test points for in question plan at the moment
                List<Map<String, Object>> testPoints = tfsApiCalls.getCall(tfsUrl + "/test/plans/" + planId + "/suites/" + suiteId + "/points?" + apiVersion, "Authorization", authType + " " + pat).jsonPath().getList("value");
                for (Map pointsInfo : testPoints) {
                    testPointIds.add(pointsInfo.get("id").toString());
                }
            }
            JsonArray testPointIdsContainer = new JsonArray();
            testPointIds.stream().forEach(testPoints -> testPointIdsContainer.add(testPoints));

            // create a run for all the test points
            var runId = tfsApiCalls.postCall(getTestRunCreationPayLoad(planId, testPointIdsContainer), null, tfsUrl + "/test/runs?" + apiVersion, "Authorization", authType + " " + pat, null).jsonPath().get("id").toString();
            runIds.add(runId);
            if (setThreadBasedConfiguration) jsonRunIds.add(runId);
            //get all test result ends against test cases
            List<List<Map<String, Object>>> listTestResultsIds = handleTestResultIds(runId);

            for (var testResultsIds : listTestResultsIds) {
                for (Map testResultInfo : testResultsIds) {
                    Map<String, Object> testCaseInfo = (Map<String, Object>) testResultInfo.get("testCase");
                    tempTestIdsToTestResultIdsMapping.put(testCaseInfo.get("id").toString(), testResultInfo.get("id").toString());
                    if (setThreadBasedConfiguration)
                        tempJsonTestIdsToTestResultIdsMapping.addProperty(testCaseInfo.get("id").toString(), testResultInfo.get("id").toString());
                    testIdsToTestResultIdsMappingToRuns.put(runId, tempTestIdsToTestResultIdsMapping);
                    if (setThreadBasedConfiguration)
                        jsonTestIdsToTestResultIdsMappingToRuns.add(runId, tempJsonTestIdsToTestResultIdsMapping);
                }
            }
        }
        if (setThreadBasedConfiguration)
            runIdsJsonArray_jsonTestIdsToTestResultIdsMappingToRuns_holder.add("runIds", jsonRunIds);
        if (setThreadBasedConfiguration)
            runIdsJsonArray_jsonTestIdsToTestResultIdsMappingToRuns_holder.add("resultMap", jsonTestIdsToTestResultIdsMappingToRuns);
        if (setThreadBasedConfiguration)
            I.amPerforming().fileHandling().toWriteInFile(tfsTestsMappingFile.toString(), new Gson().toJson(runIdsJsonArray_jsonTestIdsToTestResultIdsMappingToRuns_holder));
    }

    /**
     * handles fetching all test result ids form tfs. Special ahndling where record exceeds 1000
     * @param runId
     * @return
     * @author nauman.shahid
     */
    private static List<List<Map<String, Object>>> handleTestResultIds(String runId) {
        List<List<Map<String, Object>>> listTestResultsIds = new ArrayList<>();
        Boolean fetchingResultIds = true;
        int count = 0;
        int limitRetryApi =0;
        var initialTestResultPayload = tfsApiCalls.getCall(tfsUrl + "/test/Runs/" + runId + "/results?" + apiVersion, "Authorization", authType + " " + pat).jsonPath();

        while (fetchingResultIds) {
            limitRetryApi++;
            String recordCount = initialTestResultPayload.get("count").toString();
            List<Map<String, Object>> records = initialTestResultPayload.getList("value");

            if (!recordCount.equalsIgnoreCase("1000") && count == 0) {
                listTestResultsIds.add(initialTestResultPayload.getList("value"));
                return listTestResultsIds;
            } else if (recordCount.equalsIgnoreCase("1000")) {
                if (records.get(0).get("id").toString().endsWith("000")) {
                    listTestResultsIds.add(initialTestResultPayload.get("value"));
                    count++;
                    initialTestResultPayload = tfsApiCalls.getCall(tfsUrl + "/test/Runs/" + runId + "/results?$skip=" + (1000 * count) + "&" + apiVersion, "Authorization", authType + " " + pat).jsonPath();
                } else if (!records.get(0).get("id").toString().endsWith("000")) {
                    initialTestResultPayload = tfsApiCalls.getCall(tfsUrl + "/test/Runs/" + runId + "/results?$skip=" + (1000 * count) + "&" + apiVersion, "Authorization", authType + " " + pat).jsonPath();
                }
            } else if (!recordCount.equalsIgnoreCase("1000") && count > 0) {
                if(records.get(0).get("id").toString().endsWith("000")) {
                    listTestResultsIds.add(initialTestResultPayload.get("value"));
                    fetchingResultIds = false;
                } else {
                    initialTestResultPayload = tfsApiCalls.getCall(tfsUrl + "/test/Runs/" + runId + "/results?$skip=" + (1000 * count) + "&" + apiVersion, "Authorization", authType + " " + pat).jsonPath();
                }
            }
            if (limitRetryApi == 20) break;
        }
        return listTestResultsIds;
    }

    private static JsonObject getTestRunCreationPayLoad(String planId, JsonArray testPointId) {
        JsonObject mainContainer = new JsonObject();
        JsonObject plan = new JsonObject();
        plan.addProperty("id", planId);
        mainContainer.addProperty("name", EnvironmentFactory.getOrgName()+"-Test Blaze " + EnvironmentFactory.getScenarioTag() + " automated run for Test Plan:" + planId);
        mainContainer.add("plan", plan);
        mainContainer.add("pointIds", testPointId);
        mainContainer.addProperty("automated", false);
        return mainContainer;
    }
    /******************* End of Pre-Result Posting Configurations  **********************************/

    /*----------------------------------------------------------------------------------------------*/

    /******************* Setup Result Posting Configurations   **********************************/


    /**
     * main call to perform related posting and patching
     *
     * @author: nauman.shahid
     */
    public static void addTestResultToTfs() {
        System.out.println("-------------------- TFS Result Posting Started -----------------------");
        postAndPatchTestResult();
        System.out.println("-------------------- TFS Result Posting Completed -----------------------");
    }

    /**
     * post all test results , update run completion
     *
     * @call getTestResultsData() , patchRunsCompletion()
     * @author nauman.shahid
     */
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

    /**
     * patch the runs status
     *
     * @author nauman.shahid
     */
    private static void patchRunsCompletion() {
        for (String runId : runIds) {
            JsonObject completeState = new JsonObject();
            completeState.addProperty("state", "Completed");
            tfsApiCalls.patchCall(completeState, null, tfsUrl + "/test/runs/" + runId + "?" + apiVersion, "Authorization", authType + " " + pat, null);
        }

        if (System.getProperty("threads") == null || ("0".equals(System.getProperty("threads")) || ("1".equals(System.getProperty("threads"))))){
            for (String runId : runIds) {
                JsonObject completeState = new JsonObject();
                completeState.addProperty("state", "Completed");
                tfsApiCalls.patchCall(completeState, null, tfsUrl + "/test/runs/" + runId + "?" + apiVersion, "Authorization", authType + " " + pat, null);
            }
        }
    }

    /**
     * map the results to the result ids against test runs
     *
     * @return resultIdsToResultMappingAgainstRuns
     * @authour nauman.shahid
     */
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
            if (tempResultIdsToResultMapping.size() > 0)  resultIdsToResultMappingAgainstRuns.put(runIds, tempResultIdsToResultMapping);
        }
        return resultIdsToResultMappingAgainstRuns;
    }

    /**
     * patch test results
     *
     * @param testCaseResultPayLoadHolder
     * @param testRun
     * @author nauman.shahid
     */
    private static void patchTestResult(JsonArray testCaseResultPayLoadHolder, String testRun) {
        tfsApiCalls.patchCall(testCaseResultPayLoadHolder, tfsUrl + "/test/runs/" + testRun + "/results?" + apiVersion, "Authorization", authType + " " + pat, null);
    }

    private static JsonObject getTestCaseResultPayload(String resultId, String result) {
        JsonObject resultHolder = new JsonObject();
        resultHolder.addProperty("id", resultId);
        resultHolder.addProperty("state", "Completed");
        resultHolder.addProperty("outcome", result);
        resultHolder.addProperty("comment", "Result posted by Test Blaze :Refer to automation report for details & screenshot");
        return resultHolder;
    }


    /******************* Generic settings and Configurations  ***************************************/

    /**
     * manages the credentials and tokens of the related project
     */
    private static void loadTfsConfiguration() {
        tfsUrl = I.amPerforming().fileHandling().forJsonAnd().getDataFromJson("tfs.json", "profile", "tfsUrl") + "/_apis";
        pat = I.amPerforming().fileHandling().forJsonAnd().getDataFromJson("tfs.json", "profile", "pat");
        authType = I.amPerforming().fileHandling().forJsonAnd().getDataFromJson("tfs.json", "profile", "authType");
        apiVersion = "api-version=" + I.amPerforming().fileHandling().forJsonAnd().getDataFromJson("tfs.json", "profile", "apiVersion");
        loadTfsConfig = false;
    }

    /**
     * executes second half of execution where result posting is performed
     *
     * @@author nauman.shahid
     */
    private void triggerMandatoryClosureJobs() {
        Thread performClosureActivities = new Thread(() -> {
            addTestResultToTfs();
            try {
                fileChannel.close();
            } catch (Exception e) {

            }
        });
        Runtime.getRuntime().addShutdownHook(performClosureActivities);
    }

    private static JsonElement getDataFromJson(Path filePath) {
        String reader = null;
        try {
            reader = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var jsonParser = new JsonParser();
        // getting the root element - the whole set of json data
        JsonElement rootElement = jsonParser.parse(reader);
        // covert it to the json object to access different child objects and arrays
        return rootElement.getAsJsonObject();
    }
}