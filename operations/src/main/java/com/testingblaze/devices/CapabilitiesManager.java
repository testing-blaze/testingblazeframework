package com.testingblaze.devices;


import com.testingblaze.register.EnvironmentFactory;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

public class CapabilitiesManager {

    public static ChromeOptions getChromeCapabilities() {
        ChromeOptions chromeOptions = new ChromeOptions();
        if (null != System.getProperty("browserMode") && "incognito".equalsIgnoreCase(System.getProperty("browserMode")))
            chromeOptions.addArguments("--incognito");

        chromeOptions.addArguments(
                "disable-infobars", // disabling infobars
                "--disable-extensions", // disabling extensions
                "--disable-gpu", // applicable to windows os only
                "--no-sandbox",
                "--ignore-ssl-errors=yes", // Bypass ‘Connection Is Not Private’
                "--ignore-certificate-errors" // Bypass ‘Connection Is Not Private’
        );

        if (null != System.getProperty("evaluatePerformance") && "true".equalsIgnoreCase(System.getProperty("evaluatePerformance"))) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
            chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        }

        chromeOptions.setExperimentalOption("prefs", Map.of(
                "profile.cookie_controls_mode", 0,
                "profile.block_third_party_cookies", false,
                "profile.default_content_settings.popups", 0,
                "download.prompt_for_download", "false",
                "download.directory_upgrade", "true",
                "download.default_directory", System.getProperty("user.dir") + File.separatorChar + "target"
        ));

        if (EnvironmentFactory.isHeadless()) {
            chromeOptions.addArguments(
                    "--window-size=2560,1440",
                    "--headless",
                    "--mute-audio"
            );
        }

        if (!"local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            setBrowserCapabilities(chromeOptions);
        }

