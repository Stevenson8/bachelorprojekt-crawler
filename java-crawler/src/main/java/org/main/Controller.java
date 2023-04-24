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

        for(ERegion region : Configuration.REGIONS_TO_ANALYZE) {

            setupVPN(region);
            csvReader.setToBeginning();

            for (int i = 0; i < Configuration.NUMBER_OF_WEBSITES; i++) {
                String url = csvReader.readNext();
                Website website = new Website(url);
                analyzeWebsite(website,region);
            }
        }

        dbWriter.writeResultToDatabase(analysisResult);

        closeWebDriver();
    }

    private void setupWebDriver(){

        System.setProperty("webdriver.chrome.driver", Configuration.WEBDRIVER_URL);

        //Set whether driver is headless
        if(Configuration.WEBDRIVER_IS_HEADLESS){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
        }
        else{
            driver=new ChromeDriver();
        }
    }

    private void setupVPN(ERegion region){
        //Todo
    }

    private void analyzeWebsite(Website website,ERegion region){
        Request request=new Request(region);

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
