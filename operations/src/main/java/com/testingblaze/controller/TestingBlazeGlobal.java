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

import java.util.LinkedHashMap;
import java.util.Map;

public class TestingBlazeGlobal {
    private static Map<String, Object> globalVariable = new LinkedHashMap<>();

    public static Object getVariable(String key) {
        return (globalVariable.get(key));
    }

    public static void deleteRecord(String key) {
        globalVariable.remove(key);
    }

    public static void setVariable(String key, Object value) {
        globalVariable.put(key, value);
    }

}
