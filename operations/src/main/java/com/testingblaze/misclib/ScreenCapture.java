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
package com.testingblaze.misclib;


import com.testingblaze.controller.DeviceBucket;
import com.testingblaze.objects.InstanceRecording;
import org.apache.commons.io.FileUtils;
import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.monte.media.AudioFormatKeys.EncodingKey;
import static org.monte.media.AudioFormatKeys.FrameRateKey;
import static org.monte.media.AudioFormatKeys.KeyFrameIntervalKey;
import static org.monte.media.AudioFormatKeys.MIME_QUICKTIME;
import static org.monte.media.AudioFormatKeys.MediaTypeKey;
import static org.monte.media.AudioFormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.MediaType;
import static org.monte.media.VideoFormatKeys.*;

/**
 * @author nauman.shahid
 * @category Handles taking screen shots
 */

public final class ScreenCapture {
    private WebDriver driver;
    static Calendar calendar = Calendar.getInstance();
    static SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
    static ScreenRecorder screenRecorder;
    static Random random = new Random();

    public ScreenCapture() {
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

    /**
     * takes screen shot and associate name to it
     *
     * @author nauman.shahid
     */
    public String captureScreen(String fileName) {
        if (fileName == "") {
            fileName = "blank";
        }
        File destFile = null;
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "target/automation-report/";
            destFile = new File(
                    reportDirectory + fileName + "_" + formater.format(calendar.getTime()) + ".png");
            FileUtils.copyFile(scrFile, destFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return destFile.toString();
    }

    /**
     * capture screen shots and associate a name to it with unique additional naming
     *
     * @author nauman.shahid
     */
    public void captureScreenshot(String screenshotName) {
        DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
        Date date = new Date();
        df.format(date);
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(System.getProperty("user.dir") + "target/Automation-Report/" + screenshotName
                    + " " + df.format(date) + ".png"));
        } catch (Exception e) {
        }
    }

    /**
     * get full screen shot file
     *
     * @author nauman.shahid
     * @return
     */
    public File getScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    /**
     * Take screen shot of the specific element and saves to /target/custom_screenshots
     *
     * @param element   : Specific element whose screen shot is required
     * @param imageName : image name
     * @throws IOException
     */
    public void takeElementScreenShot(WebElement element, String imageName) {
        File elementSS = element.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(element.getScreenshotAs(OutputType.FILE), new File(System.getProperty("user.dir")
                    + File.separator+"target"+File.separator+"Automation-Report"+File.separator + imageName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Take screen shot of the specific element and saves to /target/custom_screenshots
     *
     * @param element   : Specific element whose screen shot is required
     * @throws IOException
     * @return
     */
    public File getlementScreenShot(WebElement element) {
        return element.getScreenshotAs(OutputType.FILE);
    }
    
    /**
     * used to record video
     *
     * @throws IOException
     * @throws AWTException
     * @author nauman.shahid
     */
    public static void startRecordVideo() throws IOException, AWTException {
        createRecordingInstance();
        screenRecorder.start();
    }

    /**
     * stop video recording
     *
     * @throws IOException
     * @author nauman.shahid
     */
    public static void stopRecordVideo() throws IOException {
        screenRecorder.stop();
    }

    public BufferedImage captureFullScreenShot() {
        return new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(driver).getImage();
    }

    private static GraphicsConfiguration setUpRecordVideo() {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        return gc;
    }

    private static void createRecordingInstance() throws IOException, AWTException {
        String timeStamp = formater.format(calendar.getTime()) + "_" + random.nextInt(10000) + random.nextInt(10);
        screenRecorder = new ScreenRecorder(setUpRecordVideo(), null,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_QUICKTIME),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_QUICKTIME_JPEG,
                        CompressorNameKey, ENCODING_QUICKTIME_JPEG, DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                        QualityKey, 0.5f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey,
                        Rational.valueOf(30)),
                null, new File(System.getProperty("user.dir") + "/target/Automation-Report/test_videos/rei-bdd-test" + timeStamp
                + ".avi"));
    }

}
