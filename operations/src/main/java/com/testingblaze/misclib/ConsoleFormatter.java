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

public final class ConsoleFormatter {
    public enum ICON {
        NO_ICON(""),
        THUMBS_UP("👍"),
        THUMBS_DOWN("👎"),
        WEB("🌐"),
        CYCLONE("🌀"),
        CHECKERED_FLAG("🏁"),
        CLOCK("🕐"),
        TICK("✓"),
        ROBOT("🤖"),
        HORSE("🏇"),
        OVERLAPPING_FRAME("🗗"),
        FRAME("🖽"),
        SWITCH_IN("⎘"),
        SWITCH_OUT("⎗"),
        PENGUIN("🐧"),
        ROUND_ARROW("🔃"),
        DOLPHIN("🐬"),
        HOURGLASS("⌛"),
        X_MARK("❌");

        private String icon;
        ICON(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return icon;
        }
    }

    public enum COLOR {
        BLACK("\u001B[30m", "\u001B[40m"), RED("\u001B[31m", "\u001B[41m"), GREEN("\u001B[32m", "\u001B[42m"), YELLOW("\u001B[33m", "\u001B[43m"),
        BLUE("\u001B[34m", "\u001B[44m"), PURPLE("\u001B[35m", "\u001B[45m"), CYAN("\u001B[36m", "\u001B[46m"), WHITE("\u001B[37m", "\u001B[47m"),
        BRIGHT_BLACK("\u001B[90m", "\u001B[100m"), BRIGHT_RED("\u001B[91m", "\u001B[101m"), BRIGHT_GREEN("\u001B[92m", "\u001B[102m"), BRIGHT_YELLOW("\u001B[93m", "\u001B[103m"),
        BRIGHT_BLUE("\u001B[94m", "\u001B[104m"), BRIGHT_PURPLE("\u001B[95m", "\u001B[105m"), BRIGHT_CYAN("\u001B[96m", "\u001B[106m"), BRIGHT_WHITE("\u001B[97m", "\u001B[107m");

        private String foregroundColor;
        private String backgroundColor;
        private static final String RESET = "\u001B[0m";

        COLOR(String foregroundColor, String backgroundColor) {
            this.foregroundColor = foregroundColor;
            this.backgroundColor = backgroundColor;
        }

        private String getForegroundColor() {
            return foregroundColor;
        }

        private String getBackgroundColor() {
            return backgroundColor;
        }
    }

    public static String setTextColor(COLOR color, String text) {
        return color.getForegroundColor() + text + COLOR.RESET;
    }

    public static String setTextColor(COLOR color, ICON icon) {
        return color.getForegroundColor() + icon + COLOR.RESET;
    }

    public static String setBackgroundColor(COLOR color, String text) {
        return color.getBackgroundColor() + text + COLOR.RESET;
    }

    public static String setBackgroundColor(COLOR color, ICON icon) {
        return color.getBackgroundColor() + icon + COLOR.RESET;
    }

    public static String setBoldText(String text) {
        return "\033[0;1m" + text + "\033[0;0m";
    }

    public static String setBoldText(ICON icon) {
        return "\033[0;1m" + icon + "\033[0;0m";
    }
}