        return chromeOptions;
    }

    public static FirefoxOptions getFirefoxCapabilities() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("network.cookie.cookieBehavior", 0);
        if (null != System.getProperty("browserMode") && "private".equalsIgnoreCase(System.getProperty("browserMode"))) {
            profile.setPreference("browser.privatebrowsing.autostart", true);
        }
        profile.setAssumeUntrustedCertificateIssuer(true);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", System.getProperty("user.dir") + File.pathSeparatorChar + "target");
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/plain,application/octet-stream,application/pdf,application/x-pdf,application/vnd.pdf,text/csv,application/java-archive,application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,application/msword,application/xml,application/vnd.microsoft.portable-executable");
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.helperApps.neverAsk.openFile", "");
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.closeWhenDone", true);
       profile.setPreference("pdfjs.disabled", true);
        firefoxOptions.setProfile(profile);

        if ("local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            firefoxOptions.setAcceptInsecureCerts(true);
            firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
            firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
        }

        if (EnvironmentFactory.isHeadless()) {
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            firefoxOptions.setBinary(firefoxBinary);
        }

        if (!"local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            setBrowserCapabilities(firefoxOptions);
        }

        return firefoxOptions;
    }

    public static EdgeOptions getEdgeCapabilities() {
        EdgeOptions edgeOptions = new EdgeOptions();

        edgeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        edgeOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        edgeOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        edgeOptions.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        edgeOptions.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, true);
        edgeOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

        if ("local".equalsIgnoreCase(EnvironmentFactory.getHub())) { // uncomment once switching to sel 4.0.0
            /*edgeOptions.setExperimentalOption("useAutomationExtension", false);
            edgeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            edgeOptions.setExperimentalOption("download.default_directory", System.getProperty("user.dir") + File.separatorChar+"target");
        )
            if (EnvironmentFactory.isHeadless()) {
                edgeOptions.addArguments(
                        "--window-size=2560,1440",
                        "--headless",
                        "--mute-audio"
                );
            }*/
        }

        if (!"local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            setBrowserCapabilities(edgeOptions);
        }

        return edgeOptions;
    }

    public static InternetExplorerOptions getIeCapabilities() {
        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();

        internetExplorerOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        internetExplorerOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        internetExplorerOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        internetExplorerOptions.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        internetExplorerOptions.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, true);
        internetExplorerOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

        if (!"local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            setBrowserCapabilities(internetExplorerOptions);
        }

        return internetExplorerOptions;
    }

    public static SafariOptions getSafariCapabilities() {
        SafariOptions safariOptions = new SafariOptions();

        safariOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        safariOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

        if (!"local".equalsIgnoreCase(EnvironmentFactory.getHub())) {
            setBrowserCapabilities(safariOptions);
        }

        return safariOptions;
    }

    public static DesiredCapabilities getAndroidCapabilities() {
        DesiredCapabilities androidCapabilities = new DesiredCapabilities();
        Properties loadConfigFile = new Properties();
        String filePath = System.getProperty("user.dir") + File.separatorChar + "mobileapp" + File.separatorChar;
        try {
            loadConfigFile.load(new FileReader(filePath + "appConfig.properties"));
        } catch (IOException e) {

        }
        if (EnvironmentFactory.getAppName() != null) {
            androidCapabilities.setCapability(MobileCapabilityType.APP, filePath + EnvironmentFactory.getAppName());

            try {
                androidCapabilities.setCapability("appPackage", loadConfigFile.getProperty("appPackage"));
                androidCapabilities.setCapability("appActivity", loadConfigFile.getProperty("appActivity"));
            } catch (NullPointerException e) {

            }

        } else {
            if (!"default".equalsIgnoreCase(EnvironmentFactory.getBrowserVersion())
                    && !"default".equalsIgnoreCase(EnvironmentFactory.getDriverVersion())) {
                WebDriverManager.chromedriver()
                        .browserVersion(EnvironmentFactory.getBrowserVersion())
                        .driverVersion(EnvironmentFactory.getDriverVersion())
                        .setup();
            } else if (!"default".equalsIgnoreCase(EnvironmentFactory.getBrowserVersion())) {
                WebDriverManager.chromedriver()
                        .browserVersion(EnvironmentFactory.getBrowserVersion())
                        .setup();
            } else if (!"default".equalsIgnoreCase(EnvironmentFactory.getDriverVersion())) {
                WebDriverManager.chromedriver()
                        .driverVersion(EnvironmentFactory.getDriverVersion())
                        .setup();
            } else {
                WebDriverManager.chromedriver().setup();
            }

            androidCapabilities.setCapability("chromedriverExecutable", WebDriverManager.chromedriver().getBinaryPath());
            androidCapabilities.setCapability("w3c", false);
            androidCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        }

        setMobileCapabilities(MobileDevice.ANDROID, androidCapabilities);

        return androidCapabilities;
    }

    public static DesiredCapabilities getIosCapabilities() {
        DesiredCapabilities iosCapabilities = new DesiredCapabilities();
        if (EnvironmentFactory.getAppName() != null) {
            iosCapabilities.setCapability(MobileCapabilityType.APP,
                    System.getProperty("user.dir") + File.separatorChar + "mobileapp" + File.separatorChar + EnvironmentFactory.getAppName());
        } else {
            iosCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        }

        setMobileCapabilities(MobileDevice.IOS, iosCapabilities);

        return iosCapabilities;
    }

    private static void setBrowserCapabilities(MutableCapabilities browserCapabilities) {
        browserCapabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);

        if (System.getProperty("platform") != null) {
            browserCapabilities.setCapability(CapabilityType.PLATFORM_NAME, getPlatformName());
        }

        if (System.getProperty("proxy") != null) {
            browserCapabilities.setCapability(CapabilityType.PROXY, System.getProperty("proxy"));
        }
    }

    private static void setMobileCapabilities(MobileDevice device, MutableCapabilities mobileCapabilties) {
        mobileCapabilties.setCapability(MobileCapabilityType.PLATFORM_NAME, device == MobileDevice.ANDROID ? "ANDROID" : "IOS");
        mobileCapabilties.setCapability(MobileCapabilityType.DEVICE_NAME, device == MobileDevice.ANDROID ? getAndroidDeviceName() : getIOSDeviceName());
        mobileCapabilties.setCapability(MobileCapabilityType.PLATFORM_VERSION, device == MobileDevice.ANDROID ? getAndroidVersion(): getIOSVersion());
        mobileCapabilties.setCapability(MobileCapabilityType.AUTOMATION_NAME, device == MobileDevice.ANDROID ? "uiautomator2" : "XCUITest");

        mobileCapabilties.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");
        mobileCapabilties.setCapability("locationServicesAuthorized", true);
        mobileCapabilties.setCapability("autoAcceptAlerts", true);

        mobileCapabilties.setCapability(MobileCapabilityType.ACCEPT_INSECURE_CERTS, true);
        mobileCapabilties.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS, true);
        mobileCapabilties.setCapability(MobileCapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
    }

    private  static String getAndroidVersion(){
        if(!"null".equals(System.getProperty("version"))) {
            return System.getProperty("version");
        } else return "8.0";
    }

    private static String getIOSVersion(){
        if(!"null".equals(System.getProperty("version"))) {
            return System.getProperty("version");
        } else return "11.4";
    }

    private  static String getAndroidDeviceName(){
        if(!"null".equals(System.getProperty("deviceName"))) {
            return System.getProperty("deviceName");
        } else return "Samsung";
    }

    private static String getIOSDeviceName(){
        if(!"null".equals(System.getProperty("deviceName"))) {
            return System.getProperty("deviceName");
        } else return "iPhone 8 Plus";
    }

    private static String getPlatformName() {
        String platformName = null;
        if (System.getProperty("platform").toUpperCase().contains("WINDOWS")) {
            switch (System.getProperty("version")) {
                case "8":
                    platformName = "win8";
                    break;
                case "7":
                    platformName = "vista";
                    break;
                case "10":
                default:
                    platformName = "win10";
                    break;
            }
        } else if (System.getProperty("platform").toUpperCase().contains("MAC")) {
            platformName = "mac";
        }
        return platformName;
    }

    private enum MobileDevice {ANDROID, IOS}
}
