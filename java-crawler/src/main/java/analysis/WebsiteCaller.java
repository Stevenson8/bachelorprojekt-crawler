package analysis;

import model.EInternetProtocol;
import model.ERequestStatus;
import model.Request;
import model.Website;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class WebsiteCaller implements AnalysisStep{
    private static final Logger logger = LogManager.getLogger(WebsiteCaller.class);
    @Override
    public void execute(WebDriver driver, Website website, Request request) {

        String httpsUrl="https://www."+website.getUrl()+"/";
        String httpUrl="http://www."+website.getUrl()+"/";
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
            System.err.println("[!] Error https for: "+httpsUrl);
            try {
                driver.get(httpUrl);
                request.setProtocol(EInternetProtocol.HTTP);
                finallyUsedUrl=httpUrl;
            }
            catch (Exception eHttp){
                System.err.println("[!] Error http for: "+httpUrl);
                request.setProtocol(EInternetProtocol.NOTAPPLICABLE);
                requestWasSuccessful=false;
            }
        }

        //Set Request Status
        if(!requestWasSuccessful){
            request.setRequestStatus(ERequestStatus.ERRONEOUS);
            return;
        }
        else request.setRequestStatus(ERequestStatus.OK);


        //Set potential redirection
        if(driver.getCurrentUrl()!=finallyUsedUrl){
            request.setWasRedirected(true);
            request.setRedirectedPage(driver.getCurrentUrl());
        }
        else {
            request.setWasRedirected(false);
            request.setRedirectedPage("");
        }

        logger.info(String.format("Called Website [%s -> %s]: {Status: %s, Date: %s}",finallyUsedUrl,request.getRedirectedPage(),request.getRequestStatus(),request.getDate()));

    }

    private String getDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
