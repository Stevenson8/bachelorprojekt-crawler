package model;

import java.util.List;

public class WebsiteResult {
    private List<Request> requests;
    private boolean isFinished;

    public WebsiteResult() {
        isFinished=false;
    }

    public boolean isFinished(){
        //Todo
        return isFinished;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setIsFinished(boolean value) {
        isFinished = value;
    }

}
