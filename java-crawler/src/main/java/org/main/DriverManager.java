package org.main;

import configuration.Configuration;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DriverManager {
    public static List<ChromeDriver> currentDriver=new ArrayList<ChromeDriver>() {{
        add(createChromeDriver());
    }};;
    private static boolean driverIsClosed=false;
    public DriverManager(){

    }

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

    private static ChromeDriver createChromeDriver(){
        ChromeDriver newDriver;
        ChromeOptions options = getChromeOptions();

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
