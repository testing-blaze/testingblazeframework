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
package com.fast.controller;

import com.fast.actionsfactory.elementfunctions.Mobile;
import com.fast.actionsfactory.elementfunctions.Waits;
import com.fast.actionsfactory.type.AddOns;
import com.fast.actionsfactory.type.Click;
import com.fast.actionsfactory.type.DropDown;
import com.fast.actionsfactory.type.ElementReference;
import com.fast.actionsfactory.type.EnterText;
import com.fast.actionsfactory.type.Fetch;
import com.fast.actionsfactory.type.Is;
import com.fast.actionsfactory.type.Scroll;
import com.fast.http.RestfulWebServices;
import com.fast.misclib.Compare;
import com.fast.misclib.Convert;
import com.fast.misclib.Cookies;
import com.fast.misclib.Emails;
import com.fast.misclib.FileHandler;
import com.fast.misclib.Properties_Logs;
import com.fast.misclib.RobotActions;
import com.fast.misclib.Sanitize;
import com.fast.misclib.ScreenCapture;
import com.fast.misclib.SwitchTo;
import com.fast.objects.InstanceRecording;
import com.fast.report.ReportController;
import org.assertj.core.api.SoftAssertions;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Main handler class to handle all method library classes
 */

public final class CoreHandlers {
    private Properties_Logs pl;
    private RobotActions ra;
    private ScreenCapture sc;
    private Sanitize sanitize;
    private Waits wait;
    private RestfulWebServices rWebServices;
    private SwitchTo switchTo;
    private Cookies cookies;
    private Convert convert;
    private FileHandler fileHandler;
    private Compare compare;
    private ReportController reportController;
    private Emails email;
    private Fetch get;
    private Click click;
    private EnterText enterText;
    private Scroll scroll;
    private DropDown select;
    private AddOns miscellaneous;
    private Is is;
    private ElementReference elementRef;
    private SoftAssertions softAssertions;
    private Mobile mobile;

    public Click click() {
        if (click == null) {
            click = InstanceRecording.getInstance(Click.class);
        }
        return click;
    }

    public EnterText enterText() {
        if (enterText == null) {
            enterText = InstanceRecording.getInstance(EnterText.class);
        }
        return enterText;
    }

    public Scroll scroll() {
        if (scroll == null) {
            scroll = InstanceRecording.getInstance(Scroll.class);
        }
        return scroll;
    }

    public DropDown select() {
        if (select == null) {
            select = InstanceRecording.getInstance(DropDown.class);
        }
        return select;
    }

    public SoftAssertions assertion() {
        if(softAssertions == null) {
            softAssertions = new SoftAssertions();
        }
        return softAssertions;
    }

    public Mobile.MobileAccessories mobileAccessories() {
        if(mobile == null) mobile = InstanceRecording.getInstance(Mobile.class);
        return mobile.MobileAccessories();
    }

    public AddOns addOns() {
        if (miscellaneous == null) {
            miscellaneous = InstanceRecording.getInstance(AddOns.class);
        }
        return miscellaneous;
    }

    public Is is() {
        if (is == null) {
            is = InstanceRecording.getInstance(Is.class);
        }
        return is;
    }

    public ElementReference elementReference() {
        if (elementRef == null) {
            elementRef = InstanceRecording.getInstance(ElementReference.class);
        }
        return elementRef;
    }

    /**
     * returns a clean string and xpath
     *
     * @return string
     * @author nauman.shahid
     */
    public Sanitize xpath() {
        if (sanitize == null) {
            sanitize = new Sanitize();
        }
        return sanitize;
    }

    /**
     * Handle different explicit and implicit waits
     *
     * @return differennt waits
     * @author nauman.shahid
     */
    public Waits WaitFor() {
        if (wait == null) {
            wait = InstanceRecording.getInstance(Waits.class);
        }
        return wait;
    }

    /**
     * access web services
     *
     * @author nauman.shahid
     */
    public RestfulWebServices apiCall() {
        if (rWebServices == null) rWebServices = new RestfulWebServices();
        return rWebServices;
    }

    /**
     * Access various libraries inluding webpage and elements
     *
     * @return
     * @author nauman.shahid
     */
    public Fetch get() {
        if (get == null) {
            get = new Fetch();
        }
        return get;
    }

    /**
     * write report. Only allowed to add addition reporting logs in special circumstances
     *
     * @return report
     * @author nauman.shahid
     */
    public ReportController report() {
        if (reportController == null) {
            reportController = new ReportController();
        }
        return reportController;
    }

    /**
     * access properties file : Place properties files in src/test/resources
     *
     * @return
     * @author nauman.shahid
     */
    public Properties_Logs Properties() {
        if (pl == null) {
            pl = new Properties_Logs();
        }
        return pl;
    }

    /**
     * java robot class to simulate hardware actions - Keyboard , mouse
     *
     * @return
     * @author nauman.shahid
     */
    public RobotActions RobotActions() {
        if (ra == null) {
            ra = new RobotActions();
        }
        return ra;
    }

    /**
     * take screen shots
     *
     * @return
     */
    public ScreenCapture screenCapture() {
        if (sc == null) {
            sc = new ScreenCapture();
        }
        return sc;
    }

    /**
     * switch to frames and alerts
     *
     * @return
     * @author nauman.shahid
     */
    public SwitchTo switchTo() {
        if (switchTo == null) {
            switchTo = new SwitchTo();
        }
        return switchTo;
    }

    /**
     * add,delete cookies
     *
     * @return
     * @author nauman.shahid
     */
    public Cookies eatCookies() {
        if (cookies == null) {
            cookies = new Cookies();
        }
        return cookies;
    }

    public Convert convert() {
        if (convert == null) {
            convert = new Convert();
        }
        return convert;
    }

    /**
     * Handles all type of file related ops. excel, adobe, etc
     *
     * @return different file handling including excel,adobe ,json, image.
     * @author nauman.shahid
     */
    public FileHandler file() {
        if (fileHandler == null) {
            fileHandler = new FileHandler();
        }
        return fileHandler;
    }

    /**
     * handle webElement related functionalities
     *
     * @return
     * @author nauman.shahid
     */
    public Compare compare() {
        if (compare == null) {
            compare = new Compare();
        }
        return compare;
    }

    /**
     * access emails
     *
     * @return email library
     * @author jitendra.pisal
     */
    public Emails email() {
        if (email == null) {
            email = new Emails();
        }
        return email;
    }

}
