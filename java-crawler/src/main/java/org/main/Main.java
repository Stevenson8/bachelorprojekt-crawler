package org.main;

import configuration.Configuration;
import database.DbWriter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        BasicConfigurator.configure();
        //Logger.getLogger("org.openqa.selenium").setLevel(Level.ERROR);
        logger.info("Started the Program in Main Method");

        Controller controller=new Controller();
        controller.doAnalysis();
    }
}