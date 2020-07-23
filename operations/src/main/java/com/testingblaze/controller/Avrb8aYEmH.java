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
package com.testingblaze.controller;

import com.testingblaze.actionsfactory.elementfunctions.Mobile;
import com.testingblaze.actionsfactory.elementfunctions.Waits;
import com.testingblaze.actionsfactory.type.jucQcQgaaP;
import com.testingblaze.actionsfactory.type.BQUnpmlimY;
import com.testingblaze.actionsfactory.type.DropDown;
import com.testingblaze.actionsfactory.type.GmGEaSpros;
import com.testingblaze.actionsfactory.type.NJIc1dLxYv;
import com.testingblaze.actionsfactory.type.Fetch;
import com.testingblaze.actionsfactory.type.Is;
import com.testingblaze.actionsfactory.type.Scroll;
import com.testingblaze.http.RestfulWebServices;
import com.testingblaze.misclib.*;
import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.report.ReportController;
import org.assertj.core.api.SoftAssertions;

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Main handler class to handle all method library classes
 */

public final class Avrb8aYEmH {
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
    private BQUnpmlimY click;
    private NJIc1dLxYv enterText;
    private Scroll scroll;
    private DropDown select;
    private jucQcQgaaP miscellaneous;
    private Is is;
    private GmGEaSpros elementRef;
    private SoftAssertions softAssertions;
    private Mobile mobile;
    private Browser browser;
    private Dates dates;

    public BQUnpmlimY click() {
        if (click == null) {
            click = InstanceRecording.getInstance(BQUnpmlimY.class);
        }
        return click;
    }

    public NJIc1dLxYv textInput() {
        if (enterText == null) {
            enterText = InstanceRecording.getInstance(NJIc1dLxYv.class);
        }
        return enterText;
    }

    public Scroll scroll() {
        if (scroll == null) {
            scroll = InstanceRecording.getInstance(Scroll.class);
        }
        return scroll;
    }

    public DropDown dropDownSelection() {
        if (select == null) {
            select = InstanceRecording.getInstance(DropDown.class);
        }
        return select;
    }

    public SoftAssertions assertionsTo() {
        if (softAssertions == null) {
            softAssertions = new SoftAssertions();
        }
        return softAssertions;
    }

    public Mobile.MobileAccessories mobileOperations() {
        if (mobile == null) mobile = InstanceRecording.getInstance(Mobile.class);
        return mobile.MobileAccessories();
    }

    public jucQcQgaaP addOnsTo() {
        if (miscellaneous == null) {
            miscellaneous = InstanceRecording.getInstance(jucQcQgaaP.class);
        }
        return miscellaneous;
    }

    public Is checkToSee() {
        if (is == null) {
            is = InstanceRecording.getInstance(Is.class);
        }
        return is;
    }

    public GmGEaSpros getElementReference() {
        if (elementRef == null) {
            elementRef = InstanceRecording.getInstance(GmGEaSpros.class);
        }
        return elementRef;
    }

    /**
     * returns a clean string and xpath
     *
     * @return string
     * @author nauman.shahid
     */
    public Sanitize sanityOfXpathTo() {
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
    public Waits waitFor() {
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
    public RestfulWebServices restHttp() {
        if (rWebServices == null) rWebServices = new RestfulWebServices();
        return rWebServices;
    }

    /**
     * Access various libraries inluding webpage and elements
     *
     * @return
     * @author nauman.shahid
     */
    public Fetch actionToGet() {
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
    public ReportController updatingOfReportWith() {
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
    public Properties_Logs propertiesFileOperationsTo() {
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
    public RobotActions robotActionsTo() {
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
    public ScreenCapture snapShotTo() {
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
    public Cookies cookiesOperationsTo() {
        if (cookies == null) {
            cookies = new Cookies();
        }
        return cookies;
    }

    public Convert conversionOf() {
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
    public FileHandler fileHandling() {
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
    public Compare comparisonOf() {
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
    public Emails emailOperationsTo() {
        if (email == null) {
            email = new Emails();
        }
        return email;
    }

    /**
     * access browser
     *
     * @return brwoser library
     * @author john.phillips
     */
    public Browser browserOperationsTo() {
        if (browser == null) {
            browser = new Browser();
        }
        return browser;
    }

    /**
     * access browser
     *
     * @return brwoser library
     * @author john.phillips
     */
    public Dates dateOperationsTo() {
        if (dates == null) {
            dates = new Dates();
        }
        return dates;
    }

}
