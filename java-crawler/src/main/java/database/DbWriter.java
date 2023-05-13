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
                dbHelper.insertIntoWebsite(website.getUrl(),website.getWebsiteRank(),"");
            int websiteId= dbHelper.selectIdFromWebsiteWhere(website.getUrl());

            for(Request request : result.get(website)){
                String status=request.getRequestStatus().toString();
                String originRegion=request.getOriginRegion().toString();
                String date=request.getDate();
                boolean wasDirected=request.getWasRedirected();
                String redirectedPage=request.getRedirectedPage();
                String protocol=request.getProtocol().toString();

                dbHelper.insertIntoRequest(status,originRegion,ipUsed,date,wasDirected,redirectedPage,protocol,websiteId);
                int requestId=dbHelper.selectIdFromRequestWhere(status,originRegion,ipUsed,date,wasDirected,redirectedPage,protocol,websiteId);

                for(Cookie cookie:request.getCookies()){
                    String cookieName=cookie.getName();
                    String cookieValue= cookie.getValue();
                    String expiryDate=cookie.getExpiryDate();

                    dbHelper.insertIntoCookie(cookieName,cookieValue,expiryDate,requestId);
                }
            }
        }

        dbHelper.close();
    }
}
