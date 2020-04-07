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
package com.testingblaze.controller;


import com.testingblaze.misclib.Properties_Logs;
import com.testingblaze.register.EnvironmentFetcher;

import java.io.IOException;

/**
 * This interface implementation will ensure to fetch the users according to differennt environments.
 *
 * @author nauman.shahid
 */
public interface UsersController {
    String getUserName(String userLevel);

    String getPassword(String userLevel);

    default String getEmail(String userLevel) {
        return null;
    }

    /**
     * Handles users based on environments in project frameworks
     *
     * @param portalType
     * @return relevant url
     * @author nauman.shahid
     */
    default String getPortalUrl(String portalType) {
        String url = null;
        if ("default".equalsIgnoreCase(portalType) || EnvironmentFetcher.getEnvironmentName().equalsIgnoreCase(portalType)) {
            url = EnvironmentFetcher.getEnvironmentUrl();
        } else {
            try {
                url = new Properties_Logs().ReadPropertyFile("environment.properties", EnvironmentFetcher.getEnvironmentName() + portalType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return url;
    }
}
