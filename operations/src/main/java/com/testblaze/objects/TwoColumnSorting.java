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
package com.testblaze.objects;

/**
 * This class is designed to received two row values (String,int) for saving them as custom array
 *
 * @author nauman.shahid
 */
public final class TwoColumnSorting {

    public String key;
    public int value;

    public TwoColumnSorting(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getPair() {
        return "Column 1: " + key + "| Column 2: " + value;
    }

}
