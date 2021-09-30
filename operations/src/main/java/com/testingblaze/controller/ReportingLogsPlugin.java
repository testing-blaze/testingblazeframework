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
package com.testingblaze.controller;

import com.testingblaze.misclib.ConsoleFormatter;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.DataTableArgument;
import io.cucumber.plugin.event.DocStringArgument;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.HookTestStep;
import io.cucumber.plugin.event.HookType;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import org.assertj.core.api.AbstractSoftAssertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.testingblaze.misclib.ConsoleFormatter.COLOR.*;
import static com.testingblaze.misclib.ConsoleFormatter.setTextColor;

/**
 * A plugin to control the reporting logs on console and html (Executing step and information about soft assert failures only)
 * <p>
 * In particular, it takes care of following:
 * <ol>
 *   <li>Prints the names of steps before they run, rather than after.</li>
 *   <li>Prints soft assertion errors in teh same step they occur, for easier debugging.</li>
 *   <li>If System parameter 'softAssertScreenshot' is 'true', embeds a screenshot in the report when soft asserts fail.</li>
 * </ol>
 *
 * @author john.philips
 */
public class ReportingLogsPlugin implements ConcurrentEventListener {
    private static final String initialPhrase = "Running Step: ";
    private static final String padding = " ".repeat(initialPhrase.length() + 2);
    private static final List<Throwable> allErrorsFromEntireScenario = new ArrayList<>();
    private static final List<Throwable> allErrorsFromLastCompletedStep = new ArrayList<>();
    private static final List<String> parsedFileNames = new ArrayList<>();
    private static final JsonParser parser = new JsonParser();
    private static final Pattern tfsTag = Pattern.compile("^@[0-9]+$");
    private static final Map<String, Boolean> mapTagToResult = new HashMap<>();

    private static List<String> scenarioTags;
    private static Integer currentTag;


    /**
     * This method sets up when each of the helper methods should run,
     * i.e. when a test step starts or ends, or text is written to the report.
     *
     * @param publisher Automatically provided
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, event -> {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                savePreviousErrorsBeforeStep();
                if (!ScenarioController.getScenario().isFailed()) {
                    printPickleStep((PickleStepTestStep) event.getTestStep());
                }
            } else if (event.getTestStep() instanceof HookTestStep) {
                if (((HookTestStep) event.getTestStep()).getHookType() == HookType.AFTER) {
                    savePreviousErrorsBeforeStep();
                }
            }
        });

        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                printNewErrorsAfterStep();
                updateTagsMapping((PickleStepTestStep) event.getTestStep());
            }
        });

        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            scenarioTags = null;
            markStepsFailedFromSoftAsserts();
        });
    }

    /**
     * Saves a list of all soft assertions that were already present before a step runs.
     * This ensures that error messages are not printed twice.
     */
    private void savePreviousErrorsBeforeStep() {
        allErrorsFromEntireScenario.clear();
        allErrorsFromEntireScenario.addAll(getErrors());
    }

    /**
     * This method prints to the console any soft assertions that failed during the current step.
     * If desired, the System parameter 'softAssertScreenshot' can be set to 'true' to also
     * embed a screenshot of the web page in the report on failures.
     */
    private void printNewErrorsAfterStep() {
        allErrorsFromLastCompletedStep.clear();
        allErrorsFromLastCompletedStep.addAll(getErrors().stream().filter(el -> !allErrorsFromEntireScenario.contains(el)).collect(Collectors.toList()));
        if (allErrorsFromLastCompletedStep.size() > 0) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR, "The following soft assertions failed during the step:\n" + String.join("\n", getErrorsFromPreviousStep()));

