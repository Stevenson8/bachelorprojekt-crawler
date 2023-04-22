package org.main;

import analysis.*;
import model.AnalysisResult;
import model.Website;
import model.WebsiteResult;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private AnalysisResult analysisResult;
    private WebDriver driver;
    private List<AnalysisStep> steps;

    public Controller() {
        this.analysisResult=new AnalysisResult();

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

        closeWebDriver();
    }

    private void setupWebDriver(){

        //Todo
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
        //Todo
    }
}
