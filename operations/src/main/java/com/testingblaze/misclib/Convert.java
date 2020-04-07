/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Framework [BDD] .
 *
 * Testing Blaze Automation Framework is licensed under the Apache License, Version
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

import org.openqa.selenium.By;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public final class Convert {
    /**
     * @param tableToConvert
     * @return
     * @author john.phillips
     * @warning upgrade to io.cucumber gave error on List<List<String>> tableCells = tableToConvert.cells(0); Hence it is udpated. Sanity testing to be done
     */
    public Map<String, Map<String, String>> listOfListsToMapOfMaps(List<List<String>> tableToConvert) {
        Map<String, Map<String, String>> convertedTable = new LinkedHashMap<>();
        for (int i = 1; i < tableToConvert.size(); i++) {
            Map<String, String> convertedRow = new LinkedHashMap<>();
            for (int j = 1; j < tableToConvert.get(i).size(); j++) {
                convertedRow.put(tableToConvert.get(0).get(j), tableToConvert.get(i).get(j));
            }
            convertedTable.put(tableToConvert.get(i).get(0), convertedRow);
        }
        return convertedTable;
    }

    /**
     * Convert any image to byte[]
     * @param filePathToRead
     * @param imageType
     * @return
     * @author nauman.shahid
     */
    public byte[] imageToByteArray(String filePathToRead, String imageType) {
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(new File(filePathToRead));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, imageType, bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
       return bos.toByteArray();
    }

    /**
     * convert image to byte array
     * @param bufferedImage
     * @param imageType
     * @return
     * @author nauman.shahid
     */
    public byte[] imageToByteArray(BufferedImage bufferedImage, String imageType) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, imageType, byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Reverses a call to By.toString()
     * i.e.
     * String byToString = By.xpath("//some//xpath").toString();
     * By stringToBy = convertToBy(byToString);
     */
    public By stringToBy(String byLocator) {
        By stringConvertedToBy = null;
        if (byLocator.contains("By.xpath:")) {
            stringConvertedToBy = By.xpath(byLocator.substring("By.xpath:".length()));
        } else if (byLocator.contains("By.id:")) {
            stringConvertedToBy = By.id(byLocator.substring("By.id:".length()));
        } else if (byLocator.contains("By.className:")) {
            stringConvertedToBy = By.className(byLocator.substring("By.className:".length()));
        } else if (byLocator.contains("By.cssSelector:")) {
            stringConvertedToBy = By.cssSelector(byLocator.substring("By.cssSelector:".length()));
        } else if (byLocator.contains("By.tagName:")) {
            stringConvertedToBy = By.tagName(byLocator.substring("By.tagName:".length()));
        } else if (byLocator.contains("By.linkText:")) {
            stringConvertedToBy = By.linkText(byLocator.substring("By.linkText:".length()));
        } else if (byLocator.contains("By.partialLinkText:")) {
            stringConvertedToBy = By.partialLinkText(byLocator.substring("By.partialLinkText:".length()));
        } else if (byLocator.contains("By.name:")) {
            stringConvertedToBy = By.name(byLocator.substring("By.name:".length()));
        }
        return stringConvertedToBy;
    }
}

