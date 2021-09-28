package com.testingblaze.report;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.testingblaze.objects.TestStatusDetails;
import com.testingblaze.register.EnvironmentFactory;
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
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReportAnalyzer {
    private static final JsonParser parser = new JsonParser();
    private static List<TestStatusDetails> testStatusDetails;
    private static String defaultProjectPath = System.getProperty("user.dir");
    private static String mainHTMLHeader = "<html>\n" +
            "  <head>\n" +
            "    <title>Test Automation Analysis</title>\n";

    private static String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    public void executeAnalysis(){
        reportConfigWriteUp();
    }
    public void reportConfigWriteUp() {
        Path pathAnalysis = Paths.get(defaultProjectPath + "/target/ReportAnalysis");
        Path pathFiles = Paths.get(defaultProjectPath + "/target/ReportAnalysis/Files");

        try {
            if (Files.notExists(pathAnalysis))
                Files.createDirectories(pathAnalysis);
            Files.createDirectories(pathFiles);

            if (Files.list(pathAnalysis).noneMatch(file -> file.startsWith("analysis"))) {
                Files.createFile(Paths.get(pathAnalysis + "/analysis.html"));
                Files.createFile(Paths.get(pathFiles + "/updating_details.html"));
                Files.createFile(Paths.get(pathFiles + "/bugs_details.html"));
                compileReport(pathAnalysis, pathFiles);
            }
            if (Files.list(pathFiles).noneMatch(file -> file.startsWith("styles.css"))) {
                Files.createFile(Paths.get(pathFiles + "/styles.css"));
                Files.write(Paths.get(pathFiles + "/styles.css"), cssAnalyzerHtml());
            }
        } catch (IOException e) {
        }
    }


    public void compileReport(Path pathAnalysis, Path pathFiles) throws IOException {
        Map<String, Map<String, List<TestStatusDetails>>> mainTableContainer = new TreeMap<>();
        Set<String> files = new HashSet();
        String tagName = "None";
        int assignedNumber = 0;
        String directoryName = getReportSourcePath();
        List<String> newFiles = Arrays.stream(Objects.requireNonNull(new File(directoryName).list())).collect(Collectors.toList());

        for (String fileName : newFiles) {
            if(!fileName.equalsIgnoreCase(".DS_Store"))
                files.add(fileName.split("_")[0]);
        }
        for (String specificFileName : files) {
            List<String> specificFiles = Arrays.stream(Objects.requireNonNull(new File(directoryName).list()))
                    .filter(el -> el.startsWith(specificFileName)).collect(Collectors.toList());

            Map<String, List<TestStatusDetails>> reportData = new TreeMap<>();
            for (String fileName : specificFiles) {
                assignedNumber++;
                String fileLocation = directoryName + "/" + fileName;

                JsonArray objects = parser.parse(new InputStreamReader(new FileInputStream(fileLocation), StandardCharsets.UTF_8)).getAsJsonArray();
                testStatusDetails = new ArrayList<>();

                for (JsonElement object : objects) {

                    for (JsonElement element : object.getAsJsonObject().get("elements").getAsJsonArray()) {
                        Boolean parentTag = true;
                        JsonArray fetchTAG = element.getAsJsonObject().get("tags").getAsJsonArray();
                        List<String> tagsHolder = new ArrayList<>();
                        for (JsonElement testTag : fetchTAG) {
                            String data = testTag.getAsJsonObject().get("name").getAsString();

                            if (parentTag && data.matches("[^a-zA-Z]+")) {
                                tagName = data;
                                tagsHolder.add(data);
                                parentTag = false;
                            } else if (data.matches("[^a-zA-Z]+")) {
                                tagsHolder.add(data);
                            }
                        }
                        int tag = 0;
                        for (JsonElement step : element.getAsJsonObject().get("steps").getAsJsonArray()) {
                            var keyword = step.getAsJsonObject().get("keyword").getAsString();
                            var stepName = step.getAsJsonObject().get("name").getAsString();
                            var result = step.getAsJsonObject().get("result").getAsJsonObject().get("status").getAsString();
                            if (StringUtils.containsIgnoreCase(keyword, "Then") && StringUtils.containsIgnoreCase(result, "failed")) {
                                tag++;
                                if (tagsHolder.size() > tag) {
                                    testStatusDetails.add(new TestStatusDetails("Bug", stepName, tagsHolder.get(tag)));
                                } else {
                                    testStatusDetails.add(new TestStatusDetails("Bug", stepName, "None"));
                                }

                            }else if (StringUtils.containsIgnoreCase(keyword, "Then") && StringUtils.containsIgnoreCase(result, "passed")) {
                                tag++;
                                if (tagsHolder.size() > tag) {
                                    testStatusDetails.add(new TestStatusDetails("Passed", stepName, tagsHolder.get(tag)));
                                } else {
                                    testStatusDetails.add(new TestStatusDetails("Passed", stepName, "None"));
                                }
                            }else if(StringUtils.containsIgnoreCase(result, "failed"))  {
                                        testStatusDetails.add(new TestStatusDetails("UI Change or Blocker", stepName, "None"));
                                }

                        }

                    }
                }
                if (reportData.containsKey(tagName))
                    tagName = tagName + "-Dup-" + assignedNumber;
                reportData.put(tagName, testStatusDetails);
                tagName = "No Tag" + assignedNumber;
            }
            if (mainTableContainer.containsKey(specificFileName))
                mainTableContainer.put(specificFileName + assignedNumber, reportData);
            else {
                mainTableContainer.put(specificFileName, reportData);
            }

        }

        Files.write(Paths.get(pathAnalysis + "/analysis.html"), createMainHtmlPage(mainTableContainer));
        Files.write(Paths.get(pathFiles + "/bugs_details.html"), createBugDetailsHtmlPage(mainTableContainer));
        Files.write(Paths.get(pathFiles + "/updating_details.html"), createUpdatingDetailsHtmlPage(mainTableContainer));
    }

    public List<String> createUpdatingDetailsHtmlPage(Map<String, Map<String, List<TestStatusDetails>>> mainTableData) {
        String htmlHeader = mainHTMLHeader + "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />\n" +
                "<a href=\"../analysis.html\" type=\"button\" class=\"btn btn-primary btn-lg btn-block\">Home</a> </head>";
        String tableHeader = "<h2>Automated Test Analysis</h2>" +
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
                "</div>";

        return List.of(htmlHeader, tableHeader, tableContent);
    }


    public List<String> createBugDetailsHtmlPage(Map<String, Map<String, List<TestStatusDetails>>> mainTableData) {
        String htmlHeader = mainHTMLHeader +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />\n" +
                "<a href=\"../analysis.html\" type=\"button\" class=\"btn btn-primary btn-lg btn-block\">Home</a> </head>";
        String tableHeader = "<h2>Automated Test Analysis</h2>" +
                "<h4 style=\"background-color:Red;text-align: center;\">Bugs (TFS Test Tags) </h4>" +
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
                "</div>";

        return List.of(htmlHeader, tableHeader, tableContent);
    }


    public List<String> createMainHtmlPage(Map<String, Map<String, List<TestStatusDetails>>> mainTableData) {
        int pass = 0, bug = 0, updating = 0;
        int tPass = 0, tBug = 0, tUpdating = 0;

        String tableHeader = "<h2>Automated Test Analysis 1.0</h2>" +
                "<div class=\"table-wrapper\">" +
                projectInfoHeader() +
                "<table class=\"fl-table\">" +

                "<thead>" +
                "<tr>" +
                "<th>Module</th>" +
                "<th>Passed</th>" +
                "<th>Bugs</th>" +
                "<th>Blocker/Updating</th>" +
                "<th>Health</th>" +
                "</tr>" +
                "</thead>";
        String tableContent = "<tbody>";

        for (String key : mainTableData.keySet()) {
            int testCount = mainTableData.get(key).size();
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
                    }
                }

            }
            float health = (pass * 100) / (pass+bug+updating);
            tableContent += "<tr>" +
                    "<td>" + key + "</td >" +
                    "<td >" + pass + "</td >" +
                    "<td >" + bug + "</td >" +
                    "<td >" + updating + "</td >" +
                    "<td >" + health + "</td >" +
                    "</tr >";
            pass = 0;
            bug = 0;
            updating = 0;
        }
        String htmlHead = mainHTMLHeader +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"Files/styles.css\" />\n" +
                chart(String.valueOf(tPass), String.valueOf(tBug), String.valueOf(tUpdating)) +
                "<a href=\"Files/bugs_details.html\" class=\"button\"><button style=\"background-color:#FF5733;float: right;\">Bugs</button></a>" +
                "<a href=\"Files/updating_details.html\" class=\"button\"><button style=\"background-color:yellow;float: right;\">Updates|Blockers</button></a> </head>";
        String body = "<body>" +
                "<div id=\"chartContainer\" style=\"height: 300px; max-width: 920px; margin: 0px auto;\"></div>" +
                "</body>";
        tableContent += "<tbody>" +
                "</table>" +
                "</div>";

        return List.of(htmlHead, tableHeader, body, tableContent);
    }

    public List<String> cssAnalyzerHtml() {
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
        return "<h4>Project:" + getProjectName()+ " ||  Run:" + getRunType() + "  || Date:" + date + " || Env:" + getEnvironment() + "</h4>";
    }

    private String chart(String pass, String bugs, String updating) {

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
                "{ label: \"Updating\",  y:" + updating + " }" +
                "]" +
                "}" +
                "]" +
                "});" +
                "chart.render();" +
                "} </script>";
    }


    private String getEnvironment() {
        try {
            return EnvironmentFactory.getEnvironmentUrl();
        } catch(Exception e) {
            return "No Information";
        }
    }

    private String getRunType() {
        try {
            return EnvironmentFactory.getScenarioTag();
        } catch(Exception e) {
            return "No Information";
        }
    }

    private String getProjectName() {
        try {
            return EnvironmentFactory.getProjectName();
        } catch(Exception e) {
            return "No Information";
        }
    }

    private static String getReportSourcePath(){
        if(EnvironmentFactory.getReportAnalysisPath() != null) return EnvironmentFactory.getReportAnalysisPath();
        else {
            return System.getProperty("user.dir")+"/target/cucumber-report";
        }
    }
}
