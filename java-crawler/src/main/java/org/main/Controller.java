package org.main;

import analysis.*;
import configuration.Configuration;
import csvreader.DomainCsvReader;
import database.DbWriter;
import model.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class Controller {

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
        steps.add(new WebsiteRedirector());
        steps.add(new InternetProtocolAnalyzer());
        steps.add(new CookieReader());
    }

    public void doAnalysis(){

        setupWebDriver();

        csvReader.initializeReader(Configuration.NUMBER_OF_WEBSITES);

        csvReader.setToBeginning();

        for (int i = 0; i < Configuration.NUMBER_OF_WEBSITES; i++) {
            String url = csvReader.readNext();
            Website website = new Website(url);
            analyzeWebsite(website);
        }

        dbWriter.writeResultToDatabase(analysisResult);

        closeWebDriver();
    }

    private void setupWebDriver(){

        System.setProperty("webdriver.chrome.driver", Configuration.WEBDRIVER_PATH);

        ChromeOptions options=getChromeOptions();

        if(options==null){
            driver=new ChromeDriver();
        }
        else{
            driver=new ChromeDriver(options);
        }
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
            options.addArguments("--proxy-server=" + Configuration.PROXY);
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
            step.execute(driver, request);
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
}
