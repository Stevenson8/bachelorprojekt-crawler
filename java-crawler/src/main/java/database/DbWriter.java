package database;

import model.AnalysisResult;
import model.Cookie;
import model.Request;
import model.Website;

import java.util.List;
import java.util.Map;

public class DbWriter {

    private DatabaseHelper dbHelper;
    public DbWriter() {
        this.dbHelper = new DatabaseHelper();
    }

    public void writeResultToDatabase(AnalysisResult analysisResultResult){
        //Todo
        Map<Website, List<Request>> result=analysisResultResult.getAnalysisResult();

        String ipUsed=analysisResultResult.getUsedIP();

        List<String> websitesAlreadyCreated=dbHelper.selectUrlsFromWebsite();


        for(Website website : result.keySet()){
            if(!websitesAlreadyCreated.contains(website.getUrl()))
                dbHelper.insertIntoWebsite(website.getUrl(),"");
            int websiteId= dbHelper.selectIdFromWebsiteWhere(website.getUrl());

            for(Request request : result.get(website)){
                String status="status haha";
                String originRegion=request.getOriginRegion().toString();
                String date="2022-01-02";
                boolean wasDirected=false;
                String redirectedPage="";
                String protocol="HTTP";

                dbHelper.insertIntoRequest(status,originRegion,ipUsed,date,wasDirected,redirectedPage,protocol,websiteId);
                int requestId=dbHelper.selectIdFromRequestWhere(status,originRegion,ipUsed,date,wasDirected,redirectedPage,protocol,websiteId);

                for(Cookie cookie:request.getCookies()){
                    dbHelper.insertIntoCookie("ga_12345","hvoeurbgoesbg","2019-01-09",requestId);
                }
            }
        }

        dbHelper.close();
    }
}
