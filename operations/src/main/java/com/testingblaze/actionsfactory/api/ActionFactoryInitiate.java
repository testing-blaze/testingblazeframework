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


import com.testingblaze.actionsfactory.abstracts.Action;
import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.actionsfactory.abstracts.ElementProcessing;
import com.testingblaze.actionsfactory.abstracts.LocatorProcessing;
import com.testingblaze.actionsfactory.elementfunctions.FindMyElements;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.actionsfactory.elementfunctions.Mobile;
import com.testingblaze.actionsfactory.elementfunctions.MouseActions;
import com.testingblaze.actionsfactory.elementfunctions.Ng;
import com.testingblaze.actionsfactory.elementfunctions.Waits;
import com.testingblaze.actionsfactory.processing.ExecuteElementProcessing;
import com.testingblaze.actionsfactory.processing.ExecuteLocatorProcessing;
import com.testingblaze.actionsfactory.type.AddOns;
import com.testingblaze.actionsfactory.type.Click;
import com.testingblaze.actionsfactory.type.DropDown;
import com.testingblaze.actionsfactory.type.ElementReference;
import com.testingblaze.actionsfactory.type.Is;
import com.testingblaze.actionsfactory.type.Scroll;
import com.testingblaze.actionsfactory.type.TextInput;
import com.testingblaze.objects.InstanceRecording;

public class ActionFactoryInitiate {
    public ActionFactoryInitiate() {
        InstanceRecording.recordInstance(IframeAnalyzer.class, new IframeAnalyzer());
        InstanceRecording.recordInstance(LocatorProcessing.class,new ExecuteLocatorProcessing()::getRefinedLocator);
        InstanceRecording.recordInstance(Waits.class, new Waits());
        InstanceRecording.recordInstance(Action.class, new ExecuteCoreActions());
        InstanceRecording.recordInstance(AddOns.class, new AddOns());
        InstanceRecording.recordInstance(JavaScript.class, new JavaScript());
        InstanceRecording.recordInstance(ElementProcessing.class, new ExecuteElementProcessing());
        InstanceRecording.recordInstance(FindMyElements.class, new FindMyElements());
        InstanceRecording.recordInstance(Mobile.class, new Mobile());
        InstanceRecording.recordInstance(Ng.class, new Ng());
        InstanceRecording.recordInstance(MouseActions.class, new MouseActions());
        InstanceRecording.recordInstance(Element.class, new ElementAPI());
        InstanceRecording.recordInstance(Is.class, new Is());
        InstanceRecording.recordInstance(Click.class, new Click());
        InstanceRecording.recordInstance(TextInput.class, new TextInput());
        InstanceRecording.recordInstance(Scroll.class, new Scroll());
        InstanceRecording.recordInstance(DropDown.class, new DropDown());
        InstanceRecording.recordInstance(ElementReference.class, new ElementReference());
    }
}
