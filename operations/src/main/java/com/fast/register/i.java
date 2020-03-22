/*
 * Copyright 2020
 *
 * This file is part of Fregata Automated Testing Solution [FAST].
 *
 * FAST is licensed under the Apache License, Version
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
package com.fast.register;

import com.fast.controller.CoreHandlers;
import com.fast.objects.InstanceRecording;

public class i {
    //Make register token based
    private static String register;

    public static void validateRegistry() {
        if (!isAuthorizedProject(register)) {
            throw new SecurityException("You do not have permission to access this library");
        }
    }

    public static void setRegister(AuthorizedProject register) {
        i.register = register.toString();
    }


    public static CoreHandlers perform() {
        return InstanceRecording.getInstance(CoreHandlers.class);
    }

    private static boolean isAuthorizedProject(String project) {
        boolean flag = false;
        for (AuthorizedProject validProject : AuthorizedProject.values()) {
            if (validProject.toString().equalsIgnoreCase(project)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
