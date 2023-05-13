package org.main;

import analysis.*;
import configuration.Configuration;
import csvreader.DomainCsvReader;
import database.DbWriter;
import model.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class);
    private AnalysisResult analysisResult;
    private WebDriver driver;
    private List<AnalysisStep> steps;
    private DbWriter dbWriter;
    private DomainCsvReader csvReader;

    public Controller() {
        this.analysisResult=new AnalysisResult();
        this.dbWriter=new DbWriter();
        this.csvReader=new DomainCsvReader();

        this.steps=new ArrayList<AnalysisStep>();
        steps.add(new CookieCleaner());
        steps.add(new WebsiteCaller());
        steps.add(new InternetProtocolAnalyzer());
        steps.add(new CookieReader());
    }

    public void doAnalysis(){

        setupWebDriver();

        fetchMyIP();

        logger.info("Setting csvReader");
        csvReader.initializeReader(Configuration.NUMBER_OF_WEBSITES);
        csvReader.setToBeginning();

        for (int i = 0; i < Configuration.NUMBER_OF_WEBSITES; i++) {
            logger.info("Analyzing Website # "+(i+1));
            String url = csvReader.readNext();
            logger.info("\tRead URL: "+url);
            Website website = new Website(url);
            int websiteRank= csvReader.getCurrentIndex()+1;
            website.setWebsiteRank(websiteRank);
            analyzeWebsite(website);

        }

        dbWriter.writeResultToDatabase(analysisResult);

        closeWebDriver();
    }

    private void setupWebDriver(){
        logger.info("Setting up Webdriver");

        System.setProperty("webdriver.chrome.driver", Configuration.WEBDRIVER_PATH);

        ChromeOptions options=getChromeOptions();

        if(options==null){
            driver=new ChromeDriver();
        }
        else{
            driver=new ChromeDriver(options);
        }
        logger.info("Done setting up Webdriver");
    }

    private ChromeOptions getChromeOptions(){
        ChromeOptions options=new ChromeOptions();
        boolean optionsIsFilled=false;

        //Set whether driver is headless
        if(Configuration.WEBDRIVER_IS_HEADLESS){
            options.addArguments("--headless");
            optionsIsFilled=true;
        }

        //Set VPN
        if(Configuration.PROXY_IS_USED){
            Proxy p=new Proxy();
            p.setHttpProxy(Configuration.PROXY);
            options.setCapability("proxy",p);

            //options.addArguments("--proxy-server=" + Configuration.PROXY);
            optionsIsFilled=true;
        }

        if(optionsIsFilled)
            return options;
        else
            return null;
    }

    private void analyzeWebsite(Website website){
        Request request=new Request(Configuration.REGION_TO_ANALYZE);

        for (AnalysisStep step : steps) {
            step.execute(driver, website, request);
        }

        analysisResult.addRequest(website,request);
    }

    private void writeResultToDatabase(){
        dbWriter.writeResultToDatabase(analysisResult);
    }

    private void closeWebDriver(){
        driver.close();
        driver.quit();
    }

    private void fetchMyIP(){
        logger.info("Fetching my ip adddress");
        String result="";

        try {
            URL urlObj = new URL(Configuration.MY_IP_API);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner sc = new Scanner(conn.getInputStream());
                result = sc.nextLine();

            } else {
                logger.error("Error when fetching My IP address");
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        analysisResult.setUsedIP(result);
        logger.info("Fetched ip adddress: "+result);
    }
}
