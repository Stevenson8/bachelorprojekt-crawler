package analysis;

import model.Request;
import model.Website;
import org.main.DriverManager;
import org.openqa.selenium.chrome.ChromeDriver;


public class WebsiteCloser implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {
        DriverManager.closeChromeDriver();
    }
}
