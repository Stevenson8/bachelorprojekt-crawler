package analysis;

import model.Cookie;
import model.ERequestStatus;
import model.Request;
import model.Website;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;

import net.lightbody.bmp.core.har.HarResponse;
import org.main.DriverManager;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookieHeaderReader implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {
        System.out.println("\tStep: Cookie Header Reader");

        ERequestStatus status=request.getRequestStatus();
        if(status.equals(ERequestStatus.ERRONEOUS)||status.equals(ERequestStatus.TIMEOUT)){
            return;
        }

        //Get the Url to work with
        String requestUrl;
        if(request.wasRedirected())
            requestUrl=request.getRedirectedUrl();
        else
            requestUrl=request.getOriginalUrl();

        //Start listening to network
        DriverManager.startListeningToNetwork();

        //Fetch website
        try {
            driver.get(requestUrl);
        }
        catch (TimeoutException e){
            System.err.println(e.getMessage());
            request.setRequestStatus(ERequestStatus.TIMEOUT);
            return;
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            request.setRequestStatus(ERequestStatus.ERRONEOUS);
            return;
        }

        //End listening to network and get Har Object
        Har har=DriverManager.endListeningToNetworkAndGetHar();

        //Retrieve the Cookie Header from Har Object
        String cookieHeader=null;
        boolean headersNotYetFound=true;
        boolean responseFailure=false;

        for(HarEntry entry:har.getLog().getEntries()){
            if(entry.getRequest().getUrl().equals(requestUrl)&&headersNotYetFound) {
                System.out.println("\t-> Found headers in har object");
                headersNotYetFound=false;
                List<HarNameValuePair> headerPairs=entry.getRequest().getHeaders();

                //Check if response is valid:
                HarResponse response=entry.getResponse();
                if(response.getError()!=null || response.getStatus()>=400 || response.getHeaders().size()==0) {
                    System.out.println("\tResponse Failure. Status Code: "+response.getStatus());
                    responseFailure = true;
                }

                //Fetch the cookie header from the headers
                for(HarNameValuePair pair:headerPairs){
                    if(pair.getName().equalsIgnoreCase("Cookie")){
                        cookieHeader =pair.getValue();
                    }
                }
            }
        }

        //If either the request/response has no headers or the response has failed: mark request erroneous
        if(headersNotYetFound||responseFailure){
            request.setRequestStatus(ERequestStatus.ERRONEOUS);
            return;
        }

        if(cookieHeader==null)
            return;

        //Convert the header and write results to the request object
        Map<String, String> cookieMap=getMapOfCookies(cookieHeader);

        writeCookiesFromMapToRequest(cookieMap,request);
    }

    private Map<String, String> getMapOfCookies(String cookieHeaderString){

        Map<String, String> result = new HashMap<>();
        if(cookieHeaderString.equals(""))
            return result;

        // Split the string into individual key-value pairs
        String[] pairs = cookieHeaderString.split(";");

        // Iterate over each key-value pair and split them into key and value
        for (String pair : pairs) {
            int equalsIndex = pair.indexOf("=");

            if (equalsIndex != -1) {
                String key = pair.substring(0, equalsIndex).trim().replaceAll("\"","");
                String value = pair.substring(equalsIndex + 1).trim().replaceAll("\"","");
                result.put(key, value);
            }
        }

        return result;
    }

    private void writeCookiesFromMapToRequest(Map<String, String> cookieMap, Request request){

        for(String key:cookieMap.keySet()){
            String value=cookieMap.get(key);

            Cookie cookie=new Cookie();
            cookie.setName(key);
            cookie.setValue(value);

            request.addCookie(cookie);
        }
    }

    /**
     * Checks, whether an URL Path matches.
     * www.washington.edu and https://www.washington.edu/ would match e.g.
     */
    private boolean urlPathMatches(String url1, String url2){

        String trimmedUrl1="";
        String trimmedUrl2="";

        Pattern pattern = Pattern.compile("www\\.(\\S+)");

        Matcher matcher = pattern.matcher(url1);
        if (matcher.find()) {
            trimmedUrl1=matcher.group(1);
            trimmedUrl1.trim();
            if(trimmedUrl1.endsWith("/"))
                trimmedUrl1=trimmedUrl1.substring(0, trimmedUrl1.length() - 1);
        }

        matcher = pattern.matcher(url2);
        if (matcher.find()) {
            trimmedUrl2=matcher.group(1);
            trimmedUrl2.trim();
            if(trimmedUrl2.endsWith("/"))
                trimmedUrl2=trimmedUrl2.substring(0, trimmedUrl2.length() - 1);
        }

        if(trimmedUrl1.equals("")||trimmedUrl2.equals(""))
            return false;

        return trimmedUrl1.equals(trimmedUrl2);
    }
}
