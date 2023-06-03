package org.main;

import configuration.Configuration;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class DriverManager {
    public static List<ChromeDriver> currentDriver=new ArrayList<ChromeDriver>() {{
        add(createChromeDriver());
    }};;
    private static BrowserMobProxy proxy;
    private static DesiredCapabilities capabilities;
    private static boolean driverIsClosed=false;
    public DriverManager(){ }

    public static ChromeDriver getCurrentChromeDriver(){

        if(driverIsClosed){
            currentDriver.clear();
            currentDriver.add(createChromeDriver());
            driverIsClosed=false;
        }
        return currentDriver.get(0);
    }

    public static void closeChromeDriver() {
        currentDriver.get(0).close();
        currentDriver.get(0).quit();
        DriverManager.driverIsClosed = true;
    }
    public static void closeProxy(){
        proxy.stop();
    }

    public static void initializeProxy(){
        /**
         * The following Code snippets for setting up the BrowserMob Proxy were taken from
         * https://github.com/ajautomation/GenerateHAR
         */

        //Start Proxy at an available port
        proxy= new BrowserMobProxyServer();

        proxy.start(0);

        Proxy seleniumProxy=new Proxy();
        seleniumProxy.setHttpProxy("localhost:"+proxy.getPort());
        seleniumProxy.setSslProxy("localhost:"+proxy.getPort());

        //Set the Capabilities
        capabilities=new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY,seleniumProxy);
        capabilities.acceptInsecureCerts();
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);

        //Set the Capture Types
        EnumSet<CaptureType> captureTypes=CaptureType.getAllContentCaptureTypes();
        captureTypes.addAll(CaptureType.getCookieCaptureTypes());
        captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
        captureTypes.addAll(CaptureType.getRequestCaptureTypes());
        captureTypes.addAll(CaptureType.getResponseCaptureTypes());
        proxy.setHarCaptureTypes(captureTypes);
    }

    public static void startListeningToNetwork(){
        proxy.newHar();
    }

    public static Har endListeningToNetworkAndGetHar(){
        return proxy.endHar();
    }

    private static ChromeDriver createChromeDriver(){
        ChromeDriver newDriver;
        ChromeOptions options = getChromeOptions();

        //Add capabilities of proxy to options
        options.merge(capabilities);

        newDriver = new ChromeDriver(options);

        return newDriver;
    }

    private static ChromeOptions getChromeOptions(){
        ChromeOptions options=new ChromeOptions();

        options.addArguments("--log-level=3");

        //Set whether driver is headless
        if(Configuration.WEBDRIVER_IS_HEADLESS)
            options.addArguments("--headless");

        return options;
    }
}
