package analysis;

import model.EInternetProtocol;
import model.ERequestStatus;
import model.Request;
import model.Website;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tests whether HTTP or HTTPS protocol is to be used in the request.
 * Tests further whether the website redirects.
 * Stores the final URL and date of the request in the Website and Request Object.
 */
public class WebsiteUrlIdentifier implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {
        System.out.println("\tStep: Website UrlIdentifier");

        String httpsUrl="https://"+website.getBaseUrl().replaceAll("\\s","")+"/";
        String httpUrl="http://"+website.getBaseUrl().replaceAll("\\s","")+"/";
        String finallyUsedUrl="";

        boolean requestWasSuccessful=false;
        boolean requestHadTimeout=false;

        //Set time of the request
        request.setDate(getDate());

        //Try HTTPS and/or HTTP Connection
        try {
            driver.get(httpsUrl);
            request.setProtocol(EInternetProtocol.HTTPS);
            finallyUsedUrl=httpsUrl;
            requestWasSuccessful=true;
        }
        catch (TimeoutException e){
            System.err.println(e.getMessage());
            requestHadTimeout=true;
        }
        catch (Exception eHttps){
            System.err.println(eHttps.getMessage());
            try {
                driver.get(httpUrl);
                request.setProtocol(EInternetProtocol.HTTP);
                finallyUsedUrl=httpUrl;
                requestWasSuccessful=true;
            }
            catch (TimeoutException e){
                System.err.println(e.getMessage());
                requestHadTimeout=true;
            }
            catch (Exception eHttp){
                System.err.println(eHttp.getMessage());
            }
        }

        //Set Request Status
        if(!requestWasSuccessful||requestHadTimeout)
            request.setProtocol(EInternetProtocol.NOTAPPLICABLE);

        if(requestWasSuccessful){
            request.setRequestStatus(ERequestStatus.OK);
            request.setOriginalUrl(finallyUsedUrl);
        }
        else if(requestHadTimeout){
            request.setRequestStatus(ERequestStatus.TIMEOUT);
            return;
        }
        else{
            request.setRequestStatus(ERequestStatus.ERRONEOUS);
            return;
        }

        //Set potential redirection
        if(!driver.getCurrentUrl().equals(finallyUsedUrl)){
            request.setWasRedirected(true);
            request.setRedirectedUrl(driver.getCurrentUrl());
        }
        else {
            request.setWasRedirected(false);
            request.setRedirectedUrl("");
        }
    }

    private String getDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
