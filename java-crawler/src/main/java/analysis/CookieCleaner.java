package analysis;

import model.Request;
import model.Website;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.chrome.ChromeDriver;

public class CookieCleaner implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {
        System.out.println("\tStep: Cookie Cleaner");

        driver.get("chrome:\\settings\\clearBrowserData");
    }
}
