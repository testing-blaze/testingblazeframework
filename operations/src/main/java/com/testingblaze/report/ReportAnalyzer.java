package com.testingblaze.report;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.testingblaze.http.RestfulWebServices;
import com.testingblaze.objects.TestStatusDetails;
import com.testingblaze.register.EnvironmentFactory;
import io.cucumber.core.gherkin.vintage.internal.gherkin.deps.com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReportAnalyzer {
    private static final JsonParser parser = new JsonParser();
    public static Boolean isReportPublished=false;
    Path pathAnalysis = Paths.get(getReportGenerationPath() + "/ReportAnalysis");
    Path pathFiles = Paths.get(getReportGenerationPath() + "/ReportAnalysis/Files");
    private static List<TestStatusDetails> testStatusDetails;
    private static final String mainHTMLHeader = "<html>\n" +
            "  <head>\n" +
            "    <title>Test Automation Analysis</title>\n";
    private static final Pattern scenarioTagPattern= Pattern.compile("^[0-9]{3}");

    private static final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    public void executeAnalysis() throws IOException {
        reportConfigWriteUp();
        Files.write(Paths.get(pathAnalysis + "/analysis.html"), createMainHtmlPage(compileReport()));
        Files.write(Paths.get(pathFiles + "/bugs_details.html"), createBugDetailsHtmlPage(compileReport()));
        Files.write(Paths.get(pathFiles + "/updating_details.html"), createUpdatingDetailsHtmlPage(compileReport()));
    }

    public void publishReportAnalytics() throws IOException {
        if(isReportPublished) return;
        if (System.getProperty("publishReport") != null && System.getProperty("publishReport").equalsIgnoreCase("yes")) {
            Properties OR = new Properties();
            System.out.println("********* - Report Publishing Started ....!");
            OR.load(new InputStreamReader(getClass().getResourceAsStream("/report_publisher.properties"), StandardCharsets.UTF_8));
            RestfulWebServices restfulWebServices = new RestfulWebServices();
            restfulWebServices.isJvmHookOn = true;
            String endPoint = OR.getProperty("endPoint");
            restfulWebServices.postCall(new ReportAnalyzer().getReportJson(), null, endPoint, null, null, null);
            restfulWebServices.isJvmHookOn = false;
            System.out.println(".... Report Publishing Completed - *********");
        }
    }

    public JsonObject getReportJson() throws IOException {
        JsonObject dataSet = new JsonObject();
        String data = new Gson().toJson(compileReport());
        JsonParser parser = new JsonParser();
        JsonElement jsonReportData = parser.parse(data);
        getPropertiesAccess().load(new InputStreamReader(getClass().getResourceAsStream("/report_publisher.properties"), StandardCharsets.UTF_8));
        dataSet.addProperty("projectId", getPropertiesAccess().getProperty("projectId"));
        dataSet.addProperty("projectName",getPropertiesAccess().getProperty("projectName"));
        dataSet.addProperty("date", EnvironmentFactory.getExecutionDate());
        dataSet.addProperty("totalTestsCount",getTotalTestCount());
        dataSet.addProperty("envName",EnvironmentFactory.getEnvironmentName());
        dataSet.add("reportData",jsonReportData);
        return dataSet;
    }
    Properties OR;
    private Properties getPropertiesAccess(){
        if(OR==null){
            OR = new Properties();
        }
        return OR;
    }

    private void reportConfigWriteUp() {
        try {
            if (Files.notExists(pathAnalysis)) {
                Files.createDirectories(pathAnalysis);
                Files.createDirectories(pathFiles);
            } else {
                System.out.println("Report Analysis folder already exist. Consider removing it");
            }

            if (Files.list(pathFiles).noneMatch(file -> file.startsWith("styles.css"))) {
                Files.createFile(Paths.get(pathFiles + "/styles.css"));
                Files.write(Paths.get(pathFiles + "/styles.css"), cssAnalyzerHtml());
            }
            if (Files.list(pathAnalysis).noneMatch(file -> file.startsWith("analysis"))) {
                Files.createFile(Paths.get(pathAnalysis + "/analysis.html"));
                Files.createFile(Paths.get(pathFiles + "/updating_details.html"));
                Files.createFile(Paths.get(pathFiles + "/bugs_details.html"));
            }

        } catch (IOException e) {
        }
    }


    private Map<String, Map<String, List<TestStatusDetails>>> compileReport() throws IOException {
        Map<String, Map<String, List<TestStatusDetails>>> mainTableContainer = new TreeMap<>();
        Set<String> files = new HashSet();
        String tagName = "None";
        int assignedNumber = 0;
        String directoryName = getReportSourcePath();
        List<String> newFiles = Arrays.stream(Objects.requireNonNull(new File(directoryName).list())).collect(Collectors.toList());

        for (String fileName : newFiles) {
            if (!fileName.equalsIgnoreCase(".DS_Store"))
                files.add(fileName.split("_")[0]);
        }
        int i = 1;
        for (String specificFileName : files) {
            List<String> specificFiles = Arrays.stream(Objects.requireNonNull(new File(directoryName).list()))
                    .filter(el -> el.split("_")[0].equalsIgnoreCase(specificFileName)).collect(Collectors.toList());

            Map<String, List<TestStatusDetails>> reportData = new TreeMap<>();
            for (String fileName : specificFiles) {
                assignedNumber++;
                String fileLocation = directoryName + "/" + fileName;
                JsonArray objects = null;
                try {
                    objects = parser.parse(new InputStreamReader(new FileInputStream(fileLocation), StandardCharsets.UTF_8)).getAsJsonArray();
                } catch (Exception e) {
                    System.out.println("There was a problem in the report file " + fileName);
                    continue;
                }

                testStatusDetails = new ArrayList<>();

                for (JsonElement object : objects) {

                    for (JsonElement element : object.getAsJsonObject().get("elements").getAsJsonArray()) {
                        Boolean parentTag = true;
                        JsonArray fetchTAG = element.getAsJsonObject().get("tags").getAsJsonArray();

                        List<String> tagsHolder = new ArrayList<>();
                        for (JsonElement testTag : fetchTAG) {
                            String data = testTag.getAsJsonObject().get("name").getAsString();
                            if (parentTag && scenarioTagPattern.matcher(data.substring(1)).find()) {
                                tagName = data;
                                tagsHolder.add(data);
                                parentTag = false;
                            } else if (scenarioTagPattern.matcher(data.substring(1)).find()) {
                                tagsHolder.add(data);
                            }
                        }
                        int tag = 0;
                        for (JsonElement step : element.getAsJsonObject().get("steps").getAsJsonArray()) {
                            var keyword = step.getAsJsonObject().get("keyword").getAsString();
                            var stepName = step.getAsJsonObject().get("name").getAsString();
                            var result = step.getAsJsonObject().get("result").getAsJsonObject().get("status").getAsString();

                            if (StringUtils.containsIgnoreCase(keyword, "Then") && StringUtils.containsIgnoreCase(result, "failed")) {
                                if (tagsHolder.size() > tag) {
                                    testStatusDetails.add(new TestStatusDetails("Bug", stepName, tagsHolder.get(tag)));
                                } else {
                                    testStatusDetails.add(new TestStatusDetails("Bug", stepName, "None"));
                                }
                                tag++;

                            } else if (StringUtils.containsIgnoreCase(keyword, "Then") && StringUtils.containsIgnoreCase(result, "passed")) {
                                if (tagsHolder.size() > tag) {
                                    testStatusDetails.add(new TestStatusDetails("Passed", stepName, tagsHolder.get(tag)));
                                } else {
                                    testStatusDetails.add(new TestStatusDetails("Passed", stepName, "None"));
                                }
                                tag++;
                            } else if (StringUtils.containsIgnoreCase(keyword, "Then") && StringUtils.containsIgnoreCase(result, "skipped")) {
                            if (tagsHolder.size() > tag) {
                                testStatusDetails.add(new TestStatusDetails("Skipped", stepName, tagsHolder.get(tag)));
                            } else {
                                testStatusDetails.add(new TestStatusDetails("Skipped", stepName, "None"));
                            }
                            tag++;
                        } else if (StringUtils.containsIgnoreCase(result, "failed")) {
                                testStatusDetails.add(new TestStatusDetails("UI Change or Blocker", stepName, "None"));
                            }

                        }

                    }
                }
                if (reportData.containsKey(tagName))
                    tagName = tagName + "-Ex-" + assignedNumber;
                reportData.put(tagName, testStatusDetails);
                tagName = "No Tag" + assignedNumber;
            }
            if (mainTableContainer.containsKey(specificFileName))
                mainTableContainer.put(specificFileName + assignedNumber, reportData);
            else {
                mainTableContainer.put(specificFileName, reportData);
            }

        }
        return mainTableContainer;
    }

    private List<String> createUpdatingDetailsHtmlPage(Map<String, Map<String, List<TestStatusDetails>>> mainTableData) {
        String htmlHeader = getUpdatingPageHeaderContent() + "</head><body>";
        String tableHeader =
                "<h4 style=\"background-color:yellow;text-align: center;\">Test Updating / Blockers (TFS Test Tags) </h4>" +

                        "<div class=\"table-wrapper\">" +
                        projectInfoHeader() +
                        "<table class=\"fl-table\">" +
                        "<thead>" +
                        "<tr>" +
                        "<th>Feature</th>" +
                        "<th>Main Tag</th>" +
                        "<th>Related Tag</th>" +
                        "<th>Update Point</th>" +
                        "</tr>" +
                        "</thead>";
        String tableContent = "<tbody>";

        for (String key : mainTableData.keySet()) {
            for (String key2 : mainTableData.get(key).keySet()) {
                for (TestStatusDetails obj : mainTableData.get(key).get(key2)) {
                    if (obj.getStatus().contains("Blocker")) {
                        tableContent += "<tr>" +
                                "<td>" + key + "</td >" +
                                "<td ><a href=\"\">" + key2 + "</a></td >" +
                                "<td ><a href=\"\">" + obj.getTag() + "</a></td >" +
                                "<td >" + obj.getDetails() + "</td >" +
                                "</tr >";
                    }

                }
            }

        }
        tableContent += "<tbody>" +
                "</table>" +
                "</div>" +
                "</body>";

        return List.of(htmlHeader, tableHeader, tableContent);
    }


    private List<String> createBugDetailsHtmlPage(Map<String, Map<String, List<TestStatusDetails>>> mainTableData) {
        String htmlHeader = getBugPageHeaderContent() + "</head><body>";
        String tableHeader = "<h4 style=\"background-color:Pink;text-align: center;\">Bugs (TFS Test Tags) </h4>" +
                "<div class=\"table-wrapper\">" +
                projectInfoHeader() +
                "<table class=\"fl-table\">" +
                "<thead>" +
                "<tr>" +
                "<th>Feature</th>" +
                "<th>Main Tag</th>" +
                "<th>Related Tag</th>" +
                "<th>Bug Point</th>" +
                "</tr>" +
                "</thead>";
        String tableContent = "<tbody>";

        for (String key : mainTableData.keySet()) {
            for (String key2 : mainTableData.get(key).keySet()) {
                for (TestStatusDetails obj : mainTableData.get(key).get(key2)) {
                    if (obj.getStatus().contains("Bug")) {
                        tableContent += "<tr>" +
                                "<td>" + key + "</td >" +
                                "<td ><a href=\"\">" + key2 + "</a></td >" +
                                "<td ><a href=\"\">" + obj.getTag() + "</a></td >" +
                                "<td >" + obj.getDetails() + "</td >" +
                                "</tr >";
                    }

                }
            }

        }
        tableContent += "<tbody>" +
                "</table>" +
                "</div>" +
                "</body>";

        return List.of(htmlHeader, tableHeader, tableContent);
    }


    private List<String> createMainHtmlPage(Map<String, Map<String, List<TestStatusDetails>>> mainTableData) {
        int pass = 0, bug = 0, updating =0, skipped = 0;
        int tPass = 0, tBug = 0, tUpdating = 0, tSkipped=0;

        String tableHeader = "<div class=\"table-wrapper\">" +
                "<table class=\"fl-table\">" +
                "<p style=\"font-style: italic;font-size:11px;\">[Detected Bugs]: Bugs Detected By Automated Analysis. To Be Reproduced. [Skipped Tests]: Tests Skipped Due To Blocker/Updating.</p>\n"+
                "<thead>" +
                "<tr>" +
                "<th>Module</th>" +
                "<th>Passed</th>" +
                "<th>Bugs</th>" +
                "<th>Blocker/Updating</th>" +
                "<th>Skipped Tests</th>" +
                "<th>Health</th>" +
                "</tr>" +
                "</thead>";
        String tableContent = "<tbody>";

        for (String key : mainTableData.keySet()) {
            for (String key2 : mainTableData.get(key).keySet()) {
                for (TestStatusDetails obj : mainTableData.get(key).get(key2)) {
                    if (obj.getStatus().contains("Passed")) {
                        pass++;
                        tPass++;
                    } else if (obj.getStatus().contains("Bug")) {
                        bug++;
                        tBug++;
                    } else if (obj.getStatus().contains("UI Change")) {
                        updating++;
                        tUpdating++;
                    } else if (obj.getStatus().contains("Skipped")) {
                        skipped++;
                        tSkipped++;
                    }
                }

            }
            float health = 0;
            if (pass + bug + updating > 0)
                health = (pass * 100) / (pass + bug + updating);
            tableContent += "<tr>" +
                    "<td>" + key + "</td >" +
                    "<td >" + pass + "</td >" +
                    "<td >" + bug + "</td >" +
                    "<td >" + updating + "</td >" +
                    "<td >" + skipped + "</td >" +
                    "<td >" + health + "</td >" +
                    "</tr >";
            pass = 0;
            bug = 0;
            updating = 0;
            skipped = 0;
        }

        float totalHealth = 0;
        int totalFailed=tBug + tUpdating;
        if (tPass + tBug + tUpdating > 0)
            totalHealth = (tPass * 100) / (tPass + totalFailed);
        String htmlHead = getMainPageHeaderContent() +
                chart(String.valueOf(tPass), String.valueOf(tBug), String.valueOf(tUpdating),String.valueOf(tSkipped)) + "</head>";
        String body = "<body>" +
                "<div class=\"container\">\n" +
                "  <h6 style=\"color:black\"></h6>\n" +
                projectInfoHeader() +
                "<button type=\"button\" class=\"btn btn-success\">PASSED <span class=\"badge\">" + tPass + "</span></button>\n" +
                "<button type=\"button\" class=\"btn btn-danger\">Failed <span class=\"badge\">" + totalFailed + "</span></button>\n" +
                "<button type=\"button\" class=\"btn btn-warning\">Skipped <span class=\"badge\">" + tSkipped + "</span></button>\n" +
                "<button type=\"button\" class=\"btn btn-info\">HEALTH <span class=\"badge\">" + totalHealth + "%</span></button>\n" +
                "<p style=\"font-style: italic;font-size:11px;\">**Health: Calculated Based On Total Executed Tests Only.</p>\n"+
                "</div>" +
                "<div id=\"chartContainer\" style=\"height: 300px; max-width: 920px; margin: 0px auto;\"></div>";

        tableContent += "<tbody>" +
                "</table>" +
                "</div>" +
                "</body>";

        return List.of(htmlHead, body, tableHeader, tableContent);
    }

    private List<String> cssAnalyzerHtml() {
        return List.of("*{",
                "box-sizing: border-box;",
                "-webkit-box-sizing: border-box;",
                "-moz-box-sizing: border-box;",
                "}",
                "body{",
                "font-family: Helvetica;",
                "-webkit-font-smoothing: antialiased;",
                "background: rgba( 71, 147, 227, 1);",
                "}",
                "h2{",
                "text-align: center;",
                "font-size: 18px;",
                "text-transform: uppercase;",
                "letter-spacing: 1px;",
                "color: white;",
                "padding: 30px 0;",
                "}",
                /* Table Styles */

                ".table-wrapper{",
                "margin: 10px 70px 70px;",
                "box-shadow: 0px 35px 50px rgba( 0, 0, 0, 0.2 );",
                "}",
                ".fl-table {",
                "border-radius: 5px;",
                "font-size: 12px;",
                "font-weight: normal;",
                "border: none;",
                "border-collapse: collapse;",
                "width: 100%;",
                "max-width: 100%;",
                "white-space: nowrap;",
                "background-color: white;",
                "}",

                ".fl-table td, .fl-table th {",
                "text-align: center;",
                "padding: 8px;",
                "}",

                ".fl-table td {",
                "border-right: 1px solid #f8f8f8;",
                "font-size: 12px;",
                "}",

                ".fl-table thead th {",
                "color: #ffffff;",
                "background: #4FC3A1;",
                "}",

                ".fl-table thead th:nth-child(odd) {",
                "color: #ffffff;",
                "background: #324960;",
                "}",

                ".fl-table tr:nth-child(even) {",
                "background: #F8F8F8;",
                "}",

                /* Responsive */
                "@media (max-width: 767px) {",
                ".fl-table {",
                "display: block;",
                "width: 100%;",
                "}",
                ".table-wrapper:before{",
                "content: \"Scroll horizontally >\";",
                "display: block;",
                "text-align: right;",
                "font-size: 11px;",
                "color: white;",
                "padding: 0 0 10px;",
                "}",
                ".fl-table thead, .fl-table tbody, .fl-table thead th {",
                "display: block;",
                "}",
                ".fl-table thead th:last-child{",
                "border-bottom: none;",
                "}",
                ".fl-table thead {",
                "float: left;",
                "}",
                ".fl-table tbody {",
                "width: auto;",
                "position: relative;",
                "overflow-x: auto;",
                "}",
                ".fl-table td, .fl-table th {",
                "padding: 20px .625em .625em .625em;",
                "height: 60px;",
                "vertical-align: middle;",
                "box-sizing: border-box;",
                "overflow-x: hidden;",
                "overflow-y: auto;",
                "width: 120px;",
                "font-size: 13px;",
                "text-overflow: ellipsis;",
                "}",
                ".fl-table thead th {",
                "text-align: left;",
                "border-bottom: 1px solid #f7f7f9;",
                "}",
                ".fl-table tbody tr {",
                "display: table-cell;",
                "}",
                ".fl-table tbody tr:nth-child(odd) {",
                "background: none;",
                "}",
                ".fl-table tr:nth-child(even) {",
                "background: transparent;",
                "}",
                ".fl-table tr td:nth-child(odd) {",
                "background: #F8F8F8;",
                "border-right: 1px solid #E6E4E4;",
                "}",
                ".fl-table tr td:nth-child(even) {",
                "border-right: 1px solid #E6E4E4;",
                "}",
                ".fl-table tbody td {",
                "display: block;",
                "text-align: center;",
                "}",
                "}");
    }

    private String projectInfoHeader() {
        return "<h6 style=\"color:black\">Project:" + getProjectName() + " ||  Run:" + getRunType() + "  || Date:" + date + " || Env:" + getEnvironment() + "</h6>";
    }

    private String chart(String pass, String bugs, String updating,String skipped) {

        return " <script src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"></script>" +
                "<script type=\"text/javascript\">" +
                "window.onload = function () {" +
                "var chart = new CanvasJS.Chart(\"chartContainer\", {" +
                "theme: \"light1\"," +
                "title:{" +
                "text: \"Automated Analysis Report\"" +
                "}," +
                "data: [" +
                "{" +
                // Change type to "doughnut", "line", "splineArea", etc.
                "type: \"column\"," +
                "dataPoints: [" +
                "{ label: \"Pass\",  y:" + pass + " }," +
                "{ label: \"Bugs\",  y:" + bugs + " }," +
                "{ label: \"Skipped\",  y:" + skipped + " }," +
                "{ label: \"Updating\",  y:" + updating + " }" +
                "]" +
                "}" +
                "]" +
                "});" +
                "chart.render();" +
                "} </script>";
    }

    private String getMainPageHeaderContent() {
        return "<html>\n" +
                "  <head>\n" +
                "    <title>Test Automation Analysis</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"Files/styles.css\" />\n" +
                " <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\"> \n" +
                "\n" +
                " <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n" +
                "  <a class=\"navbar-brand\" href=\"#\">Automated Test Analysis Dashboard 1.0</a>\n" +
                "  <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
                "    <span class=\"navbar-toggler-icon\"></span>\n" +
                "  </button>\n" +
                "  <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n" +
                "    <ul class=\"navbar-nav\">\n" +
                "      <li class=\"nav-item\">\n" +
                "        <a class=\"nav-link\" style=\"color:white\" href=\"Files/bugs_details.html\">Bug Details</a>\n" +
                "      </li>\n" +
                "      <li class=\"nav-item\">\n" +
                "        <a class=\"nav-link\" style=\"color:white\" href=\"Files/updating_details.html\">Blockers/Update Details</a>\n" +
                "      </li>\n" +
                "    </ul>\n" +
                "  </div>\n" +
                "</nav>";
    }

    private String getBugPageHeaderContent() {
        return "<html>\n" +
                "  <head>\n" +
                "    <title>Test Automation Analysis</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />\n" +
                " <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\"> \n" +
                "\n" +
                " <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n" +
                "  <a class=\"navbar-brand\" href=\"../analysis.html\">Automated Test Analysis Dashboard 1.0</a>\n" +
                "  <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
                "    <span class=\"navbar-toggler-icon\"></span>\n" +
                "  </button>\n" +
                "  <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n" +
                "    <ul class=\"navbar-nav\">\n" +
                "      <li class=\"nav-item\">\n" +
                "        <a class=\"nav-link\" style=\"color:white\" href=\"#\">Bug Details</a>\n" +
                "      </li>\n" +
                "      <li class=\"nav-item\">\n" +
                "        <a class=\"nav-link\" style=\"color:white\" href=\"../Files/updating_details.html\">Blockers/Update Details</a>\n" +
                "      </li>\n" +
                "    </ul>\n" +
                "  </div>\n" +
                "</nav>";
    }

    private String getUpdatingPageHeaderContent() {
        return "<html>\n" +
                "  <head>\n" +
                "    <title>Test Automation Analysis</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />\n" +
                " <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\"> \n" +
                "\n" +
                " <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n" +
                "  <a class=\"navbar-brand\" href=\"../analysis.html\">Automated Test Analysis Dashboard 1.0</a>\n" +
                "  <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
                "    <span class=\"navbar-toggler-icon\"></span>\n" +
                "  </button>\n" +
                "  <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n" +
                "    <ul class=\"navbar-nav\">\n" +
                "      <li class=\"nav-item\">\n" +
                "        <a class=\"nav-link\" style=\"color:white\" href=\"../Files/bugs_details.html\">Bug Details</a>\n" +
                "      </li>\n" +
                "      <li class=\"nav-item\">\n" +
                "        <a class=\"nav-link\" style=\"color:white\" href=\"#\">Blockers/Update Details</a>\n" +
                "      </li>\n" +
                "    </ul>\n" +
                "  </div>\n" +
                "</nav>";
    }


    private String getEnvironment() {
        try {
            return EnvironmentFactory.getEnvironmentName();
        } catch (Exception e) {
            return "No Information";
        }
    }

    private String getRunType() {
        try {
            return EnvironmentFactory.getScenarioTag();
        } catch (Exception e) {
            return "No Information";
        }
    }

    private String getProjectName() {
        try {
            return EnvironmentFactory.getProjectName();
        } catch (Exception e) {
            return "No Information";
        }
    }

    private static String getReportSourcePath() {
        if (EnvironmentFactory.getReportAnalysisDataPath() != null)
            return EnvironmentFactory.getReportAnalysisDataPath();
        else {
            return System.getProperty("user.dir") + "/target/cucumber-report";
        }
    }

    private static String getReportGenerationPath() {
        if (EnvironmentFactory.getReportAnalysisGenerationPath() != null)
            return EnvironmentFactory.getReportAnalysisGenerationPath();
        else {
            return System.getProperty("user.dir") + "/target";
        }
    }

    private String getTotalTestCount() throws IOException {
        long testCount = 0;
        try {
            List<Path> featurePath = Files.walk(Paths.get(System.getProperty("user.dir") + "/src/test/resources/features")).filter(fileType -> fileType.getFileName().toString().contains(".feature")).map(filePaths -> filePaths.toAbsolutePath()).collect(Collectors.toList());

            for (Path featureFiles : featurePath) {
                testCount = testCount + Files.readAllLines(featureFiles).stream().map(trimLines -> trimLines.trim()).filter(ff -> ff.startsWith("Then I")).count();
            }
        } catch (Exception e){

        }
        return String.valueOf(testCount);
    }
}
