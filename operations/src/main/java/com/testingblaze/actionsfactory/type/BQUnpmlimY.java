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
package com.testingblaze.actionsfactory.type;

import com.testingblaze.actionsfactory.abstracts.Action;
import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.actionsfactory.elementfunctions.Mobile;
import com.testingblaze.actionsfactory.elementfunctions.MouseActions;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import io.appium.java_client.MobileBy;

public class BQUnpmlimY {
    private MouseClicks mouseClicks;
    private JavaScript javaScript;
    private Element elementApi;
    private Action executeAction;
    private Mobile mobile;

    public BQUnpmlimY() {
        elementApi = InstanceRecording.getInstance(Element.class);
        executeAction = InstanceRecording.getInstance(Action.class);
        javaScript = InstanceRecording.getInstance(JavaScript.class);
        mobile = InstanceRecording.getInstance(Mobile.class);
    }

    /**
     * click on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void on(T locator) {
        on(locator, true);
    }

    /**
     * click on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void on(T locator, Boolean processing) {
        executeAction.doIt(elementApi.locator(locator,processing));
    }


    /**
     * clicks on element using java script
     * @param locator
     * @author nauman.shahid
     */
    public <T> void withJavaScript(T locator) {
        withJavaScript(locator, true);
    }

    /**
     * clicks on element using java script
     * @param locator
     * @author nauman.shahid
     */
    public <T> void withJavaScript(T locator, Boolean processing) {
        javaScript.clickByJSWebElement(elementApi.locator(locator, processing));
    }

    /**
     * perform click using mouse actions
     *
     * @return mouse clicks
     * @author nauman.shahid
     */
    public MouseClicks withMouse() {
        if(mouseClicks == null) {
            mouseClicks = new MouseClicks();
        }
        return mouseClicks;
    }

    /**
     * @deprecated in favor of I.amPerforming.click().on(MobileBy.AndroidViewTag("the view tag")) will do the same thing
     * clicks on android specific locator strategy
     * @param locator: Android
     */
    public void onAndroidViewTag(String locator) {
        on(I.amPerforming().getElementReference().of(MobileBy.AndroidViewTag(locator)));
    }

    /**
     * @deprecated in favor of I.amPerforming.click().on(MobileBy.image(I.amPerforming().conversionOf().imageToBase64String(imageName, "png")))
     * clicks on android specific locator strategy
     * @param imageName: Android
     */
    public void onAndroidImage(String imageName) {
        on(I.amPerforming().getElementReference().of(MobileBy.image(I.amPerforming().conversionOf().imageToBase64String(imageName, "png"))));
    }

    /**
     * @deprecated in favor of I.amPerforming.click().on(MobileBy.iOSNsPredicateString(""))
     *                     and I.amPerforming.click().on(MobileBy.iOSClassChain(""))
     * clicks on IOS specific locator strategy
     * @param locator: Mobile.
     * @
     */
    public void onIosApp(String locator) {
        // to be added
    }

    public Mobile.Tap withTapOnScreen(){
        return mobile.tap();
    }

    /**
     * Inner class for web based mouse actions handling
     *
     * @author nauman.shahid
     */
    public class MouseClicks {
        private MouseActions mouseActions;

        protected MouseClicks() {
            this.mouseActions = InstanceRecording.getInstance(MouseActions.class);
        }

        /**
         * clicks on desired location based on dimension
         *
         * @author nauman.shahid
         */
        public void at(int x, int y) {
            mouseActions.mouseClick(x, y);
        }

        /**
         * clicks on desired location on web page using By
         *
         * @author nauman.shahid
         */
        public <T> void on(T locator) {
            on(locator, true);
        }

        /**
         * clicks on desired location on web page using By
         *
         * @author nauman.shahid
         */
        public <T>  void on(T locator, Boolean processing) {
            mouseActions.mouseClick(elementApi.locator(locator, processing));
        }

        /**
         * click and hold on desired location on web paget
         *
         * @param holdTimeSeconds
         * @author nauman.shahid
         */
        public <T>  void onAndHold(T locator, int holdTimeSeconds) {
            onAndHold(locator, holdTimeSeconds, true);
        }

        /**
         * click and hold on desired location on web paget
         *
         * @param holdTimeSeconds
         * @author nauman.shahid
         */
        public <T>  void onAndHold(T locator, int holdTimeSeconds, Boolean processing) {
            mouseActions.mouseClickAndHold(elementApi.locator(locator, processing), holdTimeSeconds);
        }

        /**
         * Double clicks on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void doubleClickOn(T locator) {
            doubleClickOn(locator, true);
        }

        /**
         * Double clicks on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void doubleClickOn(T locator, Boolean processing) {
            mouseActions.mouseDoubleClick(elementApi.locator(locator, processing));
        }

        /**
         * Drag and drop on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void dragAndDrop(T source, T target) {
            dragAndDrop(source,target, true);
        }


        /**
         * Drag and drop on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void dragAndDrop(T source, T target, Boolean processing) {
            mouseActions.mouseDragAndDrop(elementApi.locator(source, processing), elementApi.locator(target, processing));
        }

        /**
         * Drag and drop on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void toDragAndDrop(T source, T target) {
            toDragAndDrop(source,target, true);
        }

        /**
         * Drag and drop on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void toDragAndDrop(T source, T target, Boolean processing) {
            mouseActions.dragAndDrop(elementApi.locator(source, processing), elementApi.locator(target, processing));
        }

        /**
         * Drag and Drop specific for HTML 5. It doesnt perform any framework processing on element.
         * @param elementSourceCss
         * @param elementTargetCss
         * @author nauman.shahid
         */
        public void dragAndDropInHtml5(String elementSourceCss, String elementTargetCss) {
            mouseActions.dragAndDropInHtml5(elementSourceCss, elementTargetCss);
        }

        /**
         * Mouse right click on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void rightClickOn(T context) {
            rightClickOn(context, true);
        }


        /**
         * Mouse right click on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void rightClickOn(T context, Boolean processing) {
            mouseActions.mouseRightClick(elementApi.locator(context, processing));
        }
    }

}
