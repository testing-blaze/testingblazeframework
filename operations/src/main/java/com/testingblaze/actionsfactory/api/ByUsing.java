package com.testingblaze.actionsfactory.api;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileSelector;

public class ByUsing extends MobileBy {
    protected ByUsing(MobileSelector selector, String locatorString) {
        super(selector, locatorString);
    }

    public static String healingXpathName(String xpath){
        return "By-xpath:"+xpath;
    }

    public static String healingIdName(String id){
        return "By-id:"+id;
    }

    public static String healingCssName(String css){
        return "By-css:"+css;
    }

    public static String healingMobileXpathName(String xpath){
        return "MobileBy-xpath:"+xpath;
    }

    public static String healingMobileIdName(String id){
        return "MobileBy-id:"+id;
    }

    public static String healingMobileCssName(String css){
        return "MobileBy-css:"+css;
    }


}

