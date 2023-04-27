package analysis;

import model.Cookie;
import model.Request;
import model.Website;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.main.Main;
import org.openqa.selenium.WebDriver;

public class CookieReader implements AnalysisStep{
    private static final Logger logger = LogManager.getLogger(CookieReader.class);
    @Override
    public void execute(WebDriver driver, Website website, Request request) {

        for (org.openqa.selenium.Cookie cookie : driver.manage().getCookies()) {

            Cookie newCookie=new Cookie();

            newCookie.setName(cookie.getName());
            newCookie.setValue(cookie.getValue());
            if(cookie.getExpiry()==null)
                newCookie.setExpiryDate("");
            else
                newCookie.setExpiryDate(convertDateFormat(cookie.getExpiry().toString()));

            request.addCookie(newCookie);
            logger.info(String.format("Added Cookie [name: %s, value: %s, date: %s]",newCookie.getName(),newCookie.getValue(),newCookie.getExpiryDate()));
        }
    }

    private String convertDateFormat(String mySqlDate){
        String day=mySqlDate.substring(8,10);
        String month=mySqlDate.substring(4,7);
        switch (month){
            case "Jan":
                month="01";
                break;
            case "Feb":
                month="02";
                break;
            case "Mar":
                month="03";
                break;
            case "Apr":
                month="04";
                break;
            case "May":
                month="05";
                break;
            case "Jun":
                month="06";
                break;
            case "Jul":
                month="07";
                break;
            case "Aug":
                month="08";
                break;
            case "Sep":
                month="09";
                break;
            case "Oct":
                month="10";
                break;
            case "Nov":
                month="11";
                break;
            case "Dec":
                month="12";
                break;
            default:
                month="01";
                break;
        }
        String year=mySqlDate.substring(25,29);

        return year+"-"+month+"-"+day;
    }
}
