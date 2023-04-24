package analysis;

import model.Request;
import org.openqa.selenium.WebDriver;

public interface AnalysisStep {
    void execute(WebDriver driver, Request request);
}
