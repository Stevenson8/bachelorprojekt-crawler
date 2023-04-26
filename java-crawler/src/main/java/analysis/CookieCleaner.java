package analysis;

import model.Request;
import model.Website;
import org.openqa.selenium.WebDriver;

public class CookieCleaner implements AnalysisStep{
    @Override
    public void execute(WebDriver driver, Website website, Request request) {
        driver.get("chrome:\\settings\\clearBrowserData");
    }
}
