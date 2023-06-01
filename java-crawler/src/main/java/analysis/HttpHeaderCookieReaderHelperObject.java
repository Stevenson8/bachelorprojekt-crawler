package analysis;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHeaderCookieReaderHelperObject {
    private boolean active;


    private boolean requestIdSet;

    private String requestId;
    private Map<String,String> headers;


    public HttpHeaderCookieReaderHelperObject(String requestUrl) {
        requestIdSet=false;
        requestId=null;
        headers=new HashMap<>();
    }

    public boolean requestIdIsSet() {
        return requestIdSet;
    }

    public void setRequestId(String requestId){
        System.out.println("aaaaaaaaaSet Request ID: {"+requestId+"}");
        this.requestId=requestId;
        requestIdSet=true;
    }

    public void setHeader(String requestId, String header){
        System.out.println("zzzzzzzzzSet Header with ID"+requestId+": {"+header+"}");
        headers.put(requestId,header);
    }



    public String getResultCookieHeader(){
        System.out.println("cccccccccGet Result Cookie Header:");


        String header=headers.get(requestId);
        System.out.println("\t\t\t\tFetched from Map: "+header);
        if(header==null)
            return null;

        String cookieHeader=null;
        cookieHeader=retrieveHeaderValue(header,"cookie");
        System.out.println("\t\t\t\tRetrieved with c!ookie: "+cookieHeader);
        if(cookieHeader==null) {
            cookieHeader = retrieveHeaderValue(header, "Cookie");
            System.out.println("\t\t\t\tRetrieved with C!ookie: " + cookieHeader);
        }

        return cookieHeader;
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





}
