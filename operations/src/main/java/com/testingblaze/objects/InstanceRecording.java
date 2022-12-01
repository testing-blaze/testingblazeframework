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
package com.testingblaze.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class InstanceRecording {
    private static Map<Class<?>, Object> instanceRecorder = new HashMap<>();

    /**
     * The instance management is based on Singleton Approach
     *
     * @param type ClassName.class
     * @return
     */
    public static <T> T getInstance(Class<T> type) {
        return type.cast(instanceRecorder.get(type));
    }

    /**
     * Record the instance
     *
     * @param type
     * @param object
     */
    public static <T> void recordInstance(Class<T> type, T object) {
        instanceRecorder.put(Objects.requireNonNull(type), Objects.requireNonNull(object));
    }

    /**
     * Flush the instance
     */
    public static <T> void flushInstance() {
        instanceRecorder.clear();
    }


    /**
     * To work on...
     *
     * @param type
     * @param <T>
     */
    public static <T> void restrictObjectCreation(Class<T> type) {
        if (getInstance(type) != null) {
            try {
                throw new IllegalAccessException("Instance creation is restricted");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
