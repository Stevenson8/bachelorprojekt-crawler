package org.main;

import analysis.*;
import configuration.Configuration;
import database.DbWriter;
import model.AnalysisResult;
import model.Website;
import model.WebsiteResult;
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

    public Controller() {
        this.analysisResult=new AnalysisResult();
        this.dbWriter=new DbWriter();

        this.steps=new ArrayList<AnalysisStep>();
        steps.add(new CookieCleaner());
        steps.add(new VpnSetter());
        steps.add(new WebsiteRedirector());
        steps.add(new InternetProtocolAnalyzer());
        steps.add(new CookieReader());
    }

    public void doAnalysis(int numberOfWebsites){

        setupWebDriver();

        for(int i=0;i<numberOfWebsites;i++){
            String url=readNextWebsite();
            Website website=new Website(url);
            analyzeWebsite(website);
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

    private String readNextWebsite(){
        //Todo
        return "";
    }

    private void analyzeWebsite(Website website){

        WebsiteResult result=new WebsiteResult();

        while(!result.isFinished()){
            for(AnalysisStep step : steps){
                step.execute(driver, result);
            }
        }
    }

    private void writeResultToDatabase(){
        //Todo
    }

    private void closeWebDriver(){
        driver.close();
        driver.quit();
    }
}
