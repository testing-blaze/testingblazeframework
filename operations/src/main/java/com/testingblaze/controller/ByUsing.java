package com.testingblaze.controller;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileSelector;

public class ByUsing extends MobileBy {
    protected ByUsing(MobileSelector selector, String locatorString) {
        super(selector, locatorString);
    }

    public static String webXpathName(String xpath){
        return "By-xpath:"+xpath;
    }

    public static String webIdName(String id){
        return "By-id:"+id;
    }

    public static String webCssName(String css){
        return "By-css:"+css;
    }

    public static String mobileXpathName(String xpath){
        return "MobileBy-xpath:"+xpath;
    }

    public static String mobileIdName(String id){
        return "MobileBy-id:"+id;
    }

    public static String mobileCssName(String css){
        return "MobileBy-css:"+css;
    }


}

