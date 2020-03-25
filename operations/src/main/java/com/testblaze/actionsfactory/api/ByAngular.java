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
package com.testblaze.actionsfactory.api;

public enum ByAngular {
    Model {
        @Override
        public String is(String locator) {
            return "ByAngular::MODEL:"+locator;
        }
    },
    Binding {
        @Override
        public String is(String locator) {
            return "ByAngular::BINDING:"+locator;
        }
    },
    ButtonText {
        @Override
        public String is(String locator) {
            return "ByAngular::BUTTON_TEXT:"+locator;
        }
    },
    ExactBinding {
        @Override
        public String is(String locator) {
            return "ByAngular::EXACT_BINDING:"+locator;
        }
    },
    CssContainingText {
        @Override
        public String is(String locator) {
            return "ByAngular::CSS CONTAINING TEXT:"+locator;
        }
    },
    Options {
        @Override
        public String is(String locator) {
            return "ByAngular::Options:"+locator;
        }
    },
    PartialButtonText {
        @Override
        public String is(String locator) {
            return "ByAngular::PARTIAL_BUTTON_TEXT:"+locator;
        }
    },
    Repeater {
        @Override
        public String is(String locator) {
            return "ByAngular::REPEATER:"+locator;
        }
    },
    ExactRepeater {
        @Override
        public String is(String locator) {
            return "ByAngular::EXACT_REPEATER:"+locator;
        }
    };


    public abstract String is(String locator);
}
