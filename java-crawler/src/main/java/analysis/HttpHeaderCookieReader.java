package analysis;

import model.Cookie;
import model.ERequestStatus;
import model.Request;
import model.Website;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.main.Controller;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v113.network.Network;
import org.openqa.selenium.devtools.v113.network.model.Headers;




import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

        HttpHeaderCookieReaderHelperObject helper=new HttpHeaderCookieReaderHelperObject(requestUrl);

        // Enable DevTools
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));



        devTools.addListener(Network.requestWillBeSent(), fetchResult -> {

            System.out.println("\t\t......ID: "+fetchResult.getRequestId()+"..........."+fetchResult.getRequest().getUrl());
            synchronized (System.out) {
                //todo rem
                System.out.println("\t\t********ID**"+fetchResult.getRequestId());
                System.out.println("\t\t\t\t**********[Timestamp]: "+fetchResult.getTimestamp());
            }

            String requestId=fetchResult.getRequestId().toString();
            if(requestId.equals(""))
                return;

            String timestamp=fetchResult.getTimestamp().toString();

            helper.setRequestTimestamp(requestId,timestamp);
        });

        devTools.addListener(Network.requestWillBeSentExtraInfo(), fetchResult -> {

            synchronized (System.out) {
                //todo rem
                System.out.println("\t\t------ID--"+fetchResult.getRequestId());
                System.out.println("\t\t\t\t---------[Headers]: "+fetchResult.getHeaders());
            }


            String requestId=fetchResult.getRequestId().toString();

            String headers=fetchResult.getHeaders().toString();

            if(headers.length()>2){
                helper.putHeader(requestId,headers);
            }


            /*
                Number requestTime=fetchResult.getConnectTiming().getRequestTime();


                if(helper.timeIsEarlier(requestTime)){
                    helper.setNewEarliestTime(requestTime);

                    System.out.println("!!!!!!Earliest RequestTime!!!!!" + requestTime.doubleValue());
                    for(var cookieHolder: fetchResult.getAssociatedCookies()){
                        System.out.println("!!!!!Cookie!!!!!!" + cookieHolder.getCookie().getName());
                    }
                }
*/




                /*
                if(fetchResult.getHeaders().get("Host") != null) {

                    if(urlPathMatches(fetchResult.getHeaders().get("Host").toString(),requestUrl)) {

                        System.out.println("!!!!!!Host!!!!!" + fetchResult.getHeaders().get("Host"));
                        System.out.println("!!!!!!My Request:!!!!!" + requestUrl);
                        for(var cookieHolder: fetchResult.getAssociatedCookies()){
                            System.out.println("!!!!!Cookie!!!!!!" + cookieHolder.getCookie().getName());

                            Cookie cookie=new Cookie();
                            cookie.setName(cookieHolder.getCookie().getName());
                            cookie.setValue(cookieHolder.getCookie().getValue());
                            request.addCookie(cookie);
                        }

                        //writeCookieToRequest(request,fetchResult.getHeaders());
                        helper.setDone();
                    }
                }*/
        });



        //Call website to trigger requests
        if(request.getRequestStatus().equals(ERequestStatus.ERRONEOUS)){
            return;
        }
        if(request.wasRedirected()){
            driver.get(request.getRedirectedUrl());
        }
        else{
            driver.get(request.getOriginalUrl());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Tidy up
        devTools.clearListeners();

        String cookieHeader=helper.getResultCookieHeader();
        System.out.println("\t\t\t\t!!!!!!!!!Resulst Cookie Header"+cookieHeader);
        if(cookieHeader!=null)
            writeCookieToRequest(request,cookieHeader);
    }

    private void writeCookieToRequest(Request request,String cookieHeader){

        Map<String,String> cookies=getMapOfCookies(cookieHeader);

        for(String key :cookies.keySet()) {
            Cookie cookie =new Cookie();
            cookie.setName(key);
            cookie.setValue(cookies.get(key));
            request.addCookie(cookie);
            logger.info("\tAdded Cookie: "+cookie.getName()+" {"+cookie.getValue()+"}");
        }
    }

    //Example of format of the headers.get("cookie"):
    //AEC=AUEFqZed4aHcneMBC6TcUAtqUH4-nLQclqayR846Rse50cS5uE_duHa_xw; __Secure-ENID=12.SE=C7GS;
    private Map<String, String> getMapOfCookies(String cookieString){

        Map<String, String> result = new HashMap<>();
        if(cookieString.equals(""))
            return result;


        // Split the string into individual key-value pairs
        String[] pairs = cookieString.split(";");

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
