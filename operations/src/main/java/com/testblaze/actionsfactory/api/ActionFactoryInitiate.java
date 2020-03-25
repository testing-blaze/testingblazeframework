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


import com.testblaze.actionsfactory.abstracts.Action;
import com.testblaze.actionsfactory.abstracts.Element;
import com.testblaze.actionsfactory.abstracts.ElementProcessing;
import com.testblaze.actionsfactory.abstracts.LocatorProcessing;
import com.testblaze.actionsfactory.elementfunctions.FindMyElements;
import com.testblaze.actionsfactory.elementfunctions.JavaScript;
import com.testblaze.actionsfactory.elementfunctions.Mobile;
import com.testblaze.actionsfactory.elementfunctions.MouseActions;
import com.testblaze.actionsfactory.elementfunctions.Ng;
import com.testblaze.actionsfactory.elementfunctions.Waits;
import com.testblaze.actionsfactory.processing.ExecuteElementProcessing;
import com.testblaze.actionsfactory.processing.ExecuteLocatorProcessing;
import com.testblaze.actionsfactory.type.Click;
import com.testblaze.actionsfactory.type.DropDown;
import com.testblaze.actionsfactory.type.ElementReference;
import com.testblaze.actionsfactory.type.EnterText;
import com.testblaze.actionsfactory.type.Is;
import com.testblaze.actionsfactory.type.Scroll;
import com.testblaze.objects.InstanceRecording;

public class ActionFactoryInitiate {
    public ActionFactoryInitiate() {
        InstanceRecording.recordInstance(IframeAnalyzer.class, new IframeAnalyzer());
        InstanceRecording.recordInstance(LocatorProcessing.class,new ExecuteLocatorProcessing()::getRefinedLocator);
        InstanceRecording.recordInstance(Waits.class, new Waits());
        InstanceRecording.recordInstance(Action.class, new ExecuteCoreActions());
        InstanceRecording.recordInstance(JavaScript.class, new JavaScript());
        InstanceRecording.recordInstance(ElementProcessing.class, new ExecuteElementProcessing());
        InstanceRecording.recordInstance(FindMyElements.class, new FindMyElements());
        InstanceRecording.recordInstance(Mobile.class, new Mobile());
        InstanceRecording.recordInstance(Ng.class, new Ng());
        InstanceRecording.recordInstance(MouseActions.class, new MouseActions());
        InstanceRecording.recordInstance(Element.class, new ElementAPI());
        InstanceRecording.recordInstance(Is.class, new Is());
        InstanceRecording.recordInstance(Click.class, new Click());
        InstanceRecording.recordInstance(EnterText.class, new EnterText());
        InstanceRecording.recordInstance(Scroll.class, new Scroll());
        InstanceRecording.recordInstance(DropDown.class, new DropDown());
        InstanceRecording.recordInstance(ElementReference.class, new ElementReference());
    }
}
