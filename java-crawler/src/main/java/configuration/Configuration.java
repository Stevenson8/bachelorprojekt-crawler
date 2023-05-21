package configuration;

import model.ERegion;

import java.util.List;

public class Configuration {

    public final static String WEBDRIVER_PATH = ("chromedriver_win32\\chromedriver.exe");
    public final static String DOMAIN_CSV_PATH= ("csv_files\\tranco_list_top-1k_20230424.csv");
    public final static String MY_IP_API="https://api.ipify.org/?";

    public final static int WEBSITE_RANK_START=1;
    public final static int WEBSITE_RANK_END=5;
    public final static boolean WEBDRIVER_IS_HEADLESS =false;
    public final static ERegion REGION_TO_ANALYZE= ERegion.EU;
    public final static boolean PROXY_IS_USED=false;
    public final static String PROXY="95.217.107.234:3128";
    public final static boolean FETCH_IP_ADDRESS=false;


}
