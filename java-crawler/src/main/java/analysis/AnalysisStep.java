package analysis;

import model.Request;
import model.Website;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.chrome.ChromeDriver;

public interface AnalysisStep {
    void execute(ChromeDriver driver, Website website, Request request);
}
