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
package com.testingblaze.misclib;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Properties file reader
 */

public final class Properties_Logs {
    private final Map<String, String> valueStore = new HashMap<>();
    String log4jConfPath = "log4j.properties";
    static final Logger log = Logger.getLogger(TestBlazeLogs.class.getName());
    Properties OR;
    TestBlazeLogs testBlazeLogs;

    public Properties_Logs() {
        testBlazeLogs = new TestBlazeLogs();
    }


    /**
     * Read from property file
     */
    public String ReadPropertyFile(String fileName, String parameter) throws IOException {
        OR = new Properties();
        try {
            OR.load(new InputStreamReader(getClass().getResourceAsStream("/" + fileName), StandardCharsets.UTF_8));
        } catch (Exception e) {
            OR.load(new InputStreamReader(getClass().getResourceAsStream("/properties/" + fileName), StandardCharsets.UTF_8));
        }
        return OR.getProperty(parameter);
    }

    /**
     * Set Property
     */
    public void setProperty(String key, String value) {
        OR = new Properties();
        OR.setProperty(key, value);
    }

    /**
     * Saves a value for future use in a scenario
     *
     * @param key   the key with which to identify the saved value in the future (case-insensitive)
     * @param value the value to save for future use
     */
    public void saveValue(String key, String value) {
        valueStore.put(key.toUpperCase(), value);
    }

    /**
     * Gets a value previously saved in a scenario
     *
     * @param key the key which identifies the saved value (case-insensitive)
     * @return the previously saved value
     */
    public String getValue(String key) {
        return valueStore.get(key.toUpperCase());
    }

    /**
     * get all keys of the saved values
     *
     * @return set of keys
     * @author nauman.shahid
     */
    public Set<String> getAllSavedKeys() {
        return valueStore.keySet();
    }

    /**
     * get all keys of the saved values
     *
     * @return set of keys
     * @author nauman.shahid
     */
    public Map<String, String> getSavedValuesMap() {
        return valueStore;
    }

    public TestBlazeLogs generateLogs() {
        return testBlazeLogs;
    }

    /**
     * Inner class to handle logs
     */
    public final class TestBlazeLogs {

        /**
         * generate logs
         *
         * @param data                     Message
         * @param infoOrWarnOrErrorOrDebug info for informational message, warn for warning messages, debug for debug messages and error for errors
         * @author nauman.shahid
         */
        public void logs(String data, String infoOrWarnOrErrorOrDebug) {
            if ("warn".equalsIgnoreCase(infoOrWarnOrErrorOrDebug)) {
                log.warn(data);
            }
            if ("debug".equalsIgnoreCase(infoOrWarnOrErrorOrDebug)) {
                log.debug(data);
            }
            if ("error".equalsIgnoreCase(infoOrWarnOrErrorOrDebug)) {
                log.error(data);
            } else if ("info".equalsIgnoreCase(infoOrWarnOrErrorOrDebug)) {
                log.info(data);
            }
        }
    }


}
