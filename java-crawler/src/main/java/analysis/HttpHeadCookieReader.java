package analysis;

import model.Request;
import model.Website;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v113.network.Network;

import java.util.Map;
import java.util.Optional;

public class HttpHeadCookieReader implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {



// Set the path to the ChromeDriver executable

        // Set Chrome options
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("w3c", true);
        options.addArguments("--start-maximized");

        // Initialize ChromeDriver
        ChromeDriver driver2 = new ChromeDriver(options);

        // Enable DevTools
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        // Enable Network domain
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Set up a listener to intercept network requests
        devTools.addListener(Network.requestWillBeSentExtraInfo(),
                requ -> {


                    System.out.println("**********Request: " + requ);
                    System.out.println("Request Headers: " + requ.getHeaders());
                }
        );

        // Navigate to a website
        driver2.get("https://www.google.at");


        // Quit the driver
        driver2.quit();
    }
}
