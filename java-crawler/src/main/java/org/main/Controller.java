package org.main;

import analysis.*;
import configuration.Configuration;
import csvreader.DomainCsvReader;
import database.DbWriter;
import model.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private List<AnalysisStep> steps;
    private AnalysisResult analysisResult;
    private DbWriter dbWriter;
    private DomainCsvReader csvReader;

    public Controller() {
        System.setProperty("webdriver.chrome.driver", Configuration.WEBDRIVER_PATH);

        this.analysisResult=new AnalysisResult();
        this.dbWriter=new DbWriter();
        this.csvReader=new DomainCsvReader();
        DriverManager.initializeProxy();

        this.steps=new ArrayList<AnalysisStep>();
        steps.add(new CookieCleaner());
        steps.add(new WebsiteUrlIdentifier());
        steps.add(new WebsiteCaller());
        steps.add(new WebsiteCloser());
        steps.add(new WebsiteCaller());
        steps.add(new CookieHeaderReader());

    }

    public void doAnalysis(){

        if(Configuration.FETCH_IP_ADDRESS)
            fetchMyIP();

        csvReader.initializeReader();

        while (csvReader.hasNextUrl()) {
            int websiteRank= csvReader.getCurrentRank();

            String url = csvReader.readNextUrl();

            Website website = new Website(url);
            website.setWebsiteRank(websiteRank);
            analyzeWebsite(website);
        }
        DriverManager.closeChromeDriver();
        DriverManager.closeProxy();
        dbWriter.writeResultToDatabase(analysisResult);
    }
    private void analyzeWebsite(Website website){
        Request request=new Request(Configuration.REGION_TO_ANALYZE);
        System.out.println("# "+website.getWebsiteRank()+"\t Start Analyzing...");

        for (AnalysisStep step : steps) {
            step.execute(DriverManager.getCurrentChromeDriver(), website, request);
        }

        analysisResult.addRequest(website,request);
    }

    private void fetchMyIP(){
        System.out.println("Fetching my IP Address");

        String result="";

        try {
            URL urlObj = new URL(Configuration.MY_IP_API);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner sc = new Scanner(conn.getInputStream());
                result = sc.nextLine();

            } else {
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        analysisResult.setUsedIP(result);
    }
}
