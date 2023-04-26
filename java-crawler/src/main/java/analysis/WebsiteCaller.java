package analysis;

import model.Request;
import model.Website;
import org.openqa.selenium.WebDriver;

public class WebsiteCaller implements AnalysisStep{
    @Override
    public void execute(WebDriver driver, Website website, Request request) {

        String url="https://"+website.getUrl();
        driver.get(url);
    }
}
