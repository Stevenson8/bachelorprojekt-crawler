package analysis;

import model.ERequestStatus;
import model.Request;
import model.Website;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebsiteCaller implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {

        if(request.getRequestStatus().equals(ERequestStatus.ERRONEOUS)){
            return;
        }

        if(request.wasRedirected()){
            driver.get(request.getRedirectedUrl());
        }
        else{
            driver.get(request.getOriginalUrl());
        }
    }
}
