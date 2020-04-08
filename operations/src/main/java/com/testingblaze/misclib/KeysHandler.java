/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Solution.
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

import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * To handle keys press and release of keyboard
 *
 * @author nauman.shahid
 */
public final class KeysHandler {
    private Actions actions;
    private KeyFunctions keyFunctions;

    public KeysHandler() {
        this.actions = new Actions(InstanceRecording.getInstance(DeviceBucket.class).getDriver());
        this.keyFunctions = new KeyFunctions();
    }

    /**
     * Press any functional key using {KeyBoard.Alt}
     *
     * @param key
     * @author nauman.shahid
     */
    public KeyFunctions keyPress(Keys key) {
        actions.keyDown(key).build().perform();
        return keyFunctions;
    }

    /**
     * Reach an element and then press a function key using {KeyBoard.Alt}
     *
     * @param element
     * @param key
     * @author nauman.shahid
     */
    public KeyFunctions KeyPress(WebElement element, Keys key) {
        actions.keyDown(element, key).build().perform();
        return keyFunctions;
    }

    public class KeyFunctions {

        /**
         * release the already depressed key
         *
         * @param holdTimeSeconds
         * @author nauman.shahid
         * @Unverified functionality yet to be tested
         */
        public KeyFunctions releaseKey(int holdTimeSeconds) {
            actions.pause(holdTimeSeconds * 1000).sendKeys(Keys.NULL).build().perform();
            return this;
        }

        /**
         * release the already depressed key
         *
         * @author nauman.shahid
         * @Unverified functionality yet to be tested
         */
        public KeyFunctions releaseKey() {
            actions.sendKeys(Keys.NULL).build().perform();
            return this;
        }

        /**
         * Enter any alpha numeric value from keyboard like {"A"}
         *
         * @author nauman.shahid
         */
        public KeyFunctions AlphaKeyPress(String nameOfAplhaKey) {
            actions.sendKeys(nameOfAplhaKey).build().perform();
            return this;
        }

        /**
         * Returns the depressed key back to original position
         *
         * @param key
         * @author nauman.shahid
         */
        public KeyFunctions KeyUp(Keys key) {
            actions.keyUp(key).build().perform();
            return this;
        }

        /**
         * Returns the depressed key back to original position
         *
         * @param element
         * @param key
         * @author nauman.shahid
         */
        public KeyFunctions KeyUp(WebElement element, Keys key) {
            actions.keyUp(element, key).build().perform();
            return this;
        }

        /**
         * Return depressed key after waiting for certain time
         *
         * @param key
         * @param waitTimeInSec
         * @author nauman.shahid
         */
        public KeyFunctions KeyUp(Keys key, int waitTimeInSec) {
            actions.keyUp(key).pause(waitTimeInSec * 1000).build().perform();
            return this;
        }

        /**
         * Return depressed key after waiting for certain time
         *
         * @param element
         * @param key
         * @param waitTimeInSec
         * @author nauman.shahid
         */
        public KeyFunctions KeyUp(WebElement element, Keys key, int waitTimeInSec) {
            actions.keyUp(element, key).pause(waitTimeInSec).build().perform();
            return this;
        }

    }
}
