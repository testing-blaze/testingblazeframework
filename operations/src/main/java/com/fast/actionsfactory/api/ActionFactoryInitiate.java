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
package com.fast.actionsfactory.api;


import com.fast.actionsfactory.abstracts.Action;
import com.fast.actionsfactory.abstracts.Element;
import com.fast.actionsfactory.abstracts.ElementProcessing;
import com.fast.actionsfactory.abstracts.LocatorProcessing;
import com.fast.actionsfactory.elementfunctions.FindMyElements;
import com.fast.actionsfactory.elementfunctions.JavaScript;
import com.fast.actionsfactory.elementfunctions.Mobile;
import com.fast.actionsfactory.elementfunctions.MouseActions;
import com.fast.actionsfactory.elementfunctions.Ng;
import com.fast.actionsfactory.elementfunctions.Waits;
import com.fast.actionsfactory.processing.ExecuteElementProcessing;
import com.fast.actionsfactory.processing.ExecuteLocatorProcessing;
import com.fast.actionsfactory.type.Click;
import com.fast.actionsfactory.type.DropDown;
import com.fast.actionsfactory.type.ElementReference;
import com.fast.actionsfactory.type.EnterText;
import com.fast.actionsfactory.type.Is;
import com.fast.actionsfactory.type.Scroll;
import com.fast.objects.InstanceRecording;

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