            if ("true".equalsIgnoreCase(System.getProperty("softAssertScreenshot"))) {
                try {
                    ScenarioController.getScenario().embed(((TakesScreenshot) InstanceRecording.getInstance(DeviceBucket.class).getDriver()).getScreenshotAs(OutputType.BYTES), "image/png", ScenarioController.getScenario().getSourceTagNames().toString());
                } catch (Exception e) {

                }
                }
        }
    }

    private void updateTagsMapping(PickleStepTestStep step) {
        if (scenarioTags == null) {
            scenarioTags = ScenarioController.getScenario().getSourceTagNames().stream().filter(el -> tfsTag.matcher(el).matches()).collect(Collectors.toList());
            currentTag = 0;
        }

        if (step.getStep().getKeyWord().toLowerCase().contains("then") && currentTag < scenarioTags.size()) {
            mapTagToResult.put(scenarioTags.get(currentTag).substring(1), !ScenarioController.getScenario().isFailed() && allErrorsFromLastCompletedStep.size() == 0);
            currentTag++;
        }
    }

    /**
     * This method will print each step to the console as it begins.
     * It will also print any DataTables or DocStrings included with the step
     * <p>
     * It will not print steps after the scenario has failed.
     *
     * @param testStep The currently running step
     */
    private void printPickleStep(final PickleStepTestStep testStep) {
        System.out.println();
        System.out.println(setTextColor(BRIGHT_GREEN, initialPhrase) + testStep.getStep().getText());
        if (testStep.getStep().getArgument() instanceof DataTableArgument) {
            printDataTable((DataTableArgument) testStep.getStep().getArgument());
        } else if (testStep.getStep().getArgument() instanceof DocStringArgument) {
            printPickleString((DocStringArgument) testStep.getStep().getArgument());
        }
        System.out.println();
    }

    /**
     * This helper method prints a DataTable argument to the console.
     *
     * @param table The table to print.
     */
    private void printDataTable(final DataTableArgument table) {
        Map<Integer, Integer> maxCellLengths = new HashMap<>();
        for (int i = 0; i < table.cells().size(); i++) {
            for (int j = 0; j < table.cells().get(i).size(); j++) {
                maxCellLengths.putIfAbsent(j, 0);

                if (table.cells().get(i).get(j).length() > maxCellLengths.get(j)) {
                    maxCellLengths.put(j, table.cells().get(i).get(j).length());
                }
            }
        }

        for (int i = 0; i < table.cells().size(); i++) {
            StringBuilder rowString = new StringBuilder();
            rowString.append(padding).append("|");
            for (int j = 0; j < table.cells().get(i).size(); j++) {
                rowString.append(String.format(" %s%s |", table.cells().get(i).get(j),
                        " ".repeat(maxCellLengths.get(j) - table.cells().get(i).get(j).length())));
            }
            System.out.println(rowString.toString());
        }
    }

    /**
     * This helper method prints a DocString argument to the console.
     *
     * @param argString The DocString to print.
     */
    private void printPickleString(final DocStringArgument argString) {
        System.out.println(padding + "\"\"\"");
        System.out.println(padding + argString.getContent().replaceAll("\\n", "\n" + padding));
        System.out.println(padding + "\"\"\"");
    }

    private void markStepsFailedFromSoftAsserts() {
        String directoryName = System.getProperty("user.dir") + "/target/cucumber-report";
        List<String> newFiles = Arrays.stream(Objects.requireNonNull(new File(directoryName).list()))
                .filter(el -> !parsedFileNames.contains(el)).collect(Collectors.toList());

        for (String fileName : newFiles) {
            String fileLocation = directoryName + "/" + fileName;
            try {
                JsonArray objects = parser.parse(new InputStreamReader(new FileInputStream(fileLocation), StandardCharsets.UTF_8)).getAsJsonArray();
                for (JsonElement object : objects) {
                    for (JsonElement element : object.getAsJsonObject().get("elements").getAsJsonArray()) {
                        for (JsonElement step : element.getAsJsonObject().get("steps").getAsJsonArray()) {
                            if (step.getAsJsonObject().get("output") != null) {
                                for (JsonElement output : step.getAsJsonObject().get("output").getAsJsonArray()) {
                                    if (output.getAsString().contains("The following soft assertions failed during the step:")) {
                                        step.getAsJsonObject().get("result").getAsJsonObject().addProperty("status", "failed");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                Files.write(Paths.get(fileLocation), List.of(objects.toString()));
            } catch (Exception e) {
            }
        }
        parsedFileNames.addAll(newFiles);
    }

    /**
     * Gets the current list of errors that have been thrown in the execution.
     * <p>
     * This method is needed because of a bug in the normal SoftAssertions.errorsCollected() method.
     * It adds the error message line number to the returned errors every time it is called.
     * So if it is called multiple times the line number is repeated over and over, which is not ideal.
     *
     * @return List of the soft assertion errors that have been thrown
     */
    private List<Throwable> getErrors() {

        try {
            @SuppressWarnings("rawtypes")
            Class softProxy = Class.forName("org.assertj.core.api.SoftProxies");

            @SuppressWarnings("unchecked")
            Method getErrors = softProxy.getDeclaredMethod("errorsCollected");
            getErrors.setAccessible(true);


            Field proxies = AbstractSoftAssertions.class.getDeclaredField("proxies");
            proxies.setAccessible(true);

            @SuppressWarnings("unchecked")
            List<Throwable> toReturn = (List<Throwable>) getErrors.invoke(proxies.get(I.amPerforming().assertionsTo()));

            return toReturn;

        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                | IllegalAccessException | InvocationTargetException e) {
            System.out.println(ConsoleFormatter.setTextColor(RED, "Error while getting errors: " + e.getMessage()));
        }

        return new ArrayList<>();
    }

    public static List<String> getErrorsFromPreviousStep() {
        List<String> toReturn = new ArrayList<>();
        for (int i = 0; i < allErrorsFromLastCompletedStep.size(); i++) {
            toReturn.add((i + 1) + ") " + ConsoleFormatter.setTextColor(BRIGHT_RED, allErrorsFromLastCompletedStep.get(i).getMessage()));
        }
        return toReturn;
    }

    public static List<String> getErrorsFromScenario() {
        List<String> toReturn = new ArrayList<>();
        for (int i = 0; i < allErrorsFromEntireScenario.size(); i++) {
            toReturn.add((i + 1) + ") " + allErrorsFromEntireScenario.get(i).getMessage());
        }
        return toReturn;
    }

    public static Map<String, Boolean> getTagResults() {
        System.out.println("map of results is   "+mapTagToResult);
        return mapTagToResult;
    }

}
