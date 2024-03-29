package configuration;

import model.ERegion;

import java.util.List;

public class Configuration {

    public final static String WEBDRIVER_PATH = ("chromedriver_win32\\chromedriver.exe");
    public final static String DOMAIN_CSV_PATH= ("csv_files\\tranco_list_top-10k_20230424.csv");
    public final static String MY_IP_API="https://api.ipify.org/?";
    public final static int WEBSITE_RANK_START=901;
    public final static int WEBSITE_RANK_END=1000;
    public final static boolean WEBDRIVER_IS_HEADLESS =true;
    public final static ERegion REGION_TO_ANALYZE= ERegion.USA;
    public final static boolean FETCH_IP_ADDRESS=true;
}
