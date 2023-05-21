package analysis;

import configuration.Configuration;
import model.Request;
import model.Website;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebdriverStarter implements AnalysisStep{
    @Override
    public void execute(ChromeDriver driver, Website website, Request request) {
        ChromeOptions options=getChromeOptions();

        if(options==null){
            driver=new ChromeDriver();
        }
        else{
            driver=new ChromeDriver(options);
        }
    }

    private ChromeOptions getChromeOptions(){
        ChromeOptions options=new ChromeOptions();
        boolean optionsIsFilled=false;

        //Set whether driver is headless
        if(Configuration.WEBDRIVER_IS_HEADLESS){
            options.addArguments("--headless");
            optionsIsFilled=true;
        }

        //Set VPN
        if(Configuration.PROXY_IS_USED){
            Proxy p=new Proxy();
            p.setHttpProxy(Configuration.PROXY);
            options.setCapability("proxy",p);

            //options.addArguments("--proxy-server=" + Configuration.PROXY);
            optionsIsFilled=true;
        }

        if(optionsIsFilled)
            return options;
        else
            return null;
    }
}
