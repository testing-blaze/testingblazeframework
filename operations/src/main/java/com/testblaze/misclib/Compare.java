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
package com.testblaze.misclib;

import com.testblaze.actionsfactory.type.AddOns;
import com.testblaze.controller.DeviceBucket;
import com.testblaze.objects.Elements;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.objects.TwoColumnSorting;
import com.testblaze.register.i;
import com.testblaze.report.LogLevel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.Assert;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class Compare {
    private Image image;
    private WebDriver driver;

    public Compare() {
        this.image = new Image();
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

    /**
     * perform various functions on image files
     *
     * @return
     * @author nauman.shahid
     */
    public Image image() {
        return this.image;
    }

    /**
     * provide two column list to the method. It will sort it according to second column
     * The provided information will be sorted according to the order given and will be compared with the orignal columns received in map
     *
     * @param twoColumnTable Provide map with index number and column values as lists: columns will have first String values and second with integer values
     * @param sortingOrder   Ascending or Descending. Default will be Ascending
     * @return true if the sorting order of the columns received is correct as per the sorting order passed
     * @author nauman.shahid
     */
    public boolean sortAndCompareWebTableWithTwoColumns(Map<Integer, List<Elements>> twoColumnTable, String sortingOrder) {
        boolean flag = false;
        List<TwoColumnSorting> runtimeSortedList = new ArrayList<TwoColumnSorting>();
        List<TwoColumnSorting> orignalTwoColumnList = new ArrayList<TwoColumnSorting>();
        List<Elements> rows_table1 = twoColumnTable.get(1);
        List<Elements> rows_table2 = twoColumnTable.get(2);
        for (int i = 0; i < rows_table1.size(); i++) {
            runtimeSortedList.add(new TwoColumnSorting(rows_table1.get(i).getText(), Integer.parseInt(rows_table2.get(i).getText())));
        }
        orignalTwoColumnList.addAll(runtimeSortedList);
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO,"Below is the original table received from web");
        orignalTwoColumnList.stream().forEach(myList -> i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, myList.getPair()));
        if ("Descending".equalsIgnoreCase(sortingOrder)) {
            Comparator<TwoColumnSorting> customComparison = Comparator.comparingInt(sort -> sort.value);
            Collections.sort(runtimeSortedList, customComparison.reversed());

        } else if ("Ascending".equalsIgnoreCase(sortingOrder)) {
            Comparator<TwoColumnSorting> customComparison = Comparator.comparingInt(sort -> sort.value);
            Collections.sort(runtimeSortedList,customComparison);

        }
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO,"Below is the sorted table by Test Blaze");
        runtimeSortedList.stream().forEach(myList -> i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, myList.getPair()));

        flag = runtimeSortedList.equals(orignalTwoColumnList);
        return flag;
    }

    /**
     * Provide any List of WebElements to validate sorting order
     *
     * @param rows_table
     * @param sorting    Ascending:Descending
     * @return true
     * @author Monu.Kumar
     */
    public Boolean sortAndCompareOneTableColumn(List<Elements> rows_table, String sorting) {
        boolean flag = false;
        try {
            ArrayList<String> obtainedList = new ArrayList<>();
            for (Elements we : rows_table) {
                obtainedList.add(we.getText().toLowerCase());
            }
            ArrayList<String> sortedList = new ArrayList<>();
            for (String s : obtainedList) {
                sortedList.add(s.toLowerCase());
            }
            if (sorting.equalsIgnoreCase("Ascending")) {
                Collections.sort(sortedList);
            } else if (sorting.equalsIgnoreCase("Descending")) {
                Collections.sort(sortedList, Collections.reverseOrder());
            }
            flag = sortedList.equals(obtainedList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * Play with images and also compare different images
     *
     * @author nauman.shahid
     */
    public static final class Image {

        /**
         * compare two Specific element whose screen
         *
         * @param imageName with extension
         * @return
         * @throws IOException
         */
        public boolean isElementImage(WebElement element, String imageName) throws IOException {
            return isElementImage(element, imageName, 0);
        }

        /**
         * compare two Specific element whose screen
         *
         * @param imageName with extension
         * @return
         * @throws IOException
         */
        public boolean isElementImage(WebElement element, String imageName, Integer tolerance) throws IOException {
            return compareTwoImages(ImageIO.read(new AddOns().getResources(imageName)), new AddOns().convertImageFileToBufferedImage(i.amPerforming().snapShot().getlementScreenShot(element)), tolerance);
        }

        /**
         * compare two full screen shots
         *
         * @param imageName
         * @return
         * @throws IOException
         */
        public boolean isFullImage(String imageName) throws IOException {
            return isFullImage(imageName, 0);
        }

        /**
         * compare two full screen shots
         *
         * @param imageName
         * @return
         * @throws IOException
         */
        public boolean isFullImage(String imageName, int tolerance) throws IOException {
            return compareTwoImages(ImageIO.read(new AddOns().getResources(imageName)), i.amPerforming().addOnsTo().convertImageFileToBufferedImage(i.amPerforming().snapShot().getScreenshot()), tolerance);
        }

        /**
         * compare two images are same or not
         *
         * @param image1WithFullFilePath
         * @param image2WithFullFilePath
         * @return true if they are same
         * @throws IOException
         */
        public boolean isImagesSame(String image1WithFullFilePath, String image2WithFullFilePath) throws IOException {
            return isImagesSame(image1WithFullFilePath, image2WithFullFilePath, 0);
        }

        /**
         * compare two images are same or not
         *
         * @param image1WithFullFilePath
         * @param image2WithFullFilePath
         * @return true if they are same
         * @throws IOException
         */
        public boolean isImagesSame(String image1WithFullFilePath, String image2WithFullFilePath, int tolerance) throws IOException {
            return compareTwoImages(ImageIO.read(new File(image1WithFullFilePath)), ImageIO.read(new File(image2WithFullFilePath)), tolerance);
        }

        /**
         *
         * @param image1 The first image to compare
         * @param image2 The second image to compare
         * @param tolerance How many points may be different between the two images before the
         *                  images are considered "different", as a percentage.
         *                  (i.e. 20 means they can differ in at most 20% of their points)
         * @return
         */
        private boolean compareTwoImages(BufferedImage image1, BufferedImage image2, int tolerance) {
            Assert.isTrue(tolerance >= 0 && tolerance <= 100, "The tolerance for comparing images must be a percentage between 0 and 100.");
            ImageDiff diff = new ImageDiffer().makeDiff(image1, image2)
                    .withDiffSizeTrigger((int) ((image1.getHeight() * (image1.getWidth()) * (tolerance / 100.00))));
            return !diff.hasDiff();
        }

    }
}
