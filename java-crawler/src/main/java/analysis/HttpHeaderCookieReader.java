package analysis;

import model.Cookie;
import model.ERequestStatus;
import model.Request;
import model.Website;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.main.DriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHeaderCookieReader implements AnalysisStep{
    private static final Logger logger = LogManager.getLogger(HttpHeaderCookieReader.class);
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {

        if(request.getRequestStatus().equals(ERequestStatus.ERRONEOUS))
            return;

        String requestUrl;
        if(request.wasRedirected())
            requestUrl=request.getRedirectedUrl();
        else
            requestUrl=request.getOriginalUrl();

        //Start listening to network
        DriverManager.startListeningToNetwork();

        //Fetch website
        driver.get(requestUrl);

        //End listening to network
        Har har=DriverManager.endListeningToNetworkAndGetHar();

        for(var x:har.getLog().getPages()){
            System.out.println("\t\t\t~~~~~~~Pages~~~~"+x.getTitle());
        }

        String cookieHeader=null;
        for(HarEntry entry:har.getLog().getEntries()){
            System.out.println("\t\t\t********Entries Url~~~~"+entry.getRequest().getUrl());
            System.out.println("\t\t\t\t********Entries Headers~~~~"+entry.getRequest().getHeaders().toString());
            if(entry.getRequest().getUrl().equals(requestUrl)) {
                List<HarNameValuePair> headerPairs=entry.getRequest().getHeaders();

                for(HarNameValuePair pair:headerPairs){
                    if(pair.getName().equals("Cookie")){
                        cookieHeader =pair.getValue();
                    }
                }
            }
        }

        if(cookieHeader==null)
            return;

        System.out.println("!!!!!Result Cookie Header: "+cookieHeader);


        Map<String, String> cookieMap=getMapOfCookies(cookieHeader);

        writeCookiesFromMapToRequest(cookieMap,request);

    }

    //Example of format of the headers.get("cookie"):
    //AEC=AUEFqZed4aHcneMBC6TcUAtqUH4-nLQclqayR846Rse50cS5uE_duHa_xw; __Secure-ENID=12.SE=C7GS;
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
                String key = pair.substring(0, equalsIndex).trim();
                String value = pair.substring(equalsIndex + 1).trim();
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
     *
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
