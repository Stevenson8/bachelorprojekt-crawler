package analysis;

import model.EInternetProtocol;
import model.ERequestStatus;
import model.Request;
import model.Website;

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

        String httpsUrl="https://"+website.getBaseUrl().replaceAll("\\s","")+"/";
        String httpUrl="http://"+website.getBaseUrl().replaceAll("\\s","")+"/";
        String finallyUsedUrl="";

        boolean requestWasSuccessful=true;

        //Set time of the request
        request.setDate(getDate());

        //Try HTTPS and/or HTTP Connection
        try {
            driver.get(httpsUrl);
            request.setProtocol(EInternetProtocol.HTTPS);
            finallyUsedUrl=httpsUrl;
        }
        catch (Exception eHttps){
            try {
                driver.get(httpUrl);
                request.setProtocol(EInternetProtocol.HTTP);
                finallyUsedUrl=httpUrl;
            }
            catch (Exception eHttp){
                request.setProtocol(EInternetProtocol.NOTAPPLICABLE);
                requestWasSuccessful=false;
            }
        }

        //Set Request Status

        if(!requestWasSuccessful){
            request.setRequestStatus(ERequestStatus.ERRONEOUS);
            return;
        }
        else {
            request.setRequestStatus(ERequestStatus.OK);
            request.setOriginalUrl(finallyUsedUrl);
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
