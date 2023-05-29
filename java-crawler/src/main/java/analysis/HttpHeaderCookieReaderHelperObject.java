package analysis;

import org.openqa.selenium.devtools.v113.network.model.Headers;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeaderCookieReaderHelperObject {
    private boolean active;

    private boolean requestIdIsSet;

    private Map<String, String> cookieHeaderMap;
    private Map<String,Double> requestTimestampMap;
    private Number currentEarliestTime;

    public HttpHeaderCookieReaderHelperObject() {
        active=true;
        requestIdIsSet=false;
        currentEarliestTime=Double.MAX_VALUE;
        cookieHeaderMap=new HashMap<>();
        requestTimestampMap=new HashMap<>();
    }

    public boolean isActive() {
        return active;
    }

    public void setDone() {
        active=false;
    }

    public void setRequestTimestamp(String requestId,String timestamp){

        if(timestamp==""||timestamp==null)
            return;

        double number=Double.parseDouble(timestamp);

        requestTimestampMap.put(requestId,number);

        //Todo remove
        System.out.println("\t\t\t###########String timestamp:"+timestamp+"; number: "+number+";");
    }

    public boolean requestIdIsSet(){
        return requestIdIsSet;
    }

    public void putHeader(String requestId,String header){
        cookieHeaderMap.put(requestId,header);
        //todo rem
        System.out.println("\t\t\t\t\t\t~~~~~Helper: Put Header~~~~~requestId: "+requestId+"; Header: "+header);
    }

    public String getResultCookieHeader(){

        double earliestRequestTime=Double.MAX_VALUE;
        String earliestRequestId="";

        for(String requestId: requestTimestampMap.keySet()){
            double newTimestamp=requestTimestampMap.get(requestId);
            if(newTimestamp<earliestRequestTime) {
                earliestRequestTime = newTimestamp;
                earliestRequestId=requestId;
               }
        }

        if(earliestRequestTime==Double.MAX_VALUE)
            return null;



        //Todo rem
        System.out.println("\t\t\t\t~~~~~Earliest RequestId~~~~~"+earliestRequestId);
        System.out.println("\t\t\t\t\t\t~~~~~Headers~~~~~"+cookieHeaderMap.get(earliestRequestId));

        return cookieHeaderMap.get(earliestRequestTime);
    }


    public void setNewEarliestTime(Number time){
        this.currentEarliestTime=time;
    }

    public boolean timeIsEarlier(Number newTime){
        double timeOld = currentEarliestTime.doubleValue();
        double timeNew = newTime.doubleValue();
        return timeNew < timeOld;
    }

}
