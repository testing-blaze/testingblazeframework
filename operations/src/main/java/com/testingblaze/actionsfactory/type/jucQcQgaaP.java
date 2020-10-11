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
import com.testingblaze.misclib.KeysHandler;
import com.testingblaze.objects.InstanceRecording;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

public class jucQcQgaaP {
    private JavaScript javaScript;
    private Element elementApi;
    private Action executeAction;
    private KeysHandler key;

    public jucQcQgaaP() {
        elementApi = InstanceRecording.getInstance(Element.class);
        executeAction = InstanceRecording.getInstance(Action.class);
        this.javaScript = InstanceRecording.getInstance(JavaScript.class);
        this.key = new KeysHandler();
    }

    /**
     * generates random number
     * @return integer random number
     * @author jitendra.pisal
     */
    public int getRandomNumber() {
        return (int) getRandomNumberInRange(1000, 50000) + (int) getRandomNumberInRange(51000, 90000);
    }

    /**
     * generates random number
     * @return String random number
     * @author jitendra.pisal
     */
    public String getStringRandomNumber() {
        return Integer.toString((int) getRandomNumberInRange(1000, 50000)) + (int) getRandomNumberInRange(51000, 90000);
    }

    /**
     * Get random number within a range.
     *
     * @param minimum
     * @param maximum
     * @param <T>     Handles Double and int at the moment
     * @return random number
     * @author nauman.shahid
     */
    public synchronized  <T extends Number> Number getRandomNumberInRange(T minimum, T maximum) {
        if (minimum instanceof Double) {
            return ThreadLocalRandom.current().nextDouble(minimum.doubleValue(), maximum.doubleValue());
        } else {
            return ThreadLocalRandom.current().nextInt(minimum.intValue(), maximum.intValue());
        }
    }

    /**
     * get access to keyboard
     *
     * @return
     */
    public KeysHandler getKeyBoard() {
        return key;
    }

    /**
     * To blink color on specifc element using java script
     */
    public void flashColor(WebElement element, String color, int blinkNumber) throws InterruptedException {
        javaScript.flashColor(element, color, blinkNumber);
    }

    /**
     * get a rounded value to a single decimal
     * @param value
     * @return
     */
    public double roundDouble (double value) {
        return Math.round(value * 10)/10.0;
    }

    /**
     * Setup a universal variable that stays alive all through the JVM life
     * @param variableName
     * @param variable
     * @author nauman.shahid
     */
    public void setJvmVariable(StringSelection variableName, String variable){
        variableName = new StringSelection(variable);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(variableName,variableName);
    }

    /**
     * retrieve the value of the variable stored universally
     * @param variableName
     * @return string
     * @author nauman.shahid
     */
    public String getJvmVariable(StringSelection variableName){
        return Toolkit.getDefaultToolkit().getSystemClipboard().getContents(variableName).toString();
    }

    /**
     * get input stream of a file to read
     * @param fileNameWithExtension
     * @return
     */

    public InputStream getResources(String fileNameWithExtension) {
        return getClass().getResourceAsStream(File.separatorChar+fileNameWithExtension);
    }

    /**
     * convert a file image to buffered image
     * @param fileName
     * @return
     */
    public BufferedImage convertImageFileToBufferedImage(File fileName) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage=ImageIO.read(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    /**
     * upload on webPage , mobilePage , AngularPage
     * @param locator Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void uploadFile(T locator,String input) {
        uploadFile(locator,input, true);
    }

    /**
     * upload on webPage , mobilePage , AngularPage
     * @param locator Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void uploadFile(T locator,String input, Boolean processing) {
        executeAction.doIt(elementApi.locator(locator,processing),input);
    }

}
