package org.main;

import analysis.*;
import configuration.Configuration;
import csvreader.DomainCsvReader;
import database.DbWriter;
import model.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class);
    private AnalysisResult analysisResult;
    private List<AnalysisStep> steps;
    private DbWriter dbWriter;
    private DomainCsvReader csvReader;

    public Controller() {
        logger.info("Setting up Webdriver");
        System.setProperty("webdriver.chrome.driver", Configuration.WEBDRIVER_PATH);

        this.analysisResult=new AnalysisResult();
        this.dbWriter=new DbWriter();
        this.csvReader=new DomainCsvReader();

        this.steps=new ArrayList<AnalysisStep>();
        steps.add(new CookieCleaner());
        steps.add(new WebsiteUrlIdentifier());
        steps.add(new WebsiteCaller());
        steps.add(new WebsiteCloser());
        steps.add(new WebsiteCaller());
        steps.add(new HttpHeaderCookieReader());

    }

    public void doAnalysis(){

        if(Configuration.FETCH_IP_ADDRESS)
            fetchMyIP();

        logger.info("Setting csvReader");
        csvReader.initializeReader();

        int numberOfWebsitesToAnaylze=Configuration.WEBSITE_RANK_END-Configuration.WEBSITE_RANK_START+1;
        while (csvReader.hasNextUrl()) {
            int websiteRank= csvReader.getCurrentRank();
            logger.info("**********Analyzing Website Rank # "+websiteRank+"**********");
            String url = csvReader.readNextUrl();
            logger.info("\tRead URL: "+url);
            Website website = new Website(url);
            website.setWebsiteRank(websiteRank);
            analyzeWebsite(website);
        }
        DriverManager.closeChromeDriver();
        dbWriter.writeResultToDatabase(analysisResult);
    }
    private void analyzeWebsite(Website website){
        Request request=new Request(Configuration.REGION_TO_ANALYZE);

        for (AnalysisStep step : steps) {
            step.execute(DriverManager.getCurrentChromeDriver(), website, request);
        }

        analysisResult.addRequest(website,request);
    }

    private void writeResultToDatabase(){
        dbWriter.writeResultToDatabase(analysisResult);
    }

    private void fetchMyIP(){
        logger.info("Fetching my ip address");
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
