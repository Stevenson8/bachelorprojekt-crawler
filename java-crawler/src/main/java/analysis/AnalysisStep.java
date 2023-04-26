package analysis;

import model.Request;
import model.Website;
import org.openqa.selenium.WebDriver;

public interface AnalysisStep {
    void execute(WebDriver driver, Website website, Request request);
}
