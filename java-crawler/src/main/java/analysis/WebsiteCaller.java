package analysis;

import model.ERequestStatus;
import model.Request;
import model.Website;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebsiteCaller implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {

        ERequestStatus status=request.getRequestStatus();
        if(status.equals(ERequestStatus.ERRONEOUS)||status.equals(ERequestStatus.TIMEOUT)){
            return;
        }

        try {
            if(request.wasRedirected()){
                driver.get(request.getRedirectedUrl());
            }
            else{
                driver.get(request.getOriginalUrl());
            }
        }
        catch (TimeoutException e){
            System.err.println(e.getMessage());
            request.setRequestStatus(ERequestStatus.TIMEOUT);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            request.setRequestStatus(ERequestStatus.ERRONEOUS);
        }
    }
}
