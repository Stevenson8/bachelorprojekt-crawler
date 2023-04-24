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

        int websiteIdCounter=1;
        int requestIdCounter=1;
        int cookieIdCounter=1;

        for(Website website : result.keySet()){

            dbHelper.insertIntoWebsite(websiteIdCounter,website.getUrl(),"");

            for(Request request : result.get(website)){

                dbHelper.insertIntoRequest(requestIdCounter,"status haha","EU","2022-01-01",false,"","HTTP");

                for(Cookie cookie:request.getCookies()){
                    dbHelper.insertIntoCookie(cookieIdCounter,"ga_12345","hvoeurbgoesbg","2019-01-09");
                    dbHelper.insertIntoRequestHasCookie(requestIdCounter,cookieIdCounter);
                    cookieIdCounter++;
                }
                dbHelper.insertIntoWebsiteHasRequest(websiteIdCounter,requestIdCounter);
                requestIdCounter++;
            }
            websiteIdCounter++;
        }







        dbHelper.close();
    }
}
