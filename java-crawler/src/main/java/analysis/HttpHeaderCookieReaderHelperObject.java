package analysis;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHeaderCookieReaderHelperObject {
    private boolean active;

    private String baseUrl;
    private String path;
    private boolean requestIdIsSet;

    private Set<String> alreadyCheckedIds;


    private Map<String,Double> requestTimestampMap;
    private Map<String, String> requestHeadersMap;
    private Number currentEarliestTime;

    public HttpHeaderCookieReaderHelperObject(String requestUrl) {

        baseUrl=retrieveBaseUrl(requestUrl);

        active=true;
        requestIdIsSet=false;
        currentEarliestTime=Double.MAX_VALUE;
        requestHeadersMap =new HashMap<>();
        requestTimestampMap=new HashMap<>();
        alreadyCheckedIds=new HashSet<>();
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

    }

    public boolean requestIdIsSet(){
        return requestIdIsSet;
    }

    public void putHeader(String requestId,String header){
        requestHeadersMap.put(requestId,header);
        System.out.println("!!!!!!!!!!Put in Map: ID: "+requestId+"; Header: "+header);
    }

    public String getResultCookieHeader(){

        //Get request Id
        String requestId=getNextChronologicalRequestId();

        while(requestId!=null){
            //Get Headers
            String headers= requestHeadersMap.get(requestId);
            System.out.println("!!!!!!!!!!Fetched Headers from Map ("+requestId+"): "+headers);

            if(headers!=null) {
                String authorityValue=retrieveHeaderValue(headers, ":authority");
                if(authorityValue!=null){
                    if (authorityValue.equals(baseUrl)) {
                        String cookieHeader = retrieveHeaderValue(headers, "cookie");
                        if (cookieHeader == null) {
                            cookieHeader = retrieveHeaderValue(headers, "Cookie");
                        }
                        return cookieHeader;
                    }
                }
            }
            requestId = getNextChronologicalRequestId();
        }

        return null;
    }


    public void setNewEarliestTime(Number time){
        this.currentEarliestTime=time;
    }

    public boolean timeIsEarlier(Number newTime){
        double timeOld = currentEarliestTime.doubleValue();
        double timeNew = newTime.doubleValue();
        return timeNew < timeOld;
    }

    private String retrieveBaseUrl(String requestUrl){
        Pattern pattern = Pattern.compile("(?:www\\.|//)([^/]+)");
        Matcher matcher = pattern.matcher(requestUrl);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private String retrieveHeaderValue(String headersString, String headerName){

        String key = headerName+"=";
        String delimiter = ";";

        int keyIndex = headersString.indexOf(key);
        if (keyIndex != -1) {
            int startIndex = keyIndex + key.length();
            int endIndex = headersString.indexOf(delimiter, startIndex);
            if (endIndex != -1) {
                return headersString.substring(startIndex, endIndex);
            } else {
                return headersString.substring(startIndex);
            }
        }
        System.out.println("!!!!!!!!!Came here with values: headersString: "+headersString+"; header name: "+headerName);
        return null;
    }

    private String getNextChronologicalRequestId(){
        double earliestRequestTime=Double.MAX_VALUE;
        String earliestRequestId="";

        for(String requestId: requestTimestampMap.keySet()){
            if(!alreadyCheckedIds.contains(requestId)) {
                double newTimestamp = requestTimestampMap.get(requestId);
                if (newTimestamp < earliestRequestTime) {
                    earliestRequestTime = newTimestamp;
                    earliestRequestId = requestId;
                    alreadyCheckedIds.add(requestId);
                }
            }
        }

        if(earliestRequestTime==Double.MAX_VALUE)
            return null;

        return earliestRequestId;
    }

    private String retrievePath(String requestUrl){
        return "";
    }

}
