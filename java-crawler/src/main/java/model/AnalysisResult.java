package model;

import java.util.Map;

public class AnalysisResult {

    private Map<Website,WebsiteResult> analysisResult;

    public void addWebsiteResult(Website website, WebsiteResult result){
        analysisResult.put(website,result);
    }

    public Map<Website,WebsiteResult> getAnalysisResult(){
        return analysisResult;
    }
}
