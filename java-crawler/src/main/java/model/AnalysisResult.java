package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisResult {

    private Map<Website, List<Request>> analysisResult;
    private String usedIP="";

    public AnalysisResult() {
        this.analysisResult=new HashMap<>();
    }

    public void addRequest(Website website, Request request){
        if(analysisResult.keySet().contains(website)){
            analysisResult.get(website).add(request);
        }
        else{
            List<Request> requests=List.of(request);
            analysisResult.put(website,requests);
        }
    }

    public Map<Website,List<Request>> getAnalysisResult(){
        return analysisResult;
    }

    public boolean alreadyContainsRequestForWebsite(Website website){
        return analysisResult.keySet().contains(website);
    }
    public List<Request> getRequests(Website website){
        return analysisResult.get(website);
    }

    public void setUsedIP(String usedIP) {
        this.usedIP = usedIP;
    }

    public String getUsedIP() {
        return usedIP;
    }
}
