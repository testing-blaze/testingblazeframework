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
package com.testingblaze.register;


import com.testingblaze.actionsfactory.api.ActionFactoryInitiate;
import com.testingblaze.objects.InstanceRecording;
import io.cucumber.java.Before;

public class AfiYxaVmIURegAFact {

    public AfiYxaVmIURegAFact(ActionFactoryInitiate actions) {
        InstanceRecording.recordInstance(ActionFactoryInitiate.class, actions);
    }

    @Before(order = 1)
    public void startActionsRegistration() {
        // To setup second registration step
    }
}
