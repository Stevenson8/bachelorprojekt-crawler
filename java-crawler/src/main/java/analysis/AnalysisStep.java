package analysis;

import model.WebsiteResult;
import org.openqa.selenium.WebDriver;

public interface AnalysisStep {
    void execute(WebDriver driver, WebsiteResult result);
}
