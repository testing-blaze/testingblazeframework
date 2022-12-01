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
package com.testingblaze.report;

import com.testingblaze.register.EnvironmentFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class CSS {
    /**
     * generates project reporting configurations
     *
     * @author nauman.shahid
     */
    public void reportConfigWriteUp() {
        var projectPath= EnvironmentFactory.getProjectPath();
        var projectName = EnvironmentFactory.getProjectName();
        Path path = Paths.get(projectPath + "/target/Automation-Report/test_blaze_config");
        try {
            if (Files.notExists(path))
                Files.createDirectories(path);
            if (Files.list(path).noneMatch(file -> file.startsWith("custom"))) {
                Files.createFile(Paths.get(path + "/custom.css"));
                Files.createFile(Paths.get(path + "/custom.properties"));
                Files.write(Paths.get(path + "/custom.css"), createCssParams());
                Files.write(Paths.get(path + "/custom.properties"), customParameters(projectName));
            }
        } catch (IOException e) {
        }
    }

    /**
     * setup css for report generation across projects
     *
     * @author nauman.shahid
     */
    private static List<String> createCssParams() {
        return List.of(
                "body, .navbar {",
                "  background-color: #003666;",
                "  -webkit-animation: mymove 10s infinite;",
                "  animation: mymove 10s infinite;",
                "}",
                "h3, h4, h5 {",
                "  color: white;",
                "}",
                "div.card, .list-group-item {",
                "  background-color: #d9d9d9;",
                "}",
                ".footer {",
                "  background-color: #a5abae !important;",
                "}",
                "div.customParameters div.card-body tr:nth-child(3) td:nth-child(2) {",
                "  color: #7fff00;",
                "  font-style: oblique;",
                "  font-weight: bold;",
                "  animation-name: flash;",
                "  animation-duration: 0.9s;",
                "  animation-timing-function: linear;",
                "  animation-iteration-count: infinite;",
                "  animation-direction: alternate;",
                "  animation-play-state: running;",
                "}",
                "div.card-body tr:nth-child(2) td:nth-child(2), div.card-body tr:nth-child(1) td:nth-child(2) {",
                "  font-weight: bold;",
                "  color: #737373;",
                "}",
                "select.custom-select, input.form-control {",
                "  background-color:#e6f2ff;",
                "}",
                "@keyframes mymove {",
                "  0% {background-color: #d9d9d9;}",
                "  50% {background-color: #404040;}",
                "  100% {background-color: #d9d9d9;}",
                "}",
                "@keyframes flash {",
                "  0% {color: #2F94A8;}",
                "  50% {color: black;}",
                "  100% {color: #2F94A8;}",
                "}"
        );
    }

    private static List<String> customParameters(String projectName) {
        return List.of(
                "Project:" + projectName ,
                "Specifications:"
                        + "Environment Name: " + EnvironmentFactory.getEnvironmentName()
                        + "  - Url: " + EnvironmentFactory.getEnvironmentUrl()
                        + "  ||  Executed Tag: " + EnvironmentFactory.getScenarioTag()
                        + "<br>Browser: " + EnvironmentFactory.getDevice()
                        + "  - Version: " + EnvironmentFactory.getDeviceVersion()
                        + "  - Platform: " + EnvironmentFactory.getPlatformInfo()
                        +"<br>Execution Mode: " + (EnvironmentFactory.isHeadless() ? "Headless" : "Browser UI")
                        + "  - Reduced Automation Execution Speed: "+(EnvironmentFactory.getSlowDownExecutionTime() > 0 ? "Yes":"No"),
                "PoweredBy:"+"Testing Blaze Automation Solution - Apache License 2.0 [ "+ EnvironmentFactory.getOrgName()+"]"
        );
    }

}
