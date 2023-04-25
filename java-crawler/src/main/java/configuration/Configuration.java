package configuration;

import model.ERegion;

import java.util.List;

public class Configuration {

    public final static String WEBDRIVER_PATH = ("chromedriver_win32\\chromedriver.exe");
    public final static String DOMAIN_CSV_PATH= ("csv_files\\tranco_list_top-1k_20230424.csv");

    public final static int NUMBER_OF_WEBSITES=5;
    public final static boolean WEBDRIVER_IS_HEADLESS =false;
    public final static ERegion REGION_TO_ANALYZE= ERegion.EU;
    public final static boolean PROXY_IS_USED=true;
    public final static String PROXY="11.456.448.110:8080";

}
