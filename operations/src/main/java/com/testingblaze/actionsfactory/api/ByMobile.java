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
package com.testingblaze.actionsfactory.api;

public enum ByMobile {

    Xpath {
        @Override
        public String is(String locator) {
            return "ByMobile::xpath:"+locator;
        }
    },
    Id {
        @Override
        public String is(String locator) {
            return "ByMobile::id"+locator;
        }
    },
    Name {
        @Override
        public String is(String locator) {
            return "ByMobile::name:"+locator;
        }
    },
    ClassName {
        @Override
        public String is(String locator) {
            return "ByMobile::classname:"+locator;
        }
    },
    Css {
        @Override
        public String is(String locator) {
            return "ByMobile::css:"+locator;
        }
    },
    LinkText {
        @Override
        public String is(String locator) {
            return "ByMobile::linktext:"+locator;
        }
    },
    AccessibilityId {
        @Override
        public String is(String locator) {
            return "ByMobile::AccessibilityId:"+locator;
        }
    },
    TagName {
        @Override
        public String is(String locator) {
            return "ByMobile::tagname:"+locator;
        }
    };


    public abstract String is(String locator);
}
